package main.propertease;

import main.propertease.server.Client;

public class ClientConnection {
    private ClientConnection() {
        client = new Client("localhost", 1926);
    }

    public static ClientConnection getInstance() {
        if (instance == null) {
            instance = new ClientConnection();
        }
        return instance;
    }

    public Client getClient() {
        return client;
    }

    private static ClientConnection instance = null;
    private final Client client;
}
