package main.propertease.command;

public class GoAddHouseViewCommand implements Command {
    private final ButtonReceiver buttonReceiver;

    public GoAddHouseViewCommand(ButtonReceiver br) {
        buttonReceiver = br;
    }

    @Override
    public void execute() {
        buttonReceiver.goAddHouseView();
    }
}
