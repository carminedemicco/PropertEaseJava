package main.propertease.server.strategy;

public class ClientManagerContext {
    public ClientManagerContext(ClientManagerStrategy strategy) {
        this.strategy = strategy;
    }

    public Runnable getRunnable() {
        return () -> {
            var isOpen = true;
            while (isOpen) {
                isOpen = strategy.communicate();
            }
        };
    }

    private final ClientManagerStrategy strategy;
}
