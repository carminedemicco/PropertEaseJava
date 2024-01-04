package main.propertease;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;

// Classe che gestisce buyerView.fxml: la view dedicata alla schermata iniziale di un Buyer:
// Contiene la lista degli appuntamenti e la griglia delle case
public class BuyerViewController implements Initializable {
     private Connection connectionDB;

    @FXML
    private GridPane gridPane;

    @FXML
    private VBox gridAppointments;

    /* NB: DA MODIFICARE CON LE CASE CHE ARRIVANO DAL DATABASE DEL SERVER */
    // Prende dal database i dati di tutte le case
    // Per ciascuna istanza crea un oggetto House corrispondente e lo inserisce nell'ArrayList
    // Restituisce l'ArrayList di oggetti House
    private List<House> houses = new ArrayList<>();
    private List<House> getHouseData() throws Exception{
        List<House> houses = new ArrayList<>();
        House house;

        // Query: dati principali di tutte le case
        String query = "select ROWID, name, address1, address2, price, img1 from hause;";
        Statement statement = connectionDB.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        // Per ogni istanza setta i campi dell'oggetto corrispondente
        while (resultSet.next()){
            house = new House();
            house.setId(resultSet.getInt("ROWID"));
            house.setName(resultSet.getString("name"));
            house.setPrice(resultSet.getString("price"));
            house.setAddress1(resultSet.getString("address1"));
            house.setAddress2(resultSet.getString("address2"));
            house.setImgsrc(resultSet.getString("img1"));

            houses.add(house);
        }

        return houses;
    }


    /* NB: DA MODIFICARE CON GLI APPUNTAMENTI CHE ARRIVANO DAL DATABASE DEL SERVER */
    // Prende dal database i dati sugli appuntamenti dell'utente loggato
    // Per ciascuna istanza crea un oggetto Appointment corrispondente e lo inserisce nell'ArrayList
    // Restituisce l'ArrayList di oggetti Appointment
    private List<Appointment> appointments = new ArrayList<>();
    private List<Appointment> getAppointmentData() throws Exception{
        List<Appointment> appointments = new ArrayList<>();
        Appointment appointment;

        // Query: seleziona i dati dell'esperto e della casa relativi agli appuntamenti dell'utente loggato
        /* NB: ↓ in futuro CAMBIARE il valore con l'username del utente corrente */
        String currentUsername = "carmine";
        String query = String.format("select appointment.ROWID, date, u.name as uname, u.surname, h.name as hname, h.address1, h.address2 from appointment " +
                " inner join hause h on h.ROWID = appointment.id_house " +
                " inner join useraccount u on u.username = appointment.username_seller_expert " +
                "WHERE username_buyer = '%s'", currentUsername);
        Statement statement = connectionDB.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()){
            appointment = new Appointment();
            appointment.setId(resultSet.getInt("ROWID"));
            appointment.setHouseName(resultSet.getString("hname"));
            appointment.setHouseAddress(resultSet.getString("address1") + ", " + resultSet.getString("address2"));
            appointment.setExpertName("Expert/Seller: " + resultSet.getString("uname") + " " + resultSet.getString("surname"));
            // Codice di conversione dal tipo Date a String
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy, h:mm a.", Locale.ENGLISH);
            String formattedDate = sdf.format(resultSet.getTimestamp("date"));
            appointment.setAppointmentDate(formattedDate);

            appointments.add(appointment);
        }

        return appointments;
    }


    // Avviato alla creazione della View
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            connectionDB = DBConnection.getDBConnection();
            setHousesGrid();
            setAppointmentsGrid();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    // Al click del bottone di logout: ritorna alla View di login
    @FXML
    void logoutButton(ActionEvent event) throws Exception{
        // Operazioni di logout
        //...

        // Torna alla schermata di login
        Stage stage = (Stage) gridPane.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("loginView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.hide();
        stage.setScene(scene);
        stage.show();
    }


    // Carica la griglia delle case
    public void setHousesGrid() throws Exception{
        houses.addAll(getHouseData());

        int row = 1; int column = 0;

        for (House house : houses) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("houses.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            anchorPane.setEffect(new DropShadow(10, Color.BLACK));

            HousesController housesController = fxmlLoader.getController();
            housesController.setData(house);

            if (column == 3){
                column = 0;
                row++;
            }
            gridPane.add(anchorPane, column++, row);
            GridPane.setMargin(anchorPane, new Insets(15));
        }
    }


    // Carica la griglia degli appuntamenti
    public void setAppointmentsGrid() throws Exception{
        appointments.addAll(getAppointmentData());
        for (Appointment appointment : appointments) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("appointments.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            anchorPane.setEffect(new DropShadow(5, Color.BLACK));

            AppointmentsController appointmentsController = fxmlLoader.getController();
            appointmentsController.setData(appointment);

            gridAppointments.getChildren().add(anchorPane);
        }
    }


}
