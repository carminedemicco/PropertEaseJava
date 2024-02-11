package main.propertease;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Controller class for managing houses.fxml: the view dedicated to the house card.
 */
public class HousesController implements Initializable {
    private int id;

    @FXML
    private ImageView img;

    @FXML
    private Label name;

    @FXML
    private Label address1;

    @FXML
    private Label address2;

    @FXML
    private Label price;

    @FXML
    private AnchorPane anchorPane;

    private House house;

    /**
     * Updates the values of the view fields with data from a House object.
     *
     * @param house The House object containing the data to be displayed.
     */
    public void setData(House house) {
        this.house = house;

        id = house.getId();
        name.setText(house.getName());
        price.setText("$ " + house.getPrice());
        // Split the address string using the "|" character
        final var addressSplit = house.getAddress().split("\\|");
        address1.setText(addressSplit[0]);
        if (addressSplit.length > 1) {
            address2.setText(addressSplit[1]);
        }

        var pic = house.getPics(0);
        if (pic == null) {
            final var defaultResource = Objects.requireNonNull(getClass().getResourceAsStream("/main/propertease/img" +
                                                                                                      "/icons" +
                                                                                                      "/placeholder" +
                                                                                                      ".png"));
            pic = new Image(defaultResource);
        }
        img.setImage(pic);
        // Set image properties
        img.setPreserveRatio(false); // Adjusts to size
        // Round the corners
        final var roundedRectangle = new Rectangle(img.getFitWidth(), img.getFitHeight());
        roundedRectangle.setArcWidth(25);
        roundedRectangle.setArcHeight(25);
        img.setClip(roundedRectangle);
    }

    /**
     * Initializes the controller.
     * Handles the action when a house card is clicked to show its details.
     *
     * @param url            The location used to resolve relative paths for the root object, or null if the location
     *                      is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not
     *                       localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Click on a house card -> show the View with its details
        anchorPane.setOnMouseClicked(event -> {
            // Screen change instructions
            try {
                // The view change isn't implemented through the Command pattern because data is dynamically assigned
                final var fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("houseDetailsView.fxml"));
                final var stage = StageSingleton.getInstance().getPrimaryStage();
                final var scene = new Scene(fxmlLoader.load());

                // Set instructions for the new screen
                final var houseDetailsController = fxmlLoader.<HouseDetailsController>getController();
                houseDetailsController.setData(house);

                stage.setScene(scene);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}