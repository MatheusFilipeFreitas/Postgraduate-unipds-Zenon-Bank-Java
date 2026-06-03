package br.com.zenon.repository.utils;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface IDatabase<T> {
    boolean insert(String query, T t) throws SQLException;
    Optional<T> get(String query, String[] params) throws SQLException;
    List<T> getAll(String query) throws SQLException;
}
