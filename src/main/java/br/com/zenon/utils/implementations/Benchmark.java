package br.com.zenon.utils.implementations;

import br.com.zenon.utils.IBenchmark;

public class Benchmark implements IBenchmark {
    private long startNanos;

    @Override
    public void start() {
        startNanos = System.nanoTime();
    }

    @Override
    public void stop() {
        long elapsedNanos = System.nanoTime() - startNanos;
        double elapsedMs = elapsedNanos / 1_000_000.0;
        IO.println("Elapsed: " + elapsedMs + " ms");
    }
}