package br.com.zenon.repository.implementation;

import br.com.zenon.constants.type.CollectionType;
import br.com.zenon.io.implementation.TransactionSQLMapper;
import br.com.zenon.models.Transaction;
import br.com.zenon.repository.ITransactionRepository;
import br.com.zenon.repository.queries.TransactionQuery;
import br.com.zenon.repository.utils.implementation.Database;

import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TransactionRepository implements ITransactionRepository {
    private List<Transaction> transactionList = new ArrayList<>();
    private Map<String, Transaction> transactionMap = new HashMap<>();
    private Database<Transaction> database;
    private final Logger logger = Logger.getLogger(String.valueOf(TransactionRepository.class));

    public TransactionRepository(List<Transaction> transactions) {
        this.transactionList = new ArrayList<>(transactions);
        convertListIntoMap(this.transactionList);
    }

    public TransactionRepository() {
        initDatabase();
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
    public Optional<Transaction> getTransactionByOriginName(String originName) {
        try {
            return database.get(TransactionQuery.FIND_BY_ORIGIN, new String[]{originName});
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return List.copyOf(transactionList);
    }

    @Override
    public boolean insert(Transaction transaction) {
        try
        {
            return this.database.insert(TransactionQuery.INSERT, transaction);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean insertAll(List<Transaction> transactions) {
        try {
            this.logger.log(Level.INFO, "TransactionRepository insertAll");
            boolean result = this.database.insertAll(TransactionQuery.INSERT, transactions);
            this.logger.log(Level.INFO, "TransactionRepository insertAll result: " + result);
            return result;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

    private void initDatabase() {
        this.database = Database.getInstance(new TransactionSQLMapper());
    }
}
