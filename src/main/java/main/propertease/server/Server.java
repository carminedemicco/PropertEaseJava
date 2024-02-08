package main.propertease.server;

import main.propertease.server.strategy.ClientManagerContext;
import main.propertease.server.strategy.ClientManagerStrategy;

import java.io.*;
import java.net.ServerSocket;
import java.util.function.Function;

public class Server {
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

    public void start(Function<ClientManager, ClientManagerStrategy> strategyFactory) {
        while (true) {
            try {
                final var clientManager = new ClientManager(socket.accept());
                final var strategy = strategyFactory.apply(clientManager);
                final var strategyContext = new ClientManagerContext(strategy);
                taskHolder.add(threadPool.add(strategyContext.getRunnable()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private final ServerSocket socket;
    private final ThreadPool threadPool;
    private final TaskHolder taskHolder;
}

