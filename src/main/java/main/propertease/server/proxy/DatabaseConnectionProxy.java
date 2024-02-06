package main.propertease.server.proxy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.function.Consumer;

public class DatabaseConnectionProxy implements DatabaseConnection {

    @Override
    public void executeUpdate(String query, Optional<? extends Iterable<Object>> values) throws SQLException {
        final var instance = ensureConnection();
        instance.executeUpdate(query, values);
    }

    @Override
    public void executeQuery(String query, Optional<? extends Iterable<Object>> values, Consumer<ResultSet> onCompletion) {
        final var instance = ensureConnection();
        instance.executeQuery(query, values, onCompletion);
    }

    private static DatabaseConnection ensureConnection() {
        if (instance == null) {
            final var root = System.getProperty("user.dir");
            final var path = String.format("%s/src/main/resources/main/propertease/server/database/propertease.sqlite", root);
            instance = new DatabaseConnectionImplementation(path);
        }
        return instance;
    }

    private static DatabaseConnection instance = null;
}
