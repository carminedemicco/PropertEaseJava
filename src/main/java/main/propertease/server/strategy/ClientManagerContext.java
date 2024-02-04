package main.propertease.server.strategy;

public class ClientManagerContext {
    public ClientManagerContext(ClientManagerStrategy strategy) {
        this.strategy = strategy;
    }

    public Runnable getRunnable() {
        return () -> {
            while (true) {
                strategy.communicate();
            }
        };
    }

    private final ClientManagerStrategy strategy;
}
