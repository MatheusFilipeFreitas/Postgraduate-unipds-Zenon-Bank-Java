package br.com.zenon.service.implementation;

import br.com.zenon.constants.Limits;
import br.com.zenon.io.IMapper;
import br.com.zenon.io.IReader;
import br.com.zenon.io.implementation.AbstractReader;
import br.com.zenon.io.implementation.TransactionMapper;
import br.com.zenon.models.Transaction;
import br.com.zenon.presentation.implementation.IngestPresenter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TransactionIngestor {
    private final IReader<Transaction> reader = new AbstractReader<>();
    private final IMapper<Transaction> mapper = new TransactionMapper();
    private final IngestPresenter ingestPresenter = new IngestPresenter();
    private List<Transaction> transactions = new ArrayList<>();

    public TransactionIngestor(String fileName, boolean isLazy, Consumer<List<Transaction>> consumer) throws Exception {
        int limit = Limits.FILE_RECORDS_EXTRACTOR;
        ingestPresenter.printIngestLimit(limit);
        if (isLazy) {
            reader.readLazy(mapper, fileName, consumer);
        } else {
            transactions = List.copyOf(reader.read(mapper, fileName, limit));
        }
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}
