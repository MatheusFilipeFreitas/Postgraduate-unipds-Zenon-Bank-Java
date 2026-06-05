package br.com.zenon.repository.utils.implementation;

import br.com.zenon.constants.DatabaseConstant;
import br.com.zenon.constants.type.DatabaseActionType;
import br.com.zenon.io.ISQLMapper;
import br.com.zenon.repository.utils.IDatabase;
import org.postgresql.core.ConnectionFactory;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class Database<T> implements IDatabase<T> {
    private final Connection connection;
    private final ISQLMapper<T> mapper;
    private static Database instance;

    public Database(ISQLMapper<T> mapper) {
        try {
            this.connection = DriverManager.getConnection(
                    DatabaseConstant.JDBC_URL,
                    DatabaseConstant.JDBC_USER,
                    DatabaseConstant.JDBC_PASSWORD
            );
            this.connection.setAutoCommit(false);
            this.mapper = mapper;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Database getInstance(ISQLMapper mapper) {
        if (instance == null) {
            instance = new Database(mapper);
        }
        return instance;
    }

    @Override
    public boolean insert(String query, T data) throws SQLException {
        Optional<T> result = Optional.ofNullable(executeQuery(query, DatabaseActionType.INSERT, List.of(data), null).getFirst());
        return result.isPresent();
    }

    @Override
    public boolean insertAll(String query, List<T> data) throws SQLException {
        List<T> result = executeQuery(query, DatabaseActionType.INSERT, data, null);
        return !(result.isEmpty());
    }


    @Override
    public Optional<T> get(String query, String[] params) throws SQLException {
        return Optional.ofNullable(executeQuery(query, DatabaseActionType.GET, null, params).getFirst());
    }

    @Override
    public List<T> getAll(String query) throws SQLException {
        return executeQuery(query, DatabaseActionType.GET, null, null);
    }

    private List<T> executeQuery(String query, DatabaseActionType action, List<T> data, String[] params) throws SQLException {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(query);
            switch (action) {
                case INSERT:
                    int count = 0;
                    if (data == null) {
                        throw new SQLException("data in execution query to database cannot be null");
                    }
                    for (T dataItem : data) {
                        mapper.fillPreparedStatement(preparedStatement, dataItem);
                        count++;
                        executeQueryInBatch(preparedStatement, count, data.size());
                    }
                    return data;

                case GET:
                    if (params != null) {
                        for (int i = 1; i <= params.length; i++) {
                            preparedStatement.setString(i, params[i]);
                        }
                    }
                    ResultSet result = preparedStatement.executeQuery();
                    return mapper.mapFromResultSet(result);

                default:
                    preparedStatement.close();
                    throw new SQLException("No mapped action in database query execution to: " + action.toString());
            }
        } catch (Exception ex) {
            this.connection.rollback();
            throw new RuntimeException(ex);
        }
    }

    private void executeQueryInBatch(PreparedStatement preparedStatement, int count, int dataSize) throws SQLException {
        preparedStatement.addBatch();

        if (dataSize >= DatabaseConstant.BATCH_SIZE) {
            if (count % DatabaseConstant.BATCH_SIZE == 0) {
                handleBatchExecution(preparedStatement);
            }
        } else {
            if (count == dataSize) {
                handleBatchExecution(preparedStatement);
            }
        }
    }

    private void handleBatchExecution(PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.executeBatch();
        this.connection.commit();
    }
}
