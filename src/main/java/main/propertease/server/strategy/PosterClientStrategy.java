package main.propertease.server.strategy;

import main.propertease.server.mediator.AbstractClientManager;
import org.json.JSONException;
import org.json.JSONObject;

public class PosterClientStrategy implements ClientManagerStrategy {
    public PosterClientStrategy(AbstractClientManager clientManager) {
        this.clientManager = clientManager;
    }

    @Override
    public void communicate() {
        final var line = clientManager.readLine();
        if (line == null) {
            clientManager.writeLine(clientManager.makeErrorMessage("invalid_input"));
            clientManager.close();
            return;
        }
        final var message = new JSONObject(line);
        try {
            final var request = message.getString("request");
            clientManager.writeLine("{\"respose\":\"test\"}");
        } catch (JSONException e) {
            clientManager.writeLine(clientManager.makeErrorMessage(e.getMessage()));
        }
    }

    private final AbstractClientManager clientManager;
}
