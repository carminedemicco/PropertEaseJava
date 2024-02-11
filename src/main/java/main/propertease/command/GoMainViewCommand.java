package main.propertease.command;

/**
 * Command to switch the scene to the main view.
 */
public class GoMainViewCommand implements Command {

    /**
     * The receiver responsible for handling the button action.
     */
    private final ButtonReceiver buttonReceiver;

    /**
     * Constructor for GoMainViewCommand.
     *
     * @param br The ButtonReceiver object to be associated with the command.
     */
    public GoMainViewCommand(ButtonReceiver br) {
        buttonReceiver = br;
    }

    /**
     * Executes the command, switching the scene to the main view.
     */
    @Override
    public void execute() {
        buttonReceiver.goMainView();
    }
}