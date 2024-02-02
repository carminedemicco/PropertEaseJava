package main.propertease;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.Statement;

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
    public void setData(Appointment appointment){
        id = appointment.getId();
        labelDate.setText(appointment.getAppointmentDate());
        labelAgent.setText(appointment.getAdministratorName());
        labelHouseName.setText(appointment.getHouseName());
        labelHouseAddr.setText(appointment.getHouseAddress());
    }

    // Al click del bottone di rimozione appuntamento
    @FXML
    void removeAppointment(ActionEvent event) throws Exception{
        // Apri il popup di conferma
        Stage primaryStage = (Stage) anchorPane.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("popupConfirm.fxml"));
        Scene newScene = new Scene(fxmlLoader.load());
        Stage newStage = new Stage();
        newStage.setScene(newScene);
        newStage.setResizable(false);
        newStage.setTitle("Confirm Cancellation");
        // Blocca l'interazione con le altre finestre finché la finestra appena aperta non viene chiusa.
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.initOwner(primaryStage);

        // mostra il popup e blocca l'esecuzione fino a quando lo Stage secondario non viene chiuso
        newStage.showAndWait();

        // Prendo il controller del popup
        PopupConfirmController popupConfirmController = fxmlLoader.getController();
        // Prendo il valore di ritorno del popup dal controller
        if(popupConfirmController.getResult()){
            // Se restituisce true('Confirm') effettua la cancellazione dell'appuntamento dal database

            /* NB: da cambiare con la cancellazione sul server */
            // SQL: rimuovi dal database l'appuntamento selezionato.
            Connection connectionDB = DBConnection.getDBConnection();
            String query = String.format("delete from appointment WHERE ROWID = %d", id);
            Statement statement = connectionDB.createStatement();
            statement.executeUpdate(query);

            // Ricarica la pagina escludendo l'appuntamento appena cancellato
            fxmlLoader = new FXMLLoader(StartApplication.class.getResource("mainView.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            primaryStage.setScene(scene);
        }
    }
}