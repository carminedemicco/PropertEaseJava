package main.propertease.server.strategy;

import main.propertease.server.Client;
import main.propertease.server.ClientManager;
import main.propertease.server.proxy.DatabaseConnection;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Strategy for the handler server, which is responsible for forwarding requests to the secondary server if necessary
 * or handling them directly.
 */
public class HandlerClientStrategy implements ClientManagerStrategy {
    /**
     * Creates a new handler client strategy.
     *
     * @param clientManager The client manager to use.
     */
    public HandlerClientStrategy(ClientManager clientManager) {
        this.clientManager = clientManager;
        secondaryServers = new ArrayList<>(ServerType.COUNT);
        secondaryServers.add(ServerType.POSTER, new Client("localhost", 1927));
        secondaryServers.add(ServerType.APPOINTMENT, new Client("localhost", 1928));
    }

    /**
     * Implements client/server communication.
     *
     * @return <b>true</b> if the client should continue to be managed, <b>false</b> otherwise.
     */
    @Override
    public boolean communicate() {
        final var line = clientManager.readLine();
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

                case "generic":
                    handleGenericRequest(data);
                    return true;

                default:
                    clientManager.writeLine(clientManager.makeErrorMessage("unknown_request: " + type));
                    return true;
            }
            clientManager.writeLine(
                server
                    .exchange(data)
                    .toString()
            );
        } catch (JSONException e) {
            clientManager.writeLine(clientManager.makeErrorMessage(e.getMessage()));
        }
        return true;
    }

    /**
     * Handles a generic request.
     *
     * @param data The message containing the request.
     */
    private void handleGenericRequest(JSONObject data) {
        final var request = data.getString("request");
        final var database = clientManager.getDatabase();
        switch (request) {
            case "signin":
                handleSignInRequest(database, data);
                break;

            case "signup":
                handleSignUpRequest(database, data);
                break;
        }
    }

    /**
     * Handles a sign-in request.
     *
     * @param database The database connection handle.
     * @param data     The message containing sign in info regarding the request.
     */
    private void handleSignInRequest(DatabaseConnection database, JSONObject data) {
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
    }

    /**
     * Handles a sign-up request.
     *
     * @param database The database connection handle.
     * @param data     The message containing sign up info regarding the request.
     */
    private void handleSignUpRequest(DatabaseConnection database, JSONObject data) {
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
    }

    private final ClientManager clientManager;
    private final List<Client> secondaryServers;
}

/**
 * Static class containing server types, their respective IDs and the total number of server types.
 */
class ServerType {
    public static final int POSTER = 0;
    public static final int APPOINTMENT = 1;
    public static final int COUNT = 2;
}
