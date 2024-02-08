package main.propertease.command;

public class GoMainViewCommand implements Command{
    private final ButtonReceiver buttonReceiver;

    public GoMainViewCommand(ButtonReceiver br){
        buttonReceiver = br;
    }

    @Override
    public void execute() {
        buttonReceiver.goMainView();
    }
}
