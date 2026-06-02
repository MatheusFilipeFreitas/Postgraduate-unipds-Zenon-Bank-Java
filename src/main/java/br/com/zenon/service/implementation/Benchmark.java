package br.com.zenon.service.implementation;

import br.com.zenon.presentation.implementation.BenchmarkPresenter;
import br.com.zenon.service.IBenchmark;

public class Benchmark implements IBenchmark {
    private final BenchmarkPresenter presenter = new BenchmarkPresenter();
    private long startNanos;

    @Override
    public void start() {
        startNanos = System.nanoTime();
    }

    @Override
    public void stop() {
        long elapsedNanos = System.nanoTime() - startNanos;
        double elapsedMs = elapsedNanos / 1_000_000.0;
        presenter.printElapsed(elapsedMs);
    }
}
