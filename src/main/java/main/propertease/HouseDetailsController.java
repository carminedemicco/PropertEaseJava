package main.propertease;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.propertease.command.ButtonReceiver;
import main.propertease.command.GoLoginViewCommand;
import main.propertease.command.GoMainViewCommand;
import main.propertease.command.Invoker;
import main.propertease.decorator.HouseInterface;
import main.propertease.decorator.HouseVat;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Controller class for managing houseDetails.fxml: the view dedicated to the details of a house.
 */
public class HouseDetailsController implements Initializable {
    @FXML
    private Label addressLabel;

    @FXML
    private Label accessoriesLabel;

    @FXML
    private Label balconiesLabel;

    @FXML
    private Label bedroomsLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private VBox detailbox1;

    @FXML
    private VBox detailbox2;

    @FXML
    private VBox detailbox3;

    @FXML
    private Button vatButtonId;

    @FXML
    private Label elevatorLabel;

    @FXML
    private Label floorLabel;

    @FXML
    private Label gardenLabel;

    @FXML
    private ImageView img1;

    @FXML
    private ImageView img2;

    @FXML
    private ImageView img3;

    @FXML
    private Label marketedbyLabel;

    @FXML
    private Label sqmLabel;

    @FXML
    private Label terraceLabel;

    @FXML
    private Label priceLabel;

    @FXML
    private Label usernameLabel;

    @FXML
    private HBox adminButtonsArea;

    @FXML
    private Label nameUser;

    @FXML
    private Button makeAppButton;

    // Pattern Command
    ButtonReceiver buttonReceiver;
    GoMainViewCommand goMainViewCommand;
    GoLoginViewCommand goLoginViewCommand;
    Invoker invoker;

    private House house;

    /**
     * Handles the action when the logout button is clicked.
     * It uses the Command pattern.
     *
     * @param event The ActionEvent triggered by the user clicking the logout button.
     */
    @FXML
    void logoutButton(ActionEvent event) {
        // Pattern Command
        invoker.placeCommand(goLoginViewCommand);
    }

    /**
     * Handles the action when the home button is clicked.
     * It uses the Command pattern.
     *
     * @param event The ActionEvent triggered by the user clicking the home button.
     */
    @FXML
    void homeButton(ActionEvent event) {
        // Pattern Command
        invoker.placeCommand(goMainViewCommand);
    }

    /**
     * Initializes the controller.
     * It sets up the user interface elements and applies effects.
     *
     * @param url            The location used to resolve relative paths for the root object, or null if the location
     *                       is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not
     *                       localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameUser.setText("Hi, " + UserAccess.getUser().getLastName());

        // Pattern Command
        buttonReceiver = new ButtonReceiver(StageSingleton.getInstance().getPrimaryStage());
        goMainViewCommand = new GoMainViewCommand(buttonReceiver);
        goLoginViewCommand = new GoLoginViewCommand(buttonReceiver);
        invoker = new Invoker();

        // Set the images to have rounded corners
        img1.setPreserveRatio(false); // Adjusts to size
        var roundedRectangle = new Rectangle(img1.getFitWidth(), img1.getFitHeight());
        roundedRectangle.setArcWidth(25);
        roundedRectangle.setArcHeight(25);
        img1.setClip(roundedRectangle);

        img2.setPreserveRatio(false);
        roundedRectangle = new Rectangle(img2.getFitWidth(), img2.getFitHeight());
        roundedRectangle.setArcWidth(25);
        roundedRectangle.setArcHeight(25);
        img2.setClip(roundedRectangle);

        img3.setPreserveRatio(false);
        roundedRectangle = new Rectangle(img3.getFitWidth(), img3.getFitHeight());
        roundedRectangle.setArcWidth(25);
        roundedRectangle.setArcHeight(25);
        img3.setClip(roundedRectangle);

        // Apply drop shadow effect to the detail boxes
        detailbox1.setEffect(new DropShadow(20, Color.BLACK));
        detailbox2.setEffect(new DropShadow(20, Color.BLACK));
        detailbox3.setEffect(new DropShadow(20, Color.BLACK));

        if (UserAccess.getUser().getPrivileges() == 1) {
            addAdminButtons();
            makeAppButton.setVisible(false);
        }
    }

    /**
     * Adds admin buttons to edit the house details.
     */
    private void addAdminButtons() {
        final var btn1 = new Button();
        btn1.getStyleClass().add("modify-house-button");
        adminButtonsArea.getChildren().add(btn1);

        btn1.setOnAction(e -> {
            try {
                // The view change isn't implemented through the Command pattern because data is dynamically assigned
                final var stage = StageSingleton.getInstance().getPrimaryStage();
                final var fxmlLoader = new FXMLLoader(StartApplication.class.getResource("addHouseView.fxml"));
                final var scene = new Scene(fxmlLoader.load());
                final var addHouseController = fxmlLoader.<AddHouseController>getController();
                addHouseController.setModifyData(house);
                stage.setScene(scene);
            }
            catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

    }

    Boolean vatCalc = false; //Declared here to avoid reinitialization

    /**
     * Handles the action when the VAT button is clicked.
     * It uses the Decorator pattern to calculate the price with or without VAT.
     *
     * @param event The ActionEvent triggered by the user clicking the VAT button.
     */
    @FXML
    void vatButton(ActionEvent event) {
        if (!vatCalc) {
            // Use the Decorator pattern to extend the house object at run-time
            HouseInterface vatHouse = new HouseVat(house);
            // Get and set the new price with VAT removed
            priceLabel.setText(String.valueOf(vatHouse.getPrice()));
            // Change the button text to perform the reverse operation
            vatButtonId.setText("Price with VAT");
            vatCalc = true;
        }
        else {
            // Get and set the price with VAT included
            priceLabel.setText(String.valueOf(house.getPrice()));
            // Change the button text to perform the reverse operation
            vatButtonId.setText("Price without VAT");
            vatCalc = false;
        }
    }

    /**
     * Handles the action when the 'Make an Appointment' button is clicked.
     * It opens a window for selecting the appointment date.
     *
     * @param event The ActionEvent triggered by the user clicking the 'Make an Appointment' button.
     * @throws IOException If an I/O error occurs when loading the popupSelectDate.fxml file.
     */
    @FXML
    void makeAppointmentButton(ActionEvent event) throws
                                                  IOException {
        final var primaryStage = (Stage) detailbox1.getScene().getWindow();

        // Create the new scene
        final var newStage = new Stage();
        final var fxmlLoader = new FXMLLoader(StartApplication.class.getResource("popupSelectDate.fxml"));
        final var newScene = new Scene(fxmlLoader.load());
        final var selectDataController = fxmlLoader.<SelectDateController>getController();
        selectDataController.setData(house.getId());
        newStage.setScene(newScene);

        // Block interaction with other windows until the newly opened window is closed.
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.initOwner(primaryStage);

        // Show the new window
        newStage.setResizable(false);
        newStage.setTitle("Make an Appointment");
        newStage.show();
    }

    /**
     * Sets the data of the house to be displayed in the view.
     *
     * @param house The house object containing the details to be displayed.
     */
    public void setData(House house) {
        this.house = house;

        img1.setImage(Objects.requireNonNullElse(
                house.getPics(0),
                new Image(Objects.requireNonNull(
                        getClass()
                                .getResourceAsStream("/main/propertease/img/icons/placeholder.png")
                ))
        ));
        img2.setImage(Objects.requireNonNullElse(
                house.getPics(1),
                new Image(Objects.requireNonNull(
                        getClass()
                                .getResourceAsStream("/main/propertease/img/icons/placeholder.png")
                ))
        ));
        img3.setImage(Objects.requireNonNullElse(
                house.getPics(2),
                new Image(Objects.requireNonNull(
                        getClass()
                                .getResourceAsStream("/main/propertease/img/icons/placeholder.png")
                ))
        ));
        addressLabel.setText(house.getAddress().replace("|", ", "));
        floorLabel.setText(String.valueOf(house.getFloor()));
        elevatorLabel.setText(house.hasElevator() ? "Yes" : "No");
        balconiesLabel.setText(String.valueOf(house.getBalconies()));
        terraceLabel.setText(String.valueOf(house.getTerrace()));
        gardenLabel.setText(String.valueOf(house.getGarden()));
        accessoriesLabel.setText(String.valueOf(house.getAccessories()));
        bedroomsLabel.setText(String.valueOf(house.getBedrooms()));
        sqmLabel.setText(String.valueOf(house.getSqm()));
        priceLabel.setText(String.valueOf(house.getPrice()));
        descriptionLabel.setText(house.getDescription());
    }
}