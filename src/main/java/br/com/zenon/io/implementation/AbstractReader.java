package br.com.zenon.io.implementation;

import br.com.zenon.io.IMapper;
import br.com.zenon.io.IReader;
import br.com.zenon.presentation.implementation.CsvParseErrorPresenter;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class AbstractReader<T> implements IReader<T> {
    private final CsvParseErrorPresenter errorPresenter = new CsvParseErrorPresenter();

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
                    list.add(mapper.parse(line.split(",")));
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
}
