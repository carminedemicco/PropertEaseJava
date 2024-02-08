package main.propertease.server.mediator;

import main.propertease.server.Client;
import main.propertease.server.ClientManager;
import main.propertease.server.strategy.ClientManagerStrategy;
import main.propertease.server.strategy.HandlerClientStrategy;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

class ServerType {
    public static final int POSTER = 0;
    public static final int APPOINTMENT = 1;
    public static final int COUNT = 2;
}

public class ConcreteServerMediator implements ServerMediator {
    public ConcreteServerMediator(ClientManager clientManager) {
        this.clientManager = clientManager;
        secondaryServers = new ArrayList<>(ServerType.COUNT);
        secondaryServers.add(ServerType.POSTER, new Client("localhost", 1927));
        secondaryServers.add(ServerType.APPOINTMENT, new Client("localhost", 1928));
    }

    @Override
    public boolean notify(ClientManagerStrategy sender, String line) {
        if (sender instanceof HandlerClientStrategy) {
            if (line == null) {
                for (final var server : secondaryServers) {
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
                        server = secondaryServers.get(ServerType.APPOINTMENT);
                        break;

                    case "poster":
                        server = secondaryServers.get(ServerType.POSTER);
                        break;

                    case "generic": {
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
        return true;
    }

    private void handleGenericRequest(JSONObject data) {
        final var request = data.getString("request");
        final var database = clientManager.getDatabase();
        switch (request) {
            case "signin": {
                final var parameters = data.getJSONObject("parameters");
                final var username = parameters.getString("username");
                final var password = parameters.getString("password");
                final var query = "select * from User where username = ? and password = ?";
                final var queryData = Arrays.<Object>asList(username, password);
                database.executeQuery(query, queryData, (result) -> {
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
                break;
            }

            case "signup": {
                final var parameters = data.getJSONObject("parameters");
                final var username = parameters.getString("username");
                final var password = parameters.getString("password");
                final var firstName = parameters.getString("first_name");
                final var lastName = parameters.getString("last_name");
                final var query = "insert into User values (?, ?, ?, ?, 0)";
                final var queryData = Arrays.<Object>asList(
                    username,
                    password,
                    firstName,
                    lastName
                );
                final var response = new JSONObject();
                try {
                    database.executeUpdate(query, queryData);
                    response.put("response", new JSONObject());
                } catch (Exception e) {
                    response.put("response", JSONObject.NULL);
                } finally {
                    clientManager.writeLine(response.toString());
                }
                break;
            }
        }
    }

    private final ClientManager clientManager;
    private final List<Client> secondaryServers;
}
