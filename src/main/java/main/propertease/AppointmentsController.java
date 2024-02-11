package main.propertease;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.JSONObject;

// Classe che gestisce appointments.fxml: la view dedicata ai dettagli degli appuntamenti
public class AppointmentsController {
    private int id;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Label labelHouseAddr;

    @FXML
    private Label labelHouseName;

    @FXML
    private Label labelDate;

    @FXML
    private Label labelAgent;

    // Aggiornamento del valore dei campi della View
    // Prende in input un oggetto di classe Appointment
    public void setData(Appointment appointment) {
        id = appointment.getId();
        labelDate.setText(appointment.getAppointmentDate());
        if (UserAccess.getUser().getPrivileges() == 1) {
            labelAgent.setText("Client: " + appointment.getAdministratorName());
        } else {
            labelAgent.setText("Estate Agent: " + appointment.getAdministratorName());
        }
        labelHouseName.setText(appointment.getHouseName());
        labelHouseAddr.setText(appointment.getHouseAddress().replace("|", ", "));
    }

    // Al click del bottone di rimozione appuntamento
    @FXML
    void removeAppointment(ActionEvent event) throws Exception {
        // Apri il popup di conferma
        final var primaryStage = StageSingleton.getInstance().getPrimaryStage();
        final var fxmlLoader = new FXMLLoader(StartApplication.class.getResource("popupConfirm.fxml"));
        final var newScene = new Scene(fxmlLoader.load());
        final var newStage = new Stage();
        newStage.setScene(newScene);
        newStage.setResizable(false);
        newStage.setTitle("Confirm Cancellation");
        // Blocca l'interazione con le altre finestre finché la finestra appena aperta non viene chiusa.
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.initOwner(primaryStage);

        // mostra il popup e blocca l'esecuzione fino a quando lo Stage secondario non viene chiuso
        newStage.showAndWait();

        // Prendo il controller del popup
        final var popupConfirmController = fxmlLoader.<PopupConfirmController>getController();
        // Prendo il valore di ritorno del popup dal controller
        if (popupConfirmController.getResult()) {
            final var message = new JSONObject(
                String.format(
                    """
                        {
                          "type": "appointment",
                          "data": {
                            "request": "removeAppointment",
                            "parameters": {
                              "id": %d
                            }
                          }
                        }
                        """,
                    id
                )
            );
            // ignora il risultato di exchange, non può mai fallire
            final var ignored = ClientConnection
                .getInstance()
                .getClient()
                .exchange(message);
            final var newFxmlLoader = new FXMLLoader(StartApplication.class.getResource("mainView.fxml"));
            final var scene = new Scene(newFxmlLoader.load());
            primaryStage.setScene(scene);
        }
    }
}