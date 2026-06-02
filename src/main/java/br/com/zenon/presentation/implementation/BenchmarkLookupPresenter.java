package br.com.zenon.presentation.implementation;

import br.com.zenon.models.Transaction;

import java.util.Optional;

public class BenchmarkLookupPresenter extends Presenter {
    public void printBlankLine() {
        IO.println();
    }

    public void promptTargetName() {
        IO.println();
        displayMessageFromKey("benchmark.target_input");
    }

    public void printNotFound(String targetOriginName) {
        displayMessageFromKey("benchmark.not_found");
        IO.println(targetOriginName);
    }

    public void printTransaction(Optional<Transaction> transaction) {
        transaction.ifPresent(IO::println);
    }
}
