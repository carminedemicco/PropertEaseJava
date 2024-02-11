package main.propertease;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/**
 * Controller class for managing insertAvailability.fxml: the view for inserting availability.
 */
public class InsertAvailabilityController implements Initializable {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private DatePicker datePickerStart;

    @FXML
    private DatePicker datePickerEnd;

    @FXML
    private Label errorLabel;

    /**
     * Closes the window when 'Cancel' is clicked.
     *
     * @param event The action event.
     */
    @FXML
    void cancelButton(ActionEvent event) {
        // Close the current window
        final var currentStage = (Stage) anchorPane.getScene().getWindow();
        currentStage.close();
    }

    /**
     * Performs date checks when 'Confirm' is clicked.
     * If the dates are correct, inserts the dates into the database and closes the window.
     * If the dates are incorrect, displays the error without closing the window.
     *
     * @param event The action event.
     */
    @FXML
    void confirmButton(ActionEvent event) {
        // Get the text of the datePickers
        final var startDateString = datePickerStart.getEditor().getText();
        final var endDateString = datePickerEnd.getEditor().getText();
        // Create a regex pattern to define the format
        final var regex = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/(\\d{4})$";
        final var pattern = Pattern.compile(regex);
        final var matcher1 = pattern.matcher(startDateString);
        final var matcher2 = pattern.matcher(endDateString);

        if (matcher1.matches() && matcher2.matches()) {
            // If both dates are in the correct format
            final var startDate = datePickerStart.getValue();
            final var endDate = datePickerEnd.getValue();

            if (startDate.isAfter(endDate)) {
                // Start Date is after End Date
                errorLabel.setText("Start Date must precede End Date.");
                errorLabel.setVisible(true);
            }
            else {
                final var user = UserAccess.getUser();
                final var query = """
                            {
                              "type": "appointment",
                              "data": {
                                "request": "insertAgentAvailability",
                                "parameters": {
                                  "agent": "%s",
                                  "start_date": "%s",
                                  "end_date": "%s",
                                }
                              }
                            }
                        """;
                final var message = new JSONObject(String.format(query, user.getUsername(), startDate, endDate));
                final var data = ClientConnection
                        .getInstance()
                        .getClient()
                        .exchange(message);
                // Close the current window
                final var currentStage = (Stage) anchorPane.getScene().getWindow();
                currentStage.close();
            }
        }
        else {
            // One or both dates are in the incorrect format
            errorLabel.setText("Invalid date format.");
            errorLabel.setVisible(true);
        }
    }

    /**
     * Initializes the controller.
     * Cancels automatic prompt on the DatePicker.
     * Hides week numbers on the DatePickers.
     *
     * @param url            The location used to resolve relative paths for the root object, or null if the location
     *                      is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not
     *                       localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Cancels automatic prompt on the DatePicker
        Platform.runLater(() -> anchorPane.requestFocus());

        // Hides week numbers on the DatePickers
        datePickerStart.setShowWeekNumbers(false);
        datePickerEnd.setShowWeekNumbers(false);
    }
}