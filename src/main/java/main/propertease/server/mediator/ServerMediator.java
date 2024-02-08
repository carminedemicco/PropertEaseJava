package main.propertease.server.mediator;

import main.propertease.server.strategy.ClientManagerStrategy;

public interface ServerMediator {
    public boolean notify(ClientManagerStrategy sender, String line);
}
