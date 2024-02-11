package main.propertease.command;

import java.util.ArrayList;

/**
 * Invoker class responsible for executing commands.
 */
public class Invoker {

    /**
     * List of commands to be executed.
     */
    private ArrayList<Command> commands = new ArrayList<>();

    /**
     * Places a command in the queue and executes the first command in the queue.
     *
     * @param command The command to be executed.
     */
    public void placeCommand(Command command) {
        commands.add(command);
        commands.removeFirst().execute();
    }
}