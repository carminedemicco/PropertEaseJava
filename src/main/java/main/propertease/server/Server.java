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
            /*
             * The ServerSocket(port) constructor allows us to:
             * - Create a new socket (equivalent to calling `socket()`);
             * - Bind the specified port to the socket (equivalent to calling `bind()`);
             * - Marks the socket as a passive socket (equivalent to calling `listen()`), that is, a socket that will be
             *  used to accept connections using `accept()`.
             */
            socket = new ServerSocket(port);
            /*
             * Creates a ThreadPool object, using as many threads as requested (or N hardware threads using the default
             * constructor).
             * This will be used in the event of an incoming connection to:
             * - Take one of the available threads and assign the connected client to it;
             * - If there are no threads available, wait for one to become available.
             */
            threadPool = new ThreadPool(1024);
            /*
             * A simple wrapper class used to wait on Future<?> objects
             */
            taskHolder = new TaskHolder();
            // Allows the socket to reuse addresses for connections in a TIMEOUT state.
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
                /*
                 * Equivalent to C's `accept()`: This accepts an incoming connection, forming a Socket, or waits for one
                 * if there are no incoming connections. An appropriate strategy will be materialized after every connection.
                 */
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

