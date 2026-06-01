package br.com.zenon.service.implementation;

import br.com.zenon.constants.Limits;
import br.com.zenon.io.IMapper;
import br.com.zenon.io.IReader;
import br.com.zenon.io.implementation.AbstractReader;
import br.com.zenon.io.implementation.TransactionMapper;
import br.com.zenon.models.Transaction;
import br.com.zenon.presentation.IngestPresenter;

import java.io.IOException;
import java.util.List;

public class TransactionIngestor {
    private final IReader<Transaction> reader = new AbstractReader<>();
    private final IMapper<Transaction> mapper = new TransactionMapper();
    private final IngestPresenter ingestPresenter = new IngestPresenter();
    private final List<Transaction> transactions;

    public TransactionIngestor(String fileName) throws IOException {
        int limit = Limits.FILE_RECORDS_EXTRACTOR;
        ingestPresenter.printIngestLimit(limit);
        transactions = List.copyOf(reader.read(mapper, fileName, limit));
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}
