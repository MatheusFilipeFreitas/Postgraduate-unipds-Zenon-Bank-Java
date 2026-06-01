package br.com.zenon.utils.implementations;

import br.com.zenon.models.ReportTransaction;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class TransactionReport {
    private final ReportTransactionMapper mapper = new ReportTransactionMapper();

    public void readReport(String path) throws Exception {
        try (Stream<String> lines = Files.lines(Path.of(path))) {
            ReportTransaction report = mapper.aggregate(lines);
            displayReport(report);
        }
    }

    private void displayReport(ReportTransaction report) {
        IO.println("Total transactions: " + report.totalTransactions());
        IO.println("Total frauds: " + report.totalFrauds());
        IO.println("Total amount: " + report.totalAmount());
    }
}
