package br.com.zenon.app;

import br.com.zenon.constants.FilePath;
import br.com.zenon.repository.implementation.TransactionRepository;
import br.com.zenon.service.implementation.TransactionIngestor;

public class ZenonMigration {
    private final TransactionRepository transactionRepository = new TransactionRepository();
    private TransactionIngestor transactionIngestor;

    public void run() throws Exception {
        long startTime = System.currentTimeMillis();
        initIngestor();
        long endTime = System.currentTimeMillis();
        IO.println("Migration finished in " + String.valueOf(endTime - startTime) + " ms");
    }

    private void initIngestor() throws Exception {
        transactionIngestor = new TransactionIngestor(FilePath.CSV_FILE_PATH, true, transactionRepository::insertAll);
    }
}
