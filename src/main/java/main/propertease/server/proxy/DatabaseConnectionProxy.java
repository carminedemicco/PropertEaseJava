package main.propertease.server.proxy;

import java.sql.ResultSet;
import java.util.Objects;
import java.util.Optional;

public class DatabaseConnectionProxy implements DatabaseConnection {
    @Override
    public <T extends Iterable<Object>> ResultSet execute(String query, Optional<T> values) {
        final var instance = ensureConnection();
        return instance.execute(query, values);
    }

    private static DatabaseConnection ensureConnection() {
        if (instance == null) {
            final var loader = DatabaseConnectionProxy.class.getClassLoader();
            final var path = Objects.requireNonNull(loader.getResource("database/propertease.sqlite")).getPath();
            instance = new DatabaseConnectionImplementation(path);
        }
        return instance;
    }

    private static DatabaseConnection instance = null;
}
