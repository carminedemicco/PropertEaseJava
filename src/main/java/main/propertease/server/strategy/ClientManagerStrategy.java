package main.propertease.server.strategy;

/**
 * Interface for a strategy to manage a client.
 */
public interface ClientManagerStrategy {
    /**
     * Implements client/server communication.
     *
     * @return <b>true</b> if the client should continue to be managed, <b>false</b> otherwise.
     */
    public boolean communicate();
}
