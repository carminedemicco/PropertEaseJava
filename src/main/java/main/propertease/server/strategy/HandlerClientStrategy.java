package main.propertease.server.strategy;

import main.propertease.server.Client;
import main.propertease.server.mediator.AbstractClientManager;
import main.propertease.server.proxy.DatabaseConnectionProxy;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.*;

public class HandlerClientStrategy implements ClientManagerStrategy {
    public HandlerClientStrategy(AbstractClientManager clientManager) {
        this.clientManager = clientManager;
        secondaryServers = new ArrayList<>(SECONDARY_SERVER_COUNT);
        secondaryServers.add(POSTER_SERVER_INDEX, new Client("localhost", 1927));
        secondaryServers.add(APPOINTMENT_SERVER_INDEX, new Client("localhost", 1928));
    }

    @Override
    public boolean communicate() {
        final var line = clientManager.readLine();
        if (line == null) {
            for (var server : secondaryServers) {
                server.close();
            }
            clientManager.close();
            return false;
        }
        final var message = new JSONObject(line);
        try {
            final var type = message.getString("type");
            final var data = message.getJSONObject("data");
            var server = (Client)null;
            switch (type) {
                case "appointment":
                    server = secondaryServers.get(APPOINTMENT_SERVER_INDEX);
                    break;

                case "poster":
                    server = secondaryServers.get(POSTER_SERVER_INDEX);
                    break;

                case "generic": {
                    // TODO: capire se a Di Nardo gli va bene questa cosa o è necessario un altro server
                    handleGenericRequest(data);
                    return true;
                }

                default:
                    clientManager.writeLine(clientManager.makeErrorMessage("unknown_request: " + type));
                    return true;
            }
            clientManager.writeLine(
                Objects.requireNonNull(server)
                    .exchange(data)
                    .toString()
            );
        } catch (JSONException e) {
            clientManager.writeLine(clientManager.makeErrorMessage(e.getMessage()));
        }
        return true;
    }

    private void handleGenericRequest(JSONObject data) {
        final var request = data.getString("request");
        switch (request) {
            case "signin": {
                final var parameters = data.getJSONObject("parameters");
                final var username = parameters.getString("username");
                final var password = parameters.getString("password");
                final var connection = new DatabaseConnectionProxy();
                final var query = "select * from User where username = ? and password = ?";
                final var queryData = Arrays.<Object>asList(username, password);
                connection.executeQuery(query, Optional.of(queryData), (result) -> {
                    final var response = new JSONObject();
                    try {
                        if (result.next()) {
                            final var user = new JSONObject();
                            user.put("username", result.getString("username"));
                            user.put("password", result.getString("password"));
                            user.put("first_name", result.getString("first_name"));
                            user.put("last_name", result.getString("last_name"));
                            user.put("privileges", result.getInt("privileges"));
                            response.put("response", user);
                        } else {
                            response.put("response", JSONObject.NULL);
                        }
                        clientManager.writeLine(response.toString());
                    } catch (SQLException e) {
                        clientManager.writeLine(clientManager.makeErrorMessage(e.getMessage()));
                    }
                });
            } break;

            case "signup": {
                final var parameters = data.getJSONObject("parameters");
                final var username = parameters.getString("username");
                final var password = parameters.getString("password");
                final var firstName = parameters.getString("first_name");
                final var lastName = parameters.getString("last_name");
                final var connection = new DatabaseConnectionProxy();
                final var query = "insert into User values (?, ?, ?, ?, 0)";
                final var queryData = Arrays.<Object>asList(
                    username,
                    password,
                    firstName,
                    lastName
                );
                final var response = new JSONObject();
                try {
                    connection.executeUpdate(query, Optional.of(queryData));
                    response.put("response", new JSONObject());
                } catch (Exception e) {
                    response.put("response", JSONObject.NULL);
                }
                clientManager.writeLine(response.toString());
            } break;
        }
    }

    private final int POSTER_SERVER_INDEX = 0;
    private final int APPOINTMENT_SERVER_INDEX = 1;
    private final int SECONDARY_SERVER_COUNT = 2;

    private final List<Client> secondaryServers;
    private final AbstractClientManager clientManager;

}
