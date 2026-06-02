package br.com.zenon.app;

import br.com.zenon.constants.FilePath;
import br.com.zenon.constants.type.CollectionType;
import br.com.zenon.io.implementation.PresenterConfiguration;
import br.com.zenon.models.Transaction;
import br.com.zenon.presentation.implementation.BenchmarkLookupPresenter;
import br.com.zenon.repository.ITransactionRepository;
import br.com.zenon.repository.implementation.TransactionRepository;
import br.com.zenon.service.IAnalyzer;
import br.com.zenon.service.IBenchmark;
import br.com.zenon.service.implementation.Benchmark;
import br.com.zenon.service.implementation.FraudAnalyzer;
import br.com.zenon.service.implementation.TransactionIngestor;
import br.com.zenon.service.implementation.TransactionReport;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class ZenonApplication {
    private final IAnalyzer<Transaction> analyzer = new FraudAnalyzer();
    private final IBenchmark benchmark = new Benchmark();
    private final TransactionReport transactionReport = new TransactionReport();
    private final BenchmarkLookupPresenter lookupPresenter = new BenchmarkLookupPresenter();
    private final PresenterConfiguration presenterConfiguration = PresenterConfiguration.getInstance();

    private ITransactionRepository transactionRepository;

    public void run() throws Exception {
        runPeerLocale(Locale.ENGLISH);
        runPeerLocale(Locale.of("pt","BR"));
    }

    public void runIngestAndDashboard() throws IOException {
        initRepository();
        displayDashboardAndAllTransactions();
    }

    public void runBenchmarkLookup() throws IOException {
        initRepository();
        String targetOriginName = promptTargetName();
        handleBenchmarkByType(CollectionType.LIST, targetOriginName);
        handleBenchmarkByType(CollectionType.MAP, targetOriginName);
    }

    private void runPeerLocale(Locale locale) throws Exception {
        presenterConfiguration.setLocale(locale);
        runIngestAndDashboard();
        runBenchmarkLookup();
        runReportReader();
    }

    private void runReportReader() throws Exception {
        lookupPresenter.printBlankLine();
        transactionReport.readReport(FilePath.CSV_FILE_PATH);
    }

    private void initRepository() throws IOException {
        TransactionIngestor ingestor = new TransactionIngestor(FilePath.CSV_FILE_PATH);
        transactionRepository = new TransactionRepository(ingestor.getTransactions());
    }

    private void displayDashboardAndAllTransactions() {
        List<Transaction> transactions = transactionRepository.getAllTransactions();
        IO.println();
        transactions.forEach(IO::println);
        IO.println();
        analyzer.analyze(transactions);
    }

    private String promptTargetName() {
        lookupPresenter.promptTargetName();
        return IO.readln();
    }

    private void handleBenchmarkByType(CollectionType collectionType, String targetOriginName) {
        lookupPresenter.printBlankLine();
        benchmark.start();
        Optional<Transaction> transaction = transactionRepository.getTransactionByOriginName(
                targetOriginName,
                collectionType
        );
        displayTargetTransaction(transaction, targetOriginName);
        benchmark.stop();
    }

    private void displayTargetTransaction(Optional<Transaction> transaction, String targetOriginName) {
        lookupPresenter.printBlankLine();
        if (transaction.isEmpty()) {
            lookupPresenter.printNotFound(targetOriginName);
        }
        lookupPresenter.printTransaction(transaction);
    }
}
