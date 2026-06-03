package br.com.zenon.repository;

import br.com.zenon.constants.type.CollectionType;
import br.com.zenon.models.Transaction;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ITransactionRepository {
    Optional<Transaction> getTransactionByOriginName(String originName, CollectionType collectionType);
    Optional<Transaction> getTransactionByOriginName(String originName);
    List<Transaction> getAllTransactions();
    boolean insert(Transaction transaction);
}
