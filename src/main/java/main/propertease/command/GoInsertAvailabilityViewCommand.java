package main.propertease.command;

public class GoInsertAvailabilityViewCommand implements Command {
    private final ButtonReceiver buttonReceiver;

    public GoInsertAvailabilityViewCommand(ButtonReceiver br) {
        buttonReceiver = br;
    }

    @Override
    public void execute() {
        buttonReceiver.goInsertAvailabilityView();
    }
}
