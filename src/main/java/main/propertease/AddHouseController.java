package main.propertease;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.propertease.builder.*;
import main.propertease.command.ButtonReceiver;
import main.propertease.command.GoLoginViewCommand;
import main.propertease.command.GoMainViewCommand;
import main.propertease.command.Invoker;
import main.propertease.memento.Memento;
import org.glavo.png.PNGWriter;
import org.glavo.png.image.ArgbImage;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;
import java.util.ResourceBundle;

public class AddHouseController implements Initializable {

    @FXML
    private TextField accessoriesField;

    @FXML
    private TextField addressField;

    @FXML
    private TextField balconiesField;

    @FXML
    private TextField bedroomsField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private Button deleteButton;

    @FXML
    private TextField elevatorField;

    @FXML
    private TextField floorField;

    @FXML
    private TextField gardenField;

    @FXML
    private TextField sqmField;

    @FXML
    private TextField terraceField;

    @FXML
    private Label nameImg1;

    @FXML
    private Label nameImg2;

    @FXML
    private Label nameImg3;

    @FXML
    private Label nameUser;

    @FXML
    private Label errorLabel;

    @FXML
    private TextField priceField;

    @FXML
    private ComboBox<String> houseTypeComboBox;
    private final String[] houseType = {"Apartment", "Garage", "Independent"};

    // Pattern Command
    ButtonReceiver buttonReceiver;
    GoMainViewCommand goMainViewCommand;
    GoLoginViewCommand goLoginViewCommand;
    Invoker invoker;
    Boolean modifyHouse = false;

    House house;
    Memento houseMemento;

    /**
     * Sets the data of the house to be modified.
     *
     * @param house The house object containing the data to be modified.
     */
    public void setModifyData(House house) {
        modifyHouse = true;

        this.house = house;
        for (var i = 0; i < pics.length; i++) {
            if (pics[i] == null) {
                pics[i] = house.getPics(i);
            }
        }

        nameImg1.setStyle("-fx-text-fill: #f0f8ff");
        nameImg1.setText("img1.png");
        nameImg2.setStyle("-fx-text-fill: #f0f8ff");
        nameImg2.setText("img2.png");
        nameImg3.setStyle("-fx-text-fill: #f0f8ff");
        nameImg3.setText("img3.png");
        houseTypeComboBox.setValue(house.getType().toString());
        houseTypeComboBox.setDisable(true);

        addressField.setText(house.getAddress());

        blockFields();

        switch (house.getType()) {
            case APARTMENT:
                floorField.setText(String.valueOf(house.getFloor()));
                elevatorField.setText(String.valueOf(house.hasElevator()));
                balconiesField.setText(String.valueOf(house.getBalconies()));
                terraceField.setText(String.valueOf(house.getTerrace()));
                accessoriesField.setText(String.valueOf(house.getAccessories()));
                bedroomsField.setText(String.valueOf(house.getBedrooms()));
                break;

            case GARAGE:
                // No additional fields for garage type.
                break;

            case INDEPENDENT:
                balconiesField.setText(String.valueOf(house.getBalconies()));
                terraceField.setText(String.valueOf(house.getTerrace()));
                gardenField.setText(String.valueOf(house.getGarden()));
                accessoriesField.setText(String.valueOf(house.getAccessories()));
                bedroomsField.setText(String.valueOf(house.getBedrooms()));
                break;
        }

        sqmField.setText(String.valueOf(house.getSqm()));
        priceField.setText(String.valueOf(house.getPrice()));
        descriptionField.setText(house.getDescription());
        deleteButton.setVisible(true);
    }


    File[] picsFile = new File[3];
    Image[] pics = new Image[3];
    final FileChooser fc = new FileChooser();

    /**
     * Adds an image to the first slot of the pictures array.
     *
     * @param event The ActionEvent triggered by the user action.
     */
    @FXML
    void addImg1(ActionEvent event) {
        addImg(0);
    }

    @FXML
    void addImg2(ActionEvent event) {
        addImg(1);
    }

    @FXML
    void addImg3(ActionEvent event) {
        addImg(2);
    }

    private void addImg(int id) {
        final var imageLabels = Arrays.asList(nameImg1, nameImg2, nameImg3);
        picsFile[id] = fc.showOpenDialog(null);

        if (picsFile[id] != null) {
            imageLabels.get(id).setStyle("-fx-text-fill: #f0f8ff");
            imageLabels.get(id).setText(picsFile[id].getName());
            pics[id] = new Image(picsFile[id].toURI().toString());
        }
    }

    /**
     * Event handler for confirming house details.
     *
     * <p> This method validates the entered details based on the selected house type,
     * updates the house object accordingly, and sends a request to insert/update the house data.
     *
     * @param event The ActionEvent object representing the event triggered by confirming the details.
     */
    @FXML
    void confirmButton(ActionEvent event) {
        var isGood = true;
        if (houseTypeComboBox.getValue() == null) {
            isGood = false;
            System.out.println("No house type selected.");
        } else {
            switch (houseTypeComboBox.getValue()) {
                case "Apartment":
                    isGood &=
                        !addressField.getText().isEmpty() &
                            !floorField.getText().isEmpty() &
                            !elevatorField.getText().isEmpty() &
                            !balconiesField.getText().isEmpty() &
                            !terraceField.getText().isEmpty() &
                            !accessoriesField.getText().isEmpty() &
                            !bedroomsField.getText().isEmpty() &
                            !sqmField.getText().isEmpty() &
                            !priceField.getText().isEmpty();
                    break;

                case "Garage":
                    isGood &=
                        !addressField.getText().isEmpty() &
                            !sqmField.getText().isEmpty() &
                            !priceField.getText().isEmpty();
                    break;

                case "Independent":
                    isGood &=
                        !addressField.getText().isEmpty() &
                            !balconiesField.getText().isEmpty() &
                            !terraceField.getText().isEmpty() &
                            !gardenField.getText().isEmpty() &
                            !accessoriesField.getText().isEmpty() &
                            !bedroomsField.getText().isEmpty() &
                            !sqmField.getText().isEmpty() &
                            !priceField.getText().isEmpty();
                    break;

                default:
                    isGood = false;
                    System.out.println("No house type selected.");
                    break;
            }
        }

        if (!isGood) {
            errorLabel.setText("Fill in all required fields.");
            errorLabel.setStyle("-fx-text-fill: red");
            errorLabel.setVisible(true);
        } else {
            if (modifyHouse) {
                houseMemento = house.createMemento();

                switch (house.getType()) {
                    case APARTMENT:
                        house.setAddress(addressField.getText());
                        house.setFloor(Integer.parseInt(floorField.getText()));
                        house.setElevator(Boolean.parseBoolean(elevatorField.getText()));
                        house.setBalconies(Integer.parseInt(balconiesField.getText()));
                        house.setTerrace(Integer.parseInt(terraceField.getText()));
                        house.setAccessories(Integer.parseInt(accessoriesField.getText()));
                        house.setBedrooms(Integer.parseInt(bedroomsField.getText()));
                        house.setSqm(Integer.parseInt(sqmField.getText()));
                        house.setPrice(Integer.parseInt(priceField.getText()));
                        house.setDescription(descriptionField.getText());
                        house.setPics(pics);
                        break;

                    case GARAGE:
                        house.setAddress(addressField.getText());
                        house.setSqm(Integer.parseInt(sqmField.getText()));
                        house.setPrice(Integer.parseInt(priceField.getText()));
                        house.setDescription(descriptionField.getText());
                        house.setPics(pics);
                        break;

                    case INDEPENDENT:
                        house.setAddress(addressField.getText());
                        house.setBalconies(Integer.parseInt(balconiesField.getText()));
                        house.setTerrace(Integer.parseInt(terraceField.getText()));
                        house.setGarden(Integer.parseInt(gardenField.getText()));
                        house.setAccessories(Integer.parseInt(accessoriesField.getText()));
                        house.setBedrooms(Integer.parseInt(bedroomsField.getText()));
                        house.setSqm(Integer.parseInt(sqmField.getText()));
                        house.setPrice(Integer.parseInt(priceField.getText()));
                        house.setDescription(descriptionField.getText());
                        house.setPics(pics);
                        break;
                }
                Objects.requireNonNull(house);
                sendInsertHouseRequest(true);

                errorLabel.setText("Update successfully completed. Click 'Reset' to undo confirmed changes.");
                errorLabel.setStyle("-fx-text-fill: green");
                errorLabel.setVisible(true);
            } else {
                switch (houseTypeComboBox.getValue()) {
                    case "Apartment": {
                        final var builder = new ApartmentBuilder();
                        final var director = new HouseDirector(builder);
                        house = director.constructApartment(
                            -1, //The ID is assigned by the database
                            HouseType.APARTMENT,
                            addressField.getText(),
                            Integer.parseInt(floorField.getText()),
                            Boolean.parseBoolean(elevatorField.getText()),
                            Integer.parseInt(balconiesField.getText()),
                            Integer.parseInt(terraceField.getText()),
                            Integer.parseInt(accessoriesField.getText()),
                            Integer.parseInt(bedroomsField.getText()),
                            Integer.parseInt(sqmField.getText()),
                            Integer.parseInt(priceField.getText()),
                            descriptionField.getText(),
                            pics
                        );
                        break;
                    }

                    case "Garage": {
                        final var builder = new GarageBuilder();
                        final var director = new HouseDirector(builder);
                        house = director.constructGarage(
                            -1, //The ID is assigned by the database
                            HouseType.GARAGE,
                            addressField.getText(),
                            Integer.parseInt(sqmField.getText()),
                            Integer.parseInt(priceField.getText()),
                            descriptionField.getText(),
                            pics
                        );
                        break;
                    }

                    case "Independent": {
                        final var builder = new IndependentBuilder();
                        final var director = new HouseDirector(builder);
                        house = director.constructIndependent(
                            -1, //The ID is assigned by the database
                            HouseType.INDEPENDENT,
                            addressField.getText(),
                            Integer.parseInt(balconiesField.getText()),
                            Integer.parseInt(terraceField.getText()),
                            Integer.parseInt(accessoriesField.getText()),
                            Integer.parseInt(bedroomsField.getText()),
                            Integer.parseInt(sqmField.getText()),
                            Integer.parseInt(priceField.getText()),
                            descriptionField.getText(),
                            pics
                        );
                        break;
                    }
                }
                Objects.requireNonNull(house);
                sendInsertHouseRequest(false);
                try {
                    homeButton(null);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    /**
     * Resets the form fields and loaded images.
     *
     * <p> If the house is being modified, restores the previous state of the house
     * and updates the form fields accordingly. Otherwise, clears all form fields
     * and image slots.
     *
     * @param event The ActionEvent triggered by the user clicking the reset button.
     */
    @FXML
    void resetButton(ActionEvent event) {
        if (modifyHouse) {
            if (houseMemento != null) {
                houseMemento.restoreState();
                setModifyData(Objects.requireNonNull(house));
                sendInsertHouseRequest(true);

                errorLabel.setText("Reset successfully completed. Click 'Confirm' to confirm changes.");
                errorLabel.setStyle("-fx-text-fill: green");
                errorLabel.setVisible(true);
            } else {
                System.out.println("No stored edits.");
            }
        } else {
            picsFile = new File[3];
            nameImg1.setStyle("-fx-text-fill: red");
            nameImg1.setText("No file selected yet.");
            nameImg2.setStyle("-fx-text-fill: red");
            nameImg2.setText("No file selected yet.");
            nameImg3.setStyle("-fx-text-fill: red");
            nameImg3.setText("No file selected yet.");

            houseTypeComboBox.setValue("");
            addressField.clear();
            priceField.clear();

            descriptionField.clear();
        }
    }

    /**
     * Handles the action when the delete button is clicked.
     *
     * <p> Opens a confirmation dialog window to confirm deletion. If confirmed,
     * sends a delete request for the current house to the server and navigates
     * back to the home screen.
     *
     * @param event The ActionEvent triggered by the user clicking the delete button.
     * @throws Exception if there is an issue with loading the confirmation popup FXML.
     */
    @FXML
    void deleteButtonAction(ActionEvent event) throws
        Exception {
        // The view change isn't implemented through the Command pattern because data is dynamically assigned
        final var primaryStage = StageSingleton.getInstance().getPrimaryStage();
        final var fxmlLoader = new FXMLLoader(StartApplication.class.getResource("popupConfirm.fxml"));
        final var newScene = new Scene(fxmlLoader.load());
        final var newStage = new Stage();
        newStage.setScene(newScene);
        newStage.setResizable(false);
        newStage.setTitle("Confirm Cancellation");
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.initOwner(primaryStage);
        newStage.showAndWait();
        final var popupConfirmController = fxmlLoader.<PopupConfirmController>getController();
        if (popupConfirmController.getResult()) {
            final var query = new JSONObject(
                String.format(
                    """
                        {
                          "type": "poster",
                          "data": {
                            "request": "deleteHouse",
                            "parameters": {
                              "id": %d
                            }
                          }
                        }
                    """,
                    house.getId()
                )
            );
            ClientConnection
                .getInstance()
                .getClient()
                .exchange(query);

            homeButton(null);
        }
    }

    /**
     * Disables specific fields based on the selected house type.
     */
    @FXML
    private void blockFields() {
        // Reset key features fields to enable all fields by default
        resetKeyFeaturesFields();

        // Check the selected house type from the ComboBox
        switch (houseTypeComboBox.getValue()) {
            case "Apartment":
                // Disable garden field for apartments
                gardenField.setDisable(true);
                break;

            case "Garage":
                // Disable all fields for garages
                floorField.setDisable(true);
                elevatorField.setDisable(true);
                balconiesField.setDisable(true);
                terraceField.setDisable(true);
                gardenField.setDisable(true);
                accessoriesField.setDisable(true);
                bedroomsField.setDisable(true);
                break;

            case "Independent":
                // Disable floor and elevator fields for independent houses
                floorField.setDisable(true);
                elevatorField.setDisable(true);
                break;

            default:
                // If no house type is selected, display a message
                System.out.println("No house type selected.");
                break;
        }
    }

    /**
     * Resets and enables all key features fields.
     */
    private void resetKeyFeaturesFields() {
        // Clear all key features fields
        floorField.clear();
        elevatorField.clear();
        balconiesField.clear();
        terraceField.clear();
        gardenField.clear();
        accessoriesField.clear();
        bedroomsField.clear();
        sqmField.clear();

        // Enable all key features fields
        floorField.setDisable(false);
        elevatorField.setDisable(false);
        balconiesField.setDisable(false);
        terraceField.setDisable(false);
        gardenField.setDisable(false);
        accessoriesField.setDisable(false);
        bedroomsField.setDisable(false);
    }

    /**
     * Initializes the controller class.
     * <p>
     * This method is automatically called by the FXMLLoader when the FXML file is loaded.
     * It initializes the UI components and sets up necessary objects.
     *
     * @param url            The location used to resolve relative paths for the root object, or null if the location
     *                       is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not
     *                       localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set the user's name in the greeting label
        nameUser.setText("Hi, " + UserAccess.getUser().getLastName());

        // Populate the house type combo box with available options
        houseTypeComboBox.getItems().setAll(houseType);

        // Initialize Command pattern objects
        buttonReceiver = new ButtonReceiver(StageSingleton.getInstance().getPrimaryStage());
        goMainViewCommand = new GoMainViewCommand(buttonReceiver);
        goLoginViewCommand = new GoLoginViewCommand(buttonReceiver);
        invoker = new Invoker();

        // Add extension filter to file chooser for PNG files
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PNG Files", "*.png"));
    }

    /**
     * Sends an insert house request to the server.
     *
     * @param update Indicates whether this is an update request or not.
     */
    private void sendInsertHouseRequest(boolean update) {
        // Construct the JSON message for the insert house request
        final var message = new JSONObject(
            String.format(
                """
                    {
                      "type": "poster",
                      "data": {
                        "request": "insertHouse",
                        "parameters": {
                          "id": %d,
                          "type": %d,
                          "address": "%s",
                          "floor": %d,
                          "elevator": %b,
                          "balconies": %d,
                          "terrace": %d,
                          "garden": %d,
                          "accessories": %d,
                          "bedrooms": %d,
                          "sqm": %d,
                          "price": %d,
                          "description": "%s",
                          "pictures": [],
                        }
                      }
                    }
                """,
                update ? house.getId() : null,
                house.getType().getValue(),
                house.getAddress(),
                house.getFloor(),
                house.hasElevator(),
                house.getBalconies(),
                house.getTerrace(),
                house.getGarden(),
                house.getAccessories(),
                house.getBedrooms(),
                house.getSqm(),
                house.getPrice(),
                house.getDescription()
            )
        );

        // Prepare picture data
        final var pictures = new Image[3];
        for (var i = 0; i < pictures.length; i++) {
            pictures[i] = house.getPics(i);
        }
        for (final var pic : pictures) {
            String data = null;
            if (pic != null) {
                try {
                    byte[] bytes;
                    if (pic.getUrl() == null) {
                        final var buffer = new ByteArrayOutputStream();
                        try (final var writer = new PNGWriter(buffer)) {
                            final var imageBytes = getBytesFromImage(pic);
                            writer.write(new ArgbImage() {
                                @Override
                                public int getWidth() {
                                    return (int)pic.getWidth();
                                }

                                @Override
                                public int getHeight() {
                                    return (int)pic.getHeight();
                                }

                                @Override
                                public int getArgb(int x, int y) {
                                    final var index = (y * getWidth() + x) * 4;
                                    final var b = imageBytes[index] & 0xFF;
                                    final var g = imageBytes[index + 1] & 0xFF;
                                    final var r = imageBytes[index + 2] & 0xFF;
                                    final var a = imageBytes[index + 3] & 0xFF;
                                    return (a << 24) | (r << 16) | (g << 8) | b;
                                }
                            });
                        }
                        bytes = Base64
                            .getEncoder()
                            .encode(buffer.toByteArray());
                    } else {
                        bytes = Base64
                            .getEncoder()
                            .encode(
                                Files.readAllBytes(Paths.get(new URI(pic.getUrl())))
                            );
                    }
                    data = new String(bytes);
                } catch (IOException | URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
            // Add picture data to the JSON message
            message
                .getJSONObject("data")
                .getJSONObject("parameters")
                .getJSONArray("pictures")
                .put(Objects.requireNonNullElse(data, JSONObject.NULL));
        }

        // Send the insert house request to the server
        final var response = ClientConnection
            .getInstance()
            .getClient()
            .exchange(message);

        // If it's a new entry and the response contains an ID, set the ID for the house
        if (!update && !response.isNull("response")) {
            house.setId(response.getJSONObject("response").getInt("id"));
        }
    }

    /**
     * Converts an Image object to a byte array.
     *
     * @param image The Image object to convert.
     * @return A byte array representing the image data.
     */
    private byte[] getBytesFromImage(Image image) {
        // Get the width and height of the image
        final var width = (int)image.getWidth();
        final var height = (int)image.getHeight();

        // Allocate a ByteBuffer to hold the image data
        final var buffer = ByteBuffer.allocate(width * height * 4);

        // Get the pixel data from the image and store it in the ByteBuffer
        image
            .getPixelReader()
            .getPixels(0, 0, width, height, PixelFormat.getByteBgraInstance(), buffer, width * 4);

        // Return the byte array representation of the image data
        return buffer.array();
    }

    /**
     * Handles the action when the logout button is clicked.
     *
     * <p> Uses the Command pattern to execute the action of switching to the login view.
     *
     * @param event The ActionEvent triggered by the user clicking the logout button.
     */
    @FXML
    void logoutButton(ActionEvent event) {
        invoker.placeCommand(goLoginViewCommand);
    }

    /**
     * Handles the action when the home button is clicked.
     *
     * <p>Uses the Command pattern to execute the action of switching to the main view.
     *
     * @param event The ActionEvent triggered by the user clicking the home button.
     */
    @FXML
    void homeButton(ActionEvent event) {
        // Execute the command to switch to the main view
        invoker.placeCommand(goMainViewCommand);
    }
}