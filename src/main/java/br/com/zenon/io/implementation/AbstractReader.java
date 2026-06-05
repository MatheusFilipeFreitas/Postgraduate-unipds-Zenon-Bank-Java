package br.com.zenon.io.implementation;

import br.com.zenon.constants.DatabaseConstant;
import br.com.zenon.constants.Limits;
import br.com.zenon.io.IMapper;
import br.com.zenon.io.IReader;
import br.com.zenon.presentation.implementation.CsvParseErrorPresenter;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class AbstractReader<T> implements IReader<T> {
    private final CsvParseErrorPresenter errorPresenter = new CsvParseErrorPresenter();
    private final Semaphore semaphore = new Semaphore(100);

    @Override
    public List<T> read(IMapper<T> mapper, String path, int limit) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(Path.of(path), StandardCharsets.UTF_8)) {
            String headerLine = reader.readLine();
            if (headerLine == null) {
                return List.of();
            }

            mapper.initializeHeaders(headerLine.split(","));
            List<T> list = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                try {
                    list.add(mapper.parse(line.split(",")).get());
                } catch (Exception e) {
                    errorPresenter.printParseError(e.getMessage(), line);
                }
                if (list.size() >= limit) {
                    break;
                }
            }
            return list;
        }
    }

    public void readLazy(IMapper<T> mapper, String path, Consumer<List<T>> consumer) throws IOException, Exception {
        try(
                ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
                Stream<String> lines = Files.lines(Path.of(path))
        ) {
            List<Future<?>> futures = new ArrayList<>();
            Iterator<String> iterator = lines.iterator();
            if (iterator.hasNext()) {
                String line = iterator.next();
                mapper.initializeHeaders(line.split(","));
            }

            List<String> lineBatch = new ArrayList<>();
            while (iterator.hasNext()) {
                String line = iterator.next();
                lineBatch.add(line);

                if (lineBatch.size() >= Limits.CONCURRENCY_LINE_BARCH) {
                    handleBatchExecution(mapper, consumer, executorService, futures, lineBatch);
                }
            }

            if (!lineBatch.isEmpty()) {
                handleBatchExecution(mapper, consumer, executorService, futures, lineBatch);
            }

            for (Future<?> f : futures) {
                IO.println(f.get());
            }
        }
    }

    private void handleBatchExecution(IMapper<T> mapper, Consumer<List<T>> consumer, ExecutorService executorService, List<Future<?>> futures, List<String> lineBatch) {
        List<String> batchCopy = List.copyOf(lineBatch);
        lineBatch.clear();
        futures.add(executorService.submit(() -> {
            try {
                executeBatch(batchCopy, consumer, mapper);
            } catch (Exception e) {
                errorPresenter.printParseError(e.getMessage(), "batch");
            }
        }));
    }

    private void executeBatch(List<String> lineBatch, Consumer<List<T>> consumer, IMapper<T> mapper) throws Exception {
        List<T> batch = lineBatch
            .stream()
            .map(line -> {
                try {
                    return mapper.parse(line.split(","));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            })
            .filter(Optional::isPresent)
            .map(Optional::get)
            .toList();

        try {
            semaphore.acquire();
            try {
                consumer.accept(batch);
            } finally {
                semaphore.release();
            }
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
