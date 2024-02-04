package main.propertease.server.proxy;

import java.sql.ResultSet;
import java.util.Objects;
import java.util.Optional;

public class DatabaseConnectionProxy implements DatabaseConnection {
    @Override
    public ResultSet execute(String query, Optional<Iterable<Object>> values) {
        final var instance = ensureConnection();
        return instance.execute(query, values);
    }

    private static DatabaseConnection ensureConnection() {
        if (instance == null) {
            final var resource = DatabaseConnectionProxy.class.getResource("/main/propertease/server/database/propertease.sqlite");
            final var path = Objects.requireNonNull(resource).getPath();
            instance = new DatabaseConnectionImplementation(path);
        }
        return instance;
    }

    private static DatabaseConnection instance = null;
}
