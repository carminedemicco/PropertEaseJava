package main.propertease.server.proxy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

/**
 * Proxy to a database connection.
 */
public class DatabaseConnectionProxy implements DatabaseConnection {
    /**
     * Executes a DML query (e.g. INSERT, UPDATE, DELETE) with the given arguments
     *
     * @param query  The query to execute
     * @param values The values to use in the query
     * @throws SQLException If an error occurs while executing the query (e.g. integrity violation, ...)
     */
    @Override
    public void executeUpdate(String query, Iterable<?> values) throws SQLException {
        final var instance = ensureConnection();
        instance.executeUpdate(query, values);
    }

    /**
     * Execute a QL query (e.g. SELECT) with the given arguments
     *
     * @param query        The query to execute
     * @param values       The values to use in the query
     * @param onCompletion The callback to execute when the query is completed, this is needed because ResultSet
     *                     is AutoCloseable and should be closed after the query is completed
     */
    @Override
    public void executeQuery(String query, Iterable<?> values, Consumer<ResultSet> onCompletion) {
        final var instance = ensureConnection();
        instance.executeQuery(query, values, onCompletion);
    }

    /**
     * Get the instance of the database connection and performs the connection to the database lazily
     *
     * @return The instance of the database connection
     */
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
