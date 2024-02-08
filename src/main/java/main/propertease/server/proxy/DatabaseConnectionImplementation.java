package main.propertease.server.proxy;

import java.sql.*;
import java.util.Optional;
import java.util.function.Consumer;

public class DatabaseConnectionImplementation implements DatabaseConnection {
    public DatabaseConnectionImplementation(String host) {
        try {
            connection = DriverManager.getConnection(String.format("jdbc:sqlite:%s", host));
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void executeUpdate(String query, Iterable<?> values) throws SQLException {
        try (final var statement = prepareStatement(query, values)) {
            statement.executeUpdate();
        }
    }

    @Override
    public void executeQuery(String query, Iterable<?> values, Consumer<ResultSet> onCompletion) {
        try (final var statement = prepareStatement(query, values)) {
            onCompletion.accept(statement.executeQuery());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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
