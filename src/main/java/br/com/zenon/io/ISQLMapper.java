package br.com.zenon.io;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface ISQLMapper<T> {
    void fillPreparedStatement(PreparedStatement preparedStatement, T data) throws SQLException;
    List<T> mapFromResultSet(ResultSet resultSet) throws SQLException;
}
