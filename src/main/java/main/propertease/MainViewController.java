package main.propertease;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import main.propertease.builder.*;
import main.propertease.command.*;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Manages the mainView.fxml, the view dedicated to the initial screen.
 * Contains the list of appointments and the grid of houses.
 */
public class MainViewController implements Initializable {
    @FXML
    private GridPane gridPane;

    @FXML
    private VBox gridAppointments;

    @FXML
    private HBox adminButtonsArea;

    @FXML
    private Label nameUser;

    // Pattern Command
    ButtonReceiver buttonReceiver;
    GoLoginViewCommand goLoginViewCommand;
    GoAddHouseViewCommand goAddHouseViewCommand;
    GoInsertAvailabilityViewCommand goInsertAvailabilityViewCommand;
    Invoker invoker;


    /**
     * Retrieves house data from the database.
     * Constructs House objects based on the data retrieved.
     * Returns a list of House objects.
     *
     * @return A list of House objects.
     */
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
            final var images = new Image[]{ null, null, null };
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
                            house.getInt("sqm"),
                            house.getInt("price"),
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


    /**
     * Retrieves appointment data from the database.
     * Constructs Appointment objects based on the data retrieved.
     * Returns a list of Appointment objects.
     *
     * @return A list of Appointment objects.
     */
    private List<Appointment> getAppointmentData() {
        final var appointments = new ArrayList<Appointment>();
        final var user = UserAccess.getUser();
        // Selects the appointment data based on the logged user
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
        }
        else {
            message.getJSONObject("data").put("request", "getAppointmentsForUser");
        }
        final var data = ClientConnection
                .getInstance()
                .getClient()
                .exchange(message);
        final var response = data.getJSONArray("response");
        for (final var each : response) {
            appointments.add(getAppointment((JSONObject) each));
        }
        return appointments;
    }

    /**
     * Constructs an Appointment object from the provided JSON data.
     *
     * @param info The JSON object containing appointment information.
     * @return An Appointment object constructed from the JSON data.
     */
    private Appointment getAppointment(JSONObject info) {
        final var agentInfo = info.getJSONObject("agent");
        final var houseInfo = info.getJSONObject("house");

        final var appointment = new Appointment();
        appointment.setId(info.getInt("id"));
        appointment.setHouseName(HouseType.fromValue(houseInfo.getInt("type")).toString());
        appointment.setHouseAddress(houseInfo.getString("address"));
        appointment.setAdministratorName(agentInfo.getString("first_name") + " " + agentInfo.getString("last_name"));
        appointment.setAppointmentDate(info.getString("date"));
        return appointment;
    }

    /**
     * Initializes the view upon creation.
     * Sets the name displayed on the user label, initializes the Command pattern objects,
     * loads the grids of houses and appointments, and adds admin buttons if the user has admin privileges.
     *
     * @param url            The location used to resolve relative paths for the root object, or null if the location
     *                      is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not
     *                       localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameUser.setText("Hi, " + UserAccess.getUser().getLastName());

        // Pattern Command
        buttonReceiver = new ButtonReceiver(StageSingleton.getInstance().getPrimaryStage());
        goLoginViewCommand = new GoLoginViewCommand(buttonReceiver);
        goAddHouseViewCommand = new GoAddHouseViewCommand(buttonReceiver);
        goInsertAvailabilityViewCommand = new GoInsertAvailabilityViewCommand(buttonReceiver);
        invoker = new Invoker();

        try {
            setHousesGrid();
            setAppointmentsGrid();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (UserAccess.getUser().getPrivileges() == 1) {
            addAdminButtons();
        }
    }

    /**
     * Adds admin buttons to the view if the user has admin privileges.
     * Initializes two buttons and sets their actions.
     */
    private void addAdminButtons() {
        final var btn1 = new Button();
        final var btn2 = new Button();
        btn1.getStyleClass().add("add-house-button");
        btn2.getStyleClass().add("add-date-button");
        adminButtonsArea.getChildren().add(btn1);
        adminButtonsArea.getChildren().add(btn2);

        btn1.setOnAction(e -> invoker.placeCommand(goAddHouseViewCommand));
        btn2.setOnAction(e -> invoker.placeCommand(goInsertAvailabilityViewCommand));
    }

    /**
     * Logs the user out and navigates back to the login view.
     *
     * @param event The ActionEvent triggered by clicking the button.
     */
    @FXML
    void logoutButton(ActionEvent event) {
        // Pattern Command
        invoker.placeCommand(goLoginViewCommand);
    }

    /**
     * Loads the grid of houses.
     *
     * @throws Exception If an error occurs during loading.
     */
    public void setHousesGrid() throws
                                Exception {
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

    /**
     * Loads the grid of appointments.
     *
     * @throws Exception If an error occurs during loading.
     */
    public void setAppointmentsGrid() throws
                                      Exception {
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
