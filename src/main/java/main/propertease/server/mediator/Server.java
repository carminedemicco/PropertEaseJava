package main.propertease.server.mediator;

import main.propertease.server.TaskHolder;
import main.propertease.server.ThreadPool;
import main.propertease.server.strategy.ClientManagerContext;
import main.propertease.server.strategy.ClientManagerStrategy;

import java.io.*;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Server implements ServerMediator {
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

    public void start(Function<AbstractClientManager, ClientManagerStrategy> strategyFactory) {
        while (true) {
            try {
                final var clientManager = new ClientManager(this, socket.accept());
                final var strategy = strategyFactory.apply(clientManager);
                final var strategyContext = new ClientManagerContext(strategy);
                taskHolder.add(threadPool.add(strategyContext.getRunnable()));
                add(clientManager);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void broadcast(AbstractClientManager sender, String message) {
        for (final var clientManager : clientManagers) {
            if (clientManager != sender) {
                clientManager.writeLine(message);
            }
        }
    }

    @Override
    public void add(AbstractClientManager client) {
        clientManagers.add(client);
    }

    private final List<AbstractClientManager> clientManagers = new ArrayList<>();
    private final ServerSocket socket;
    private final ThreadPool threadPool;
    private final TaskHolder taskHolder;
}

