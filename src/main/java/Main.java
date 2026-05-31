import br.com.zenon.constants.FilePath;

import br.com.zenon.models.Transaction;
import br.com.zenon.utils.implementations.FraudAnalyzer;
import br.com.zenon.utils.implementations.TransactionIngestor;

void main() throws IOException {
    FraudAnalyzer analyzer = new FraudAnalyzer();
    TransactionIngestor ingestor = new TransactionIngestor(FilePath.CSV_FILE_PATH);
    List<Transaction> transactions = ingestor.getTransactions();
    IO.println();
    transactions.forEach(IO::println);
    IO.println();
    analyzer.analyze(transactions);
}
