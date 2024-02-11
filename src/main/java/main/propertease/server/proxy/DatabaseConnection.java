package main.propertease.server.proxy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

/**
 * Interface for a proxy to a database connection.
 */
public interface DatabaseConnection {
    /**
     * Executes a DML query (e.g. INSERT, UPDATE, DELETE) with the given arguments
     *
     * @param query  The query to execute
     * @param values The values to use in the query
     * @throws SQLException If an error occurs while executing the query (e.g. integrity violation, ...)
     */
    public void executeUpdate(String query, Iterable<?> values) throws SQLException;

    /**
     * Execute a QL query (e.g. SELECT) with the given arguments
     *
     * @param query        The query to execute
     * @param values       The values to use in the query
     * @param onCompletion The callback to execute when the query is completed, this is needed because ResultSet
     *                     is AutoCloseable and should be closed after the query is completed
     */
    public void executeQuery(String query, Iterable<?> values, Consumer<ResultSet> onCompletion);
}
