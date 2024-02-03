package main.propertease;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

// Classe che gestisce popupConfirm.fxml: un popup che chide la conferma di cancellazione appuntamento
public class PopupConfirmController {

    @FXML
    private AnchorPane anchorPane;

    // Alla chiusura del popup viene restituito true se l'utente clicca il bottone di conferma
    private boolean result = false;

    public boolean getResult() {
        return result;
    }

    // Al click di 'Cancel' chiude la finestra
    @FXML
    void cancelButton(ActionEvent event) {
        // Chiusura della finestra corrente
        Stage currentStage = (Stage) anchorPane.getScene().getWindow();
        currentStage.close();
    }

    // Al click di 'Confirm' viene restituito true e si chiude la finestra
    @FXML
    void confirmButton(ActionEvent event) {
        result = true;
        // Chiusura della finestra corrente
        Stage currentStage = (Stage) anchorPane.getScene().getWindow();
        currentStage.close();
    }

}