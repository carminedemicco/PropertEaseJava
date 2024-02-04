package main.propertease.server.proxy;

import java.sql.*;
import java.util.Optional;

public class DatabaseConnectionImplementation implements DatabaseConnection {
    public DatabaseConnectionImplementation(String host) {
        try {
            connection = DriverManager.getConnection(String.format("jdbc:sqlite:%s", host));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T extends Iterable<Object>> ResultSet execute(String query, Optional<T> values) {
        try {
            final var preparedStatement = connection.prepareStatement(query);
            if (values.isPresent()) {
                var index = 1;
                for (final var value : values.get()) {
                    preparedStatement.setObject(index, value);
                    index++;
                }
            }
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Connection connection;
}
