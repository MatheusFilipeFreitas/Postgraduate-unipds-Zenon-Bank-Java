package br.com.zenon.presentation.implementation;

public class BenchmarkPresenter extends Presenter {
    public void printElapsed(double elapsedMs) {
        displayMessageFromKey("benchmark.elapsed");
        IO.println(+ elapsedMs + " ms");
    }
}
