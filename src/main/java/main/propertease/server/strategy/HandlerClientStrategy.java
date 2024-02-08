package main.propertease.server.strategy;

import main.propertease.server.Client;
import main.propertease.server.ClientManager;
import main.propertease.server.mediator.ConcreteServerMediator;
import main.propertease.server.mediator.ServerMediator;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.*;

public class HandlerClientStrategy implements ClientManagerStrategy {
    public HandlerClientStrategy(ClientManager clientManager) {
        this.clientManager = clientManager;
        mediator = new ConcreteServerMediator(clientManager);
    }

    @Override
    public boolean communicate() {
        return mediator.notify(this, clientManager.readLine());
    }

    private final ClientManager clientManager;
    private final ServerMediator mediator;
}
