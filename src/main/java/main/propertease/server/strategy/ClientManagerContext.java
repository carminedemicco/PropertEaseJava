package main.propertease.server.strategy;

/**
 * Context for a client manager strategy.
 */
public class ClientManagerContext {
    /**
     * Creates a new context for a client manager strategy.
     *
     * @param strategy The strategy to use.
     */
    public ClientManagerContext(ClientManagerStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Gets the runnable for the strategy.
     *
     * @return The runnable for the strategy.
     * @see Runnable
     */
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
