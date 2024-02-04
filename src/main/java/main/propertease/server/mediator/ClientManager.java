package main.propertease.server.mediator;

import java.net.Socket;

public class ClientManager extends AbstractClientManager {
    public ClientManager(ServerMediator serverMediator, Socket socket) {
        super(serverMediator, socket);
    }

    @Override
    public void broadcast(String message) {
        serverMediator.broadcast(this, message);
    }
}
