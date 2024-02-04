package main.propertease.server.mediator;

public interface ServerMediator {
    public void broadcast(AbstractClientManager sender, String message);
    public void add(AbstractClientManager client);
}
