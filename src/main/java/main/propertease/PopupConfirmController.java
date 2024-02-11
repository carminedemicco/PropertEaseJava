package main.propertease;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class for the confirmation popup (popupConfirm.fxml)
 */
public class PopupConfirmController {

    @FXML
    private AnchorPane anchorPane;

    private boolean result = false;

    /**
     * Get the result of the confirmation
     * @return the result of the confirmation
     */
    public boolean getResult() {
        return result;
    }

    /**
     * Closes the current window when the 'cancel' button is clicked and sets the result to false
     * @param event The event that triggered the method
     */
    @FXML
    void cancelButton(ActionEvent event) {
        final var currentStage = (Stage)anchorPane.getScene().getWindow();
        currentStage.close();
    }

    /**
     * Confirms the action and sets the result to true
     * @param event The event that triggered the method
     */
    @FXML
    void confirmButton(ActionEvent event) {
        result = true;
        final var currentStage = (Stage)anchorPane.getScene().getWindow();
        currentStage.close();
    }

}