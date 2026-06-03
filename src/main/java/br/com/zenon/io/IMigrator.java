package br.com.zenon.io;

import java.sql.SQLException;
import java.util.List;

public interface IMigrator<T> {
    void migrate(List<T> data) throws SQLException;
}
