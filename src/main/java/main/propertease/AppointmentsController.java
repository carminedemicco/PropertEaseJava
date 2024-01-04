package main.propertease;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
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
    private Label labelExpert;

    // Aggiornamento del valore dei campi della View
    // Prende in input un oggetto di classe Appointment
    public void setData(Appointment appointment){
        id = appointment.getId();
        labelDate.setText(appointment.getAppointmentDate());
        labelExpert.setText(appointment.getExpertName());
        labelHouseName.setText(appointment.getHouseName());
        labelHouseAddr.setText(appointment.getHouseAddress());
    }

    // Al click del bottone di rimozione appuntamento
    @FXML
    void removeAppointment(ActionEvent event) throws Exception{
        /* SQL: RIMUOVI DAL DATABASE DEGLI APPUNTAMENTI QUELLO SELEZIONATO */
        /*
        Connection connectionDB = DBConnection.getDBConnection();
        String query = String.format("delete from appointment WHERE ROWID = %d", id);
        Statement statement = connectionDB.createStatement();
        statement.executeUpdate(query);

        // NB: da valutare perchè se l'esperto elimina l'appuntamento dal database automaticamente viene eliminato anche per il cliente
        // (senza nemmeno una notifica ?)

        // Ricarica la pagina escludendo l'appuntamento appena cancellato
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("buyerView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        */


        // Unica alternativa è usare un campo flag nel database che indica se è visibile o meno
    }
}