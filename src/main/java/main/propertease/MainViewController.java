package main.propertease;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import main.propertease.builder.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.JSONObject;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;

// Classe che gestisce mainView.fxml: la view dedicata alla schermata iniziale
// Contiene la lista degli appuntamenti e la griglia delle case
public class MainViewController implements Initializable {
    @FXML
    private GridPane gridPane;

    @FXML
    private VBox gridAppointments;

    @FXML
    private HBox adminButtonsArea;

    @FXML
    private Label nameUser;


    /* NB: DA MODIFICARE CON LE CASE CHE ARRIVANO DAL DATABASE DEL SERVER */
    // Prende dal database i dati di tutte le case
    // Per ciascuna istanza crea un oggetto House corrispondente e lo inserisce nell'ArrayList
    // Restituisce l'ArrayList di oggetti House
    private final List<House> houses = new ArrayList<>();

    private List<House> getHouseData() throws Exception {
        final List<House> houses = new ArrayList<>();
        final var query = """
            {
              "type": "poster",
              "data": {
                "request": "getHouses",
              },
            }
        """;
        final var data = ClientConnection
            .getInstance()
            .getClient()
            .exchange(new JSONObject(query));
        final var response = data.getJSONArray("response");
        final var houseDirector = new HouseDirector();
        for (var i = 0; i < response.length(); i++) {
            final var house = response.getJSONObject(i);
            final var type = HouseType.fromValue(house.getInt("type"));
            switch (type) {
                case APARTMENT: {
                    final var images = new Image[] { null, null, null };
                    for (var j = 0; j < images.length; j++) {
                        if (!house.isNull("image" + j)) {
                            final var image = house.getString("image" + j);
                            final var bytes = Base64.getDecoder().decode(image);
                            final var stream = new ByteArrayInputStream(bytes);
                            images[j] = new Image(stream);
                        }
                    }
                    final var builder = new ApartmentBuilder();
                    final var result = houseDirector.constructApartment(
                        builder,
                        house.getInt("id"),
                        type,
                        house.getString("address"),
                        house.getInt("floor"),
                        house.getBoolean("elevator"),
                        house.getInt("balconies"),
                        house.getInt("terrace"),
                        house.getInt("accessories"),
                        house.getInt("bedrooms"),
                        house.getInt("sqm"),
                        house.getInt("price"),
                        images
                    );
                    houses.add(result);
                } break;
                case GARAGE: {

                } break;
                case INDEPENDENT: {

                } break;
            }

        }
        return houses;
    }


    /* NB: DA MODIFICARE CON GLI APPUNTAMENTI CHE ARRIVANO DAL DATABASE DEL SERVER */
    // Prende dal database i dati sugli appuntamenti dell'utente loggato
    // Per ciascuna istanza crea un oggetto Appointment corrispondente e lo inserisce nell'ArrayList
    // Restituisce l'ArrayList di oggetti Appointment
    private List<Appointment> appointments = new ArrayList<>();

    private List<Appointment> getAppointmentData() throws Exception {
        List<Appointment> appointments = new ArrayList<>();
        Appointment appointment;

        // Query: seleziona i dati dell'esperto e della casa relativi agli appuntamenti dell'utente loggato
        /* NB: ↓ in futuro CAMBIARE il valore con l'username del utente corrente */
        String currentUsername = "carmine";
        String query = String.format("select appointment.ROWID, date, u.name as uname, u.surname, h.name as hname, h.address1, h.address2 from appointment " +
                                         " inner join house h on h.ROWID = appointment.id_house " +
                                         " inner join useraccount u on u.username = appointment.username_administrator " +
                                         "WHERE username_buyer = '%s'", currentUsername);
        /*Statement statement = connectionDB.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            appointment = new Appointment();
            appointment.setId(resultSet.getInt("ROWID"));
            appointment.setHouseName(resultSet.getString("hname"));
            appointment.setHouseAddress(resultSet.getString("address1") + ", " + resultSet.getString("address2"));
            appointment.setAdministratorName("Estate Agent: " + resultSet.getString("uname") + " " + resultSet.getString("surname"));
            // Codice di conversione dal tipo Date a String
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy, h:mm a.", Locale.ENGLISH);
            String formattedDate = sdf.format(resultSet.getTimestamp("date"));
            appointment.setAppointmentDate(formattedDate);

            appointments.add(appointment);
        }*/

        return appointments;
    }


    // Avviato alla creazione della View
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        nameUser.setText("Hi, " + UserAccess.getUser().getLastName());

        try {
            setHousesGrid();
            setAppointmentsGrid();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if(UserAccess.getUser().getPrivileges() == 1){
            addAdminButtons();
        }
    }

    private void addAdminButtons() {
        Button btn1 = new Button();
        Button btn2 = new Button();
        btn1.getStyleClass().add("add-house-button");
        btn2.getStyleClass().add("add-date-button");
        adminButtonsArea.getChildren().add(btn1);
        adminButtonsArea.getChildren().add(btn2);

        // Al click del bottone di inserimento casa: porta alla View di inserimento
        btn1.setOnAction(e -> {
            try {
                Stage stage = (Stage) gridPane.getScene().getWindow();
                FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("addHouseView.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                stage.setScene(scene);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        // Al click del bottone di inserimento disponibilità: apre il popup relativo
        btn2.setOnAction(e -> {
            try {
                Stage primaryStage = (Stage) gridPane.getScene().getWindow();

                // Crea la nuova scena
                Stage newStage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("popupInsertAvailability.fxml"));
                Scene newScene = new Scene(fxmlLoader.load());
                newStage.setScene(newScene);

                // Blocca l'interazione con le altre finestre finché la finestra appena aperta non viene chiusa.
                newStage.initModality(Modality.WINDOW_MODAL);
                newStage.initOwner(primaryStage);

                // Mostra la nuova finestra
                newStage.setResizable(false);
                newStage.setTitle("Select Available Dates");
                newStage.show();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

    }


    // Al click del bottone di logout: ritorna alla View di login
    @FXML
    void logoutButton(ActionEvent event) throws Exception {
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
    public void setHousesGrid() throws Exception {
        houses.addAll(getHouseData());

        int row = 1;
        int column = 0;

        for (House house : houses) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("houses.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            anchorPane.setEffect(new DropShadow(10, Color.BLACK));

            HousesController housesController = fxmlLoader.getController();
            housesController.setData(house);

            if (column == 3) {
                column = 0;
                row++;
            }

            gridPane.add(anchorPane, column++, row);
            GridPane.setMargin(anchorPane, new Insets(15));
        }
    }


    // Carica la griglia degli appuntamenti
    public void setAppointmentsGrid() throws Exception {
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
