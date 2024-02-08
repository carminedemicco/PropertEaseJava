package main.propertease.server.proxy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

public interface DatabaseConnection {
    public void executeUpdate(String query, Iterable<?> values) throws SQLException;

    public void executeQuery(String query, Iterable<?> values, Consumer<ResultSet> onCompletion);
}
