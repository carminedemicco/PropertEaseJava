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

public class SelectDateController implements Initializable {
    private int houseId;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private DatePicker datePicker1;

    @FXML
    private Label errorLabel;

    public void setData(int houseId) {
        this.houseId = houseId;
    }

    // Al click di 'Cancel' chiude la finestra
    @FXML
    public void cancelButton(ActionEvent event) {
        // Chiusura della finestra corrente
        final var currentStage = (Stage)anchorPane.getScene().getWindow();
        currentStage.close();
    }

    // AL click di 'Confirm' effettua i controlli sulla data selezionata
    // Se la data è corretta prende l'appuntamento e chiude la finestra
    // Se la data non è corretta visualizza l'errore senza chiudere la finestra
    @FXML
    public void confirmButton(ActionEvent event) {
        // Prendo il testo del datePicker
        final var dateString = datePicker1.getEditor().getText();
        // Creo una regex: regular expression, permette di definire il formato
        final var regex = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/(\\d{4})$";
        final var pattern = Pattern.compile(regex);
        final var matcher = pattern.matcher(dateString);

        // Se la data è nel formato corretto
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
                // Gestisci l'errore
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
                // Appuntamento preso con successo
                // Chiusura della finestra corrente
                final var currentStage = (Stage)anchorPane.getScene().getWindow();
                currentStage.close();
            }

        } else {
            // La data non è nel formato corretto
            errorLabel.setText("Invalid date format.");
            errorLabel.setVisible(true);
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // annulla il prompt automatico sul DataPicker
        Platform.runLater(() -> anchorPane.requestFocus());

        datePicker1.setShowWeekNumbers(false);
    }

}
