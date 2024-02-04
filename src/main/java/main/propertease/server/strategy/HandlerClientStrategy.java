package main.propertease.server.strategy;

import main.propertease.server.Client;
import main.propertease.server.mediator.AbstractClientManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HandlerClientStrategy implements ClientManagerStrategy {
    public HandlerClientStrategy(AbstractClientManager clientManager) {
        this.clientManager = clientManager;
        secondaryServers = new ArrayList<>(SECONDARY_SERVER_COUNT);
        secondaryServers.add(POSTER_SERVER_INDEX, new Client("localhost", 1927));
        secondaryServers.add(APPOINTMENT_SERVER_INDEX, new Client("localhost", 1928));
    }

    @Override
    public void communicate() {
        final var line = clientManager.readLine();
        if (line == null) {
            clientManager.writeLine(clientManager.makeErrorMessage("invalid_input"));
            for (var server : secondaryServers) {
                server.close();
            }
            clientManager.close();
            return;
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

                default:
                    clientManager.writeLine(clientManager.makeErrorMessage("unknown_request: " + type));
                    break;
            }
            clientManager.writeLine(
                Objects.requireNonNull(server)
                    .exchange(data)
                    .toString()
            );
        } catch (JSONException e) {
            clientManager.writeLine(clientManager.makeErrorMessage(e.getMessage()));
        }
    }

    private final int POSTER_SERVER_INDEX = 0;
    private final int APPOINTMENT_SERVER_INDEX = 1;
    private final int SECONDARY_SERVER_COUNT = 2;

    private final List<Client> secondaryServers;
    private final AbstractClientManager clientManager;

}
