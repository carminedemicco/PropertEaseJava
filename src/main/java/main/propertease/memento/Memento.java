package main.propertease.memento;

/**
 * Interface for a memento object used in the Memento pattern.
 */
public interface Memento {

    /**
     * Restores the state of an object to a previous state.
     */
    void restoreState();
}