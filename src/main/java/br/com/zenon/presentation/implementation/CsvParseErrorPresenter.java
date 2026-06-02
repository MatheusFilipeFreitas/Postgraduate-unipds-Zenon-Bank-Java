package br.com.zenon.presentation.implementation;

public class CsvParseErrorPresenter extends Presenter {
    public void printParseError(String message, String line) {
        displayMessageFromKey("csv.error");
        IO.println(message + " -> " + line);
    }
}
