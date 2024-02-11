package main.propertease.server.proxy;

import java.sql.*;
import java.util.function.Consumer;

/**
 * Implementation of the DatabaseConnection interface
 */
public class DatabaseConnectionImplementation implements DatabaseConnection {
    /**
     * Constructor for the DatabaseConnectionImplementation
     *
     * @param host The host to connect to
     */
    public DatabaseConnectionImplementation(String host) {
        try {
            connection = DriverManager.getConnection(String.format("jdbc:sqlite:%s", host));
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Executes a DML query (e.g. INSERT, UPDATE, DELETE) with the given arguments
     *
     * @param query  The query to execute
     * @param values The values to use in the query
     * @throws SQLException If an error occurs while executing the query (e.g. integrity violation, ...)
     */
    @Override
    public void executeUpdate(String query, Iterable<?> values) throws SQLException {
        try (final var statement = prepareStatement(query, values)) {
            statement.executeUpdate();
        }
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
        try (final var statement = prepareStatement(query, values)) {
            onCompletion.accept(statement.executeQuery());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prepare a statement with the given query and values
     *
     * @param query  The query to prepare
     * @param values The values to use in the query
     * @return The prepared statement
     * @throws SQLException If an error occurs while preparing the statement
     */
    private PreparedStatement prepareStatement(String query, Iterable<?> values) throws SQLException {
        final var preparedStatement = connection.prepareStatement(query);
        if (values != null) {
            var index = 1;
            for (final var value : values) {
                preparedStatement.setObject(index, value);
                index++;
            }
        }
        return preparedStatement;
    }

    private Connection connection;
}
