package main.propertease.command;

/**
 * Command to switch the scene to the login view.
 */
public class GoLoginViewCommand implements Command {

    /**
     * The receiver responsible for handling the button action.
     */
    private final ButtonReceiver buttonReceiver;

    /**
     * Constructor for GoLoginViewCommand.
     *
     * @param br The ButtonReceiver object to be associated with the command.
     */
    public GoLoginViewCommand(ButtonReceiver br) {
        buttonReceiver = br;
    }

    /**
     * Executes the command, switching the scene to the login view.
     */
    @Override
    public void execute() {
        buttonReceiver.goLoginView();
    }
}