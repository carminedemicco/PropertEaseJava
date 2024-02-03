
package main.propertease;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InsertAvailabilityController implements Initializable {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private DatePicker datePickerStart;

    @FXML
    private DatePicker datePickerEnd;

    @FXML
    private Label errorLabel;

    // Al click di 'Cancel' chiude la finestra
    @FXML
    void cancelButton(ActionEvent event) {
        // Chiusura della finestra corrente
        Stage currentStage = (Stage) anchorPane.getScene().getWindow();
        currentStage.close();
    }

    // AL click di 'Confirm' effettua i controlli sulle date
    // Se le date sono corrette inserisce le date nel database e chiude la finestra
    // Se le date non sono corrette visualizza l'errore senza chiudere la finestra
    @FXML
    void confirmButton(ActionEvent event) {
        // Prendo il testo dei datePicker
        String startDateString = datePickerStart.getEditor().getText();
        String endDateString = datePickerEnd.getEditor().getText();
        // Creo una regex: regular expression, permette di definire il formato
        String regex = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/(\\d{4})$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher1 = pattern.matcher(startDateString);
        Matcher matcher2 = pattern.matcher(endDateString);

        if (matcher1.matches() && matcher2.matches()) {
            // Se entrambe le date sono nel formato corretto
            LocalDate startDate = datePickerStart.getValue();
            LocalDate endDate = datePickerEnd.getValue();

            if (startDate.isAfter(endDate)) {
                // Data inizio successiva a data fine
                errorLabel.setText("Start Date must precede End Date.");
                errorLabel.setVisible(true);
            } else {
                // NB: Logica di inserimento disponibilità
                // ...

                // Chiusura della finestra corrente
                Stage currentStage = (Stage) anchorPane.getScene().getWindow();
                currentStage.close();
            }
        } else {
            // Una o entrambe le date non sono nel formato corretto
            errorLabel.setText("Invalid date format.");
            errorLabel.setVisible(true);
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // annulla il prompt automatico sul DataPicker
        Platform.runLater(() -> anchorPane.requestFocus());

        datePickerStart.setShowWeekNumbers(false);
        datePickerEnd.setShowWeekNumbers(false);
    }

}
