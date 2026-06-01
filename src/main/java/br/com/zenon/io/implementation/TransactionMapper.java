package br.com.zenon.io.implementation;

import br.com.zenon.io.IMapper;
import br.com.zenon.models.Transaction;

import java.util.HashMap;
import java.util.Map;

public class TransactionMapper implements IMapper<Transaction> {
    private TransactionFactory transactionFactory;

    @Override
    public void initializeHeaders(String[] headers) {
        Map<String, Integer> columnIndexMap = new HashMap<>(headers.length);
        for (int i = 0; i < headers.length; i++) {
            columnIndexMap.put(headers[i], i);
        }
        this.transactionFactory = new TransactionFactory(columnIndexMap);
    }

    @Override
    public Transaction parse(String[] values) throws Exception {
        return transactionFactory.create(values);
    }
}
