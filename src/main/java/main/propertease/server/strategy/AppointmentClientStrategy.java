package main.propertease.server.strategy;

import main.propertease.server.mediator.AbstractClientManager;
import org.json.JSONException;
import org.json.JSONObject;

public class AppointmentClientStrategy implements ClientManagerStrategy {
    public AppointmentClientStrategy(AbstractClientManager clientManager) {
        this.clientManager = clientManager;
    }

    @Override
    public boolean communicate() {
        final var line = clientManager.readLine();
        if (line == null) {
            clientManager.close();
            return false;
        }
        final var message = new JSONObject(line);
        try {
            final var request = message.getString("request");
            clientManager.writeLine("{\"respose\":\"test\"}");
        } catch (JSONException e) {
            clientManager.writeLine(clientManager.makeErrorMessage(e.getMessage()));
        }
        return true;
    }

    private final AbstractClientManager clientManager;
}
