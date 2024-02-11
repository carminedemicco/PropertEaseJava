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
 * FXML Controller class for the date selection view (popupSelectDate.fxml)
 */
public class SelectDateController implements Initializable {
    private int houseId;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private DatePicker datePicker1;

    @FXML
    private Label errorLabel;

    /**
     * Utility method to set the house id
     * @param houseId the house id
     */
    public void setData(int houseId) {
        this.houseId = houseId;
    }

    /**
     * Closes thecurrent window when the 'cancel' button is clicked
     * @param event The event that triggered the method
     */
    @FXML
    public void cancelButton(ActionEvent event) {
        final var currentStage = (Stage)anchorPane.getScene().getWindow();
        currentStage.close();
    }

    /**
     * Confirms the selected date and books the appointment
     * <p>If the date is correct, the appointment is booked, otherwise, the server
     *    responds with an error message that is displayed to the user</p>
     * @param event The event that triggered the method
     */
    @FXML
    public void confirmButton(ActionEvent event) {
        // Get the date from the DatePicker
        final var dateString = datePicker1.getEditor().getText();
        // Regular expression to match the date format
        final var regex = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/(\\d{4})$";
        final var pattern = Pattern.compile(regex);
        final var matcher = pattern.matcher(dateString);

        // If the date is in the correct format, send the request to the server
        if (matcher.matches()) {
            final var date = datePicker1.getValue();
            final var message = new JSONObject(
                String.format(
                    """
                        {
                          "type": "appointment",
                          "data": {
                            "request": "bookAppointment",
                            "parameters": {
                              "username": "%s",
                              "date": "%s",
                              "house": %d
                            }
                          }
                        }
                    """,
                    UserAccess.getUser().getUsername(),
                    date,
                    houseId
                )
            );
            final var response = ClientConnection
                .getInstance()
                .getClient()
                .exchange(message);
            final var data = response.getJSONObject("response");
            if (data.has("error")) {
                final var error = data.getString("error");
                // If the server responds with an error, display the error message
                switch (error) {
                    case "alreadyBooked":
                        errorLabel.setText("Already booked for this date.");
                        break;

                    case "notAvailable":
                        errorLabel.setText("Agent is not available for this date.");
                        break;

                    default:
                        errorLabel.setText("An unknown error occurred.");
                        break;
                }
                errorLabel.setVisible(true);
            } else {
                // If the server responds with no error, close the window
                final var currentStage = (Stage)anchorPane.getScene().getWindow();
                currentStage.close();
            }

        } else {
            errorLabel.setText("Invalid date format.");
            errorLabel.setVisible(true);
        }
    }


    /**
     * Initializes the controller class.
     * @param url The location used to resolve relative paths for the root object, or <b>null</b>
     *            if the location is not known.
     *
     * @param resourceBundle The resources used to localize the root object, or <b>null</b> if
     *                       the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // annulla il prompt automatico sul DataPicker
        Platform.runLater(() -> anchorPane.requestFocus());

        datePicker1.setShowWeekNumbers(false);
    }

}
