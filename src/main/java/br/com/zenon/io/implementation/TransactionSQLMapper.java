package br.com.zenon.io.implementation;

import br.com.zenon.io.ISQLMapper;
import br.com.zenon.models.FraudDemark;
import br.com.zenon.models.Origin;
import br.com.zenon.models.Transaction;
import br.com.zenon.models.types.TransactionType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionSQLMapper implements ISQLMapper<Transaction> {
    @Override
    public void fillPreparedStatement(PreparedStatement preparedStatement, Transaction data) throws SQLException {
        preparedStatement.setInt(1, data.step());
        preparedStatement.setString(2, data.type().name());
        preparedStatement.setBigDecimal(3, data.amount());
        preparedStatement.setString(4, data.origin().name());
        preparedStatement.setBigDecimal(5, data.origin().oldBalance());
        preparedStatement.setBigDecimal(6, data.origin().newBalance());
        preparedStatement.setString(7, data.destinatary().name());
        preparedStatement.setBigDecimal(8, data.destinatary().oldBalance());
        preparedStatement.setBigDecimal(9, data.destinatary().newBalance());
        preparedStatement.setBoolean(10, data.fraudDemark().isFraud());
        preparedStatement.setBoolean(11, data.fraudDemark().isFlagged());
    }

    @Override
    public List<Transaction> mapFromResultSet(ResultSet resultSet) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        while (resultSet.next()) {
            transactions.add(
                new Transaction(
                    resultSet.getInt("step"),
                    TransactionType.valueOf(resultSet.getString("type")),
                    resultSet.getBigDecimal("amount"),
                    new Origin(
                        resultSet.getString("name_orig"),
                        resultSet.getBigDecimal("old_balance_orig"),
                        resultSet.getBigDecimal("new_balance_orig")
                    ),
                    new Origin(
                        resultSet.getString("name_dest"),
                        resultSet.getBigDecimal("old_balance_dest"),
                        resultSet.getBigDecimal("new_balance_dest")
                    ),
                    new FraudDemark(
                        resultSet.getBoolean("is_fraud"),
                        resultSet.getBoolean("is_flagged_fraud")
                    )
                )
            );
        }
        return transactions;
    }
}
