package br.com.zenon.presentation;

import br.com.zenon.models.Transaction;

import java.util.Optional;

public class BenchmarkLookupPresenter {
    public void printBlankLine() {
        IO.println();
    }

    public void promptTargetName() {
        IO.println();
        IO.println("Target Name: ");
    }

    public void printNotFound(String targetOriginName) {
        IO.println("Could not find transaction with origin name: " + targetOriginName);
    }

    public void printTransaction(Optional<Transaction> transaction) {
        transaction.ifPresent(IO::println);
    }
}
