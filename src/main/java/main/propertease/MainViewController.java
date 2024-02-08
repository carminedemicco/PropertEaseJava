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


    // Prende dal database i dati di tutte le case
    // Per ciascuna istanza crea un oggetto House corrispondente e lo inserisce nell'ArrayList
    // Restituisce l'ArrayList di oggetti House

    private List<House> getHouseData() {
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
        for (var i = 0; i < response.length(); i++) {
            final var house = response.getJSONObject(i);
            final var type = HouseType.fromValue(house.getInt("type"));
            final var images = new Image[]{null, null, null};
            for (var j = 0; j < images.length; j++) {
                if (!house.isNull("image" + j)) {
                    final var image = house.getString("image" + j);
                    final var bytes = Base64.getDecoder().decode(image);
                    final var stream = new ByteArrayInputStream(bytes);
                    images[j] = new Image(stream);
                }
            }
            switch (type) {
                case APARTMENT: {
                    final var builder = new ApartmentBuilder();
                    final var houseDirector = new HouseDirector(builder);
                    final var result = houseDirector.constructApartment(
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
                        house.getString("description"),
                        images
                    );
                    houses.add(result);
                    break;
                }
                case GARAGE: {
                    final var builder = new GarageBuilder();
                    final var houseDirector = new HouseDirector(builder);
                    final var result = houseDirector.constructGarage(
                        house.getInt("id"),
                        type,
                        house.getString("address"),
                        house.getInt("price"),
                        house.getInt("sqm"),
                        house.getString("description"),
                        images
                    );
                    houses.add(result);
                    break;
                }
                case INDEPENDENT: {
                    final var builder = new IndependentBuilder();
                    final var houseDirector = new HouseDirector(builder);
                    final var result = houseDirector.constructIndependent(
                        house.getInt("id"),
                        type,
                        house.getString("address"),
                        house.getInt("garden"),
                        house.getInt("terrace"),
                        house.getInt("accessories"),
                        house.getInt("bedrooms"),
                        house.getInt("sqm"),
                        house.getInt("price"),
                        house.getString("description"),
                        images
                    );
                    houses.add(result);
                    break;
                }
            }

        }
        return houses;
    }


    // Prende dal database i dati sugli appuntamenti dell'utente loggato
    // Per ciascuna istanza crea un oggetto Appointment corrispondente e lo inserisce nell'ArrayList
    // Restituisce l'ArrayList di oggetti Appointment

    private List<Appointment> getAppointmentData() throws Exception {
        final var appointments = new ArrayList<Appointment>();
        final var user = UserAccess.getUser();
        // Seleziona i dati dell'esperto e della casa relativi agli appuntamenti dell'utente loggato
        final var query = """
              {
                "type": "appointment",
                "data": {
                  "parameters": {
                    "username": "%s"
                  }
                }
              }
        """;
        final var message = new JSONObject(String.format(query, user.getUsername()));
        if (user.getPrivileges() == 1) {
            message.getJSONObject("data").put("request", "getAppointmentsForAgent");
        } else {
            message.getJSONObject("data").put("request", "getAppointmentsForUser");
        }
        final var data = ClientConnection
            .getInstance()
            .getClient()
            .exchange(message);
        final var response = data.getJSONArray("response");
        for (final var each : response) {
            appointments.add(getAppointment((JSONObject)each));
        }
        return appointments;
    }

    private Appointment getAppointment(JSONObject info) {
        final var agentInfo = info.getJSONObject("agent");
        final var houseInfo = info.getJSONObject("house");

        final var appointment = new Appointment();
        appointment.setId(info.getInt("id"));
        appointment.setHouseName(HouseType.fromValue(houseInfo.getInt("type")).toString());
        appointment.setHouseAddress(houseInfo.getString("address"));
        appointment.setAdministratorName(
            String.format(
                "Estate Agent: %s %s",
                agentInfo.getString("first_name"),
                agentInfo.getString("last_name")
            )
        );
        appointment.setAppointmentDate(info.getString("date"));
        return appointment;
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

        if (UserAccess.getUser().getPrivileges() == 1) {
            addAdminButtons();
        }
    }

    private void addAdminButtons() {
        final var btn1 = new Button();
        final var btn2 = new Button();
        btn1.getStyleClass().add("add-house-button");
        btn2.getStyleClass().add("add-date-button");
        adminButtonsArea.getChildren().add(btn1);
        adminButtonsArea.getChildren().add(btn2);

        // Al click del bottone di inserimento casa: porta alla View di inserimento
        btn1.setOnAction(e -> {
            try {
                final var stage = (Stage)gridPane.getScene().getWindow();
                final var fxmlLoader = new FXMLLoader(StartApplication.class.getResource("addHouseView.fxml"));
                final var scene = new Scene(fxmlLoader.load());
                stage.setScene(scene);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        // Al click del bottone di inserimento disponibilità: apre il popup relativo
        btn2.setOnAction(e -> {
            try {
                final var primaryStage = (Stage)gridPane.getScene().getWindow();

                // Crea la nuova scena
                final var newStage = new Stage();
                final var fxmlLoader = new FXMLLoader(StartApplication.class.getResource("popupInsertAvailability.fxml"));
                final var newScene = new Scene(fxmlLoader.load());
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
        final var stage = (Stage)gridPane.getScene().getWindow();
        final var fxmlLoader = new FXMLLoader(StartApplication.class.getResource("loginView.fxml"));
        final var scene = new Scene(fxmlLoader.load());
        stage.hide();
        stage.setScene(scene);
        stage.show();
    }


    // Carica la griglia delle case
    public void setHousesGrid() throws Exception {
        int row = 1;
        int column = 0;

        for (final var house : getHouseData()) {
            final var fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("houses.fxml"));
            final var anchorPane = fxmlLoader.<AnchorPane>load();
            anchorPane.setEffect(new DropShadow(10, Color.BLACK));

            final var housesController = fxmlLoader.<HousesController>getController();
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
        for (final var appointment : getAppointmentData()) {
            final var fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("appointments.fxml"));
            final var anchorPane = fxmlLoader.<AnchorPane>load();
            anchorPane.setEffect(new DropShadow(5, Color.BLACK));

            final var appointmentsController = fxmlLoader.<AppointmentsController>getController();
            appointmentsController.setData(appointment);

            gridAppointments.getChildren().add(anchorPane);
        }
    }


}
