package br.com.zenon.presentation;

public class CsvParseErrorPresenter {
    public void printParseError(String message, String line) {
        IO.println("Error: " + message + " -> " + line);
    }
}
