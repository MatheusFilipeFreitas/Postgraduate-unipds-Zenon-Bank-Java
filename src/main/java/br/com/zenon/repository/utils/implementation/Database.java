package br.com.zenon.repository.utils.implementation;

import br.com.zenon.constants.DatabaseConstant;
import br.com.zenon.constants.type.DatabaseActionType;
import br.com.zenon.io.ISQLMapper;
import br.com.zenon.repository.utils.IDatabase;

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
        Optional<T> result = Optional.ofNullable(executeQuery(query, DatabaseActionType.INSERT, data, null).getFirst());
        return result.isPresent();
    }

    @Override
    public Optional<T> get(String query, String[] params) throws SQLException {
        return Optional.ofNullable(executeQuery(query, DatabaseActionType.GET, null, params).getFirst());
    }

    @Override
    public List<T> getAll(String query) throws SQLException {
        return executeQuery(query, DatabaseActionType.GET, null, null);
    }

    private List<T> executeQuery(String query, DatabaseActionType action, T data, String[] params) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        switch (action) {
            case INSERT:
                if (data == null) {
                    throw new SQLException("data in execution query to database cannot be null");
                }
                mapper.fillPreparedStatement(preparedStatement, data);
                preparedStatement.execute();
            return List.of(data);

            case GET:
                if (params != null) {
                    for (int i = 1; i <= params.length; i++) {
                        preparedStatement.setString(i, params[i]);
                    }
                }
                ResultSet result = preparedStatement.executeQuery();
            return mapper.mapFromResultSet(result);

            default:
                throw new SQLException("No mapped action in database query execution to: " + action.toString());
        }
    }
}
