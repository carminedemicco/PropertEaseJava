package main.propertease;

import main.propertease.server.Client;

/**
 * Singleton class for managing the client connection to the server.
 */
public class ClientConnection {
    private static ClientConnection instance = null;
    private final Client client;

    // Private constructor to prevent instantiation from outside
    private ClientConnection() {
        // Initialize the client with the server address and port
        client = new Client("localhost", 1926);
    }

    /**
     * Retrieves the singleton instance of the ClientConnection class.
     *
     * @return The singleton instance of ClientConnection.
     */
    public static ClientConnection getInstance() {
        // Lazy initialization of the singleton instance
        if (instance == null) {
            instance = new ClientConnection();
        }
        return instance;
    }

    /**
     * Retrieves the client object for communicating with the server.
     *
     * @return The client object.
     */
    public Client getClient() {
        return client;
    }
}