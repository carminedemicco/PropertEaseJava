package main.propertease.command;

/**
 * Command to switch the scene to the insert availability view.
 */
public class GoInsertAvailabilityViewCommand implements Command {

    /**
     * The receiver responsible for handling the button action.
     */
    private final ButtonReceiver buttonReceiver;

    /**
     * Constructor for GoInsertAvailabilityViewCommand.
     *
     * @param br The ButtonReceiver object to be associated with the command.
     */
    public GoInsertAvailabilityViewCommand(ButtonReceiver br) {
        buttonReceiver = br;
    }

    /**
     * Executes the command, switching the scene to the insert availability view.
     */
    @Override
    public void execute() {
        buttonReceiver.goInsertAvailabilityView();
    }
}