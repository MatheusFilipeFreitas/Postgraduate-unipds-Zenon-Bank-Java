package br.com.zenon.presentation.implementation;

public class IngestPresenter extends Presenter {
    public void printIngestLimit(int limit) {
        displayFormattedMessage("ingest.lines", String.valueOf(limit));
    }
}
