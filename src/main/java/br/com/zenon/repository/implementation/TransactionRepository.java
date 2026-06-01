package br.com.zenon.repository.implementation;

import br.com.zenon.constants.type.CollectionType;
import br.com.zenon.models.Transaction;
import br.com.zenon.repository.ITransactionRepository;

import java.util.*;

public class TransactionRepository implements ITransactionRepository {
    private List<Transaction> transactionList = new ArrayList<>();
    private Map<String, Transaction> transactionMap = new HashMap<>();

    public TransactionRepository(List<Transaction> transactions) {
        this.transactionList = new ArrayList<>(transactions);
        convertListIntoMap(this.transactionList);
    }

    @Override
    public Optional<Transaction> getTransactionByOriginName(String originName, CollectionType collectionType) {
        return switch (collectionType) {
            case LIST -> getTransactionByOriginNameInList(originName);
            case MAP -> getTransactionByOriginNameInMap(originName);
            default -> Optional.empty();
        };
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return List.copyOf(transactionList);
    }

    private Optional<Transaction> getTransactionByOriginNameInMap(String originName) {
        return Optional.ofNullable(this.transactionMap.get(originName));
    }

    private Optional<Transaction> getTransactionByOriginNameInList(String originName) {
        return this.transactionList
                .stream()
                .filter(transaction -> transaction.origin().name().equals(originName))
                .findFirst();
    }

    private void convertListIntoMap(List<Transaction> transactionList) {
        transactionList.forEach(transaction -> {
            transactionMap.put(transaction.origin().name(), transaction);
        });
    }
}
