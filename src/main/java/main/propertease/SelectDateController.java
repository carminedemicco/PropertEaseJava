
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

public class SelectDateController implements Initializable {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private DatePicker datePicker1;

    @FXML
    private Label errorLabel;

    // Al click di 'Cancel' chiude la finestra
    @FXML
    void cancelButton(ActionEvent event) {
        // Chiusura della finestra corrente
        Stage currentStage = (Stage) anchorPane.getScene().getWindow();
        currentStage.close();
    }

    // AL click di 'Confirm' effettua i controlli sulla data selezionata
    // Se la data è corretta prende l'appuntamento e chiude la finestra
    // Se la data non è corretta visualizza l'errore senza chiudere la finestra
    @FXML
    void confirmButton(ActionEvent event) {
        // Prendo il testo del datePicker
        String dateString = datePicker1.getEditor().getText();
        // Creo una regex: regular expression, permette di definire il formato
        String regex = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/(\\d{4})$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(dateString);

        if (matcher.matches()) {
            // Se la data è nel formato corretto
            LocalDate date = datePicker1.getValue();

            /* NB: da aggiungere */
            // Logica di controllo data: vedi se c'è disponibilità in quella data
            // ...
            // Logica di inserimento appuntamento
            // ...

            // Chiusura della finestra corrente
            Stage currentStage = (Stage) anchorPane.getScene().getWindow();
            currentStage.close();

        }
        else {
            // La data non è nel formato corretto
            errorLabel.setText("Invalid date format.");
            errorLabel.setVisible(true);
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // annulla il prompt automatico sul DataPicker
        Platform.runLater(()->anchorPane.requestFocus());

        datePicker1.setShowWeekNumbers(false);
    }

}
