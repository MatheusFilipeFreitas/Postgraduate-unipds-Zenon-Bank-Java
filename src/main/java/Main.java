import br.com.zenon.constants.FilePath;

import br.com.zenon.constants.type.CollectionType;
import br.com.zenon.models.Transaction;
import br.com.zenon.repository.implementation.TransactionRepository;
import br.com.zenon.utils.implementations.Benchmark;
import br.com.zenon.utils.implementations.FraudAnalyzer;
import br.com.zenon.utils.implementations.TransactionIngestor;

private TransactionRepository transactionRepository;
private FraudAnalyzer analyzer = new FraudAnalyzer();
private Benchmark benchmark = new Benchmark();

void main() throws IOException {
    initApp();
    displayDashboardAndAllTransactions();
    displayBenchmark();
}

void initApp() throws IOException {
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

private void displayBenchmark() {
    String targetOriginName = getOriginTargetName();
    handleBenchmarkByType(CollectionType.LIST, targetOriginName);
    handleBenchmarkByType(CollectionType.MAP, targetOriginName);
}

private String getOriginTargetName() {
    IO.println();
    IO.println("Target Name: ");
    return IO.readln();
}


private void handleBenchmarkByType(CollectionType collectionType, String targetOriginName) {
    IO.println();
    benchmark.start();
    Optional<Transaction> transaction2 = transactionRepository.getTransactionByOriginName(targetOriginName, collectionType);
    displayTargetTransaction(transaction2, targetOriginName);
    benchmark.stop();
}

private void displayTargetTransaction(Optional<Transaction> transaction, String targetOriginName) {
    IO.println();
    if (transaction.isEmpty()) {
        IO.println("Could not find transaction with origin name: " + targetOriginName);
    }
    transaction.ifPresent(IO::println);
}
