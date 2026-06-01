package br.com.zenon.service.implementation;

import br.com.zenon.io.implementation.ReportTransactionMapper;
import br.com.zenon.models.ReportTransaction;
import br.com.zenon.presentation.TransactionReportPresenter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class TransactionReport {
    private final ReportTransactionMapper mapper = new ReportTransactionMapper();
    private final TransactionReportPresenter presenter = new TransactionReportPresenter();

    public void readReport(String path) throws Exception {
        try (Stream<String> lines = Files.lines(Path.of(path))) {
            ReportTransaction report = mapper.aggregate(lines);
            presenter.display(report);
        }
    }
}
