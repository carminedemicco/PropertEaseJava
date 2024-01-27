
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
import java.util.ResourceBundle;

public class PopupSelectDateController implements Initializable {

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
        // Logica di controllo data
        // ...
        // Logica di inserimento appuntamento
        // ...

        // Chiusura della finestra corrente
        Stage currentStage = (Stage) anchorPane.getScene().getWindow();
        currentStage.close();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // annulla il prompt automatico sul DataPicker
        Platform.runLater(()->anchorPane.requestFocus());

        datePicker1.setShowWeekNumbers(false);
    }

}
