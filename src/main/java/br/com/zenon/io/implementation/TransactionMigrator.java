package br.com.zenon.io.implementation;

import br.com.zenon.io.IMigrator;
import br.com.zenon.models.Transaction;
import br.com.zenon.repository.queries.TransactionQuery;
import br.com.zenon.repository.utils.implementation.Database;

import java.sql.SQLException;
import java.util.List;

public class TransactionMigrator implements IMigrator<Transaction> {
    private Database<Transaction> database;

    public TransactionMigrator() {
        database = Database.getInstance(new TransactionSQLMapper());
    }

    @Override
    public void migrate(List<Transaction> data) throws SQLException {
        for (Transaction transaction : data) {
            database.insert(TransactionQuery.INSERT, transaction);
        }
    }
}
