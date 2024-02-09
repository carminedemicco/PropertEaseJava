package main.propertease.command;

import java.util.ArrayList;

public class Invoker {
    private ArrayList<Command> commands = new ArrayList<>();

    public void placeCommand(Command command) {
        // Aggiunge il comando alla lista
        commands.add(command);

        // Restituisce e rimuove il primo elemento e richiama il suo metodo execute().
        commands.removeFirst().execute();
    }
}
