package main.propertease.server.proxy;

import java.sql.ResultSet;
import java.util.Optional;

public interface DatabaseConnection {
    public <T extends Iterable<Object>> ResultSet execute(String query, Optional<T> values);
}
