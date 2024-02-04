package main.propertease.server.proxy;

import java.sql.ResultSet;
import java.util.Optional;

public interface DatabaseConnection {
    public ResultSet execute(String query, Optional<Iterable<Object>> values);
}
