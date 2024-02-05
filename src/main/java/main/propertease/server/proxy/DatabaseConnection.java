package main.propertease.server.proxy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.function.Consumer;

public interface DatabaseConnection {
    public void executeUpdate(
        String query,
        Optional<? extends Iterable<Object>> values
    ) throws SQLException;

    public void executeQuery(
        String query,
        Optional<? extends Iterable<Object>> values,
        Consumer<ResultSet> onCompletion
    );
}
