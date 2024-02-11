package main.propertease.command;

/**
 * Command to switch the scene to the add house view.
 */
public class GoAddHouseViewCommand implements Command {

    /**
     * The receiver responsible for handling the button action.
     */
    private final ButtonReceiver buttonReceiver;

    /**
     * Constructor for GoAddHouseViewCommand.
     *
     * @param br The ButtonReceiver object to be associated with the command.
     */
    public GoAddHouseViewCommand(ButtonReceiver br) {
        buttonReceiver = br;
    }

    /**
     * Executes the command, switching the scene to the add house view.
     */
    @Override
    public void execute() {
        buttonReceiver.goAddHouseView();
    }
}