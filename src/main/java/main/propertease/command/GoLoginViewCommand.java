package main.propertease.command;

public class GoLoginViewCommand implements Command {
    private final ButtonReceiver buttonReceiver;

    public GoLoginViewCommand(ButtonReceiver br) {
        buttonReceiver = br;
    }

    @Override
    public void execute() {
        buttonReceiver.goLoginView();
    }
}
