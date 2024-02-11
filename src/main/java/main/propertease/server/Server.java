package main.propertease.server;

import main.propertease.server.strategy.ClientManagerContext;
import main.propertease.server.strategy.ClientManagerStrategy;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.function.Function;

/**
 * A simple server that listens for incoming connections and manages them using a strategy.
 */
public class Server {
    /**
     * Creates a new server.
     *
     * @param port The port to listen on.
     * @throws RuntimeException If an error occurs during server creation.
     */
    public Server(int port) {
        try {
            socket = new ServerSocket(port);
            threadPool = new ThreadPool();
            taskHolder = new TaskHolder();
            socket.setReuseAddress(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Starts the server.
     *
     * @param strategyFactory A factory to create a strategy for each client.
     */
    public void start(Function<ClientManager, ClientManagerStrategy> strategyFactory) {
        while (true) {
            try {
                final var clientManager = new ClientManager(socket.accept());
                final var strategy = strategyFactory.apply(clientManager);
                final var strategyContext = new ClientManagerContext(strategy);
                taskHolder.add(threadPool.add(strategyContext.getRunnable()));
            } catch (IOException ignored) {
            }
        }
    }

    private final ServerSocket socket;
    private final ThreadPool threadPool;
    private final TaskHolder taskHolder;
}

