package main.propertease;

import javafx.scene.image.Image;
import main.propertease.builder.House;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

// Classe che gestisce houses.fxml: la view dedicata alla scheda delle case
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

    // Aggiornamento del valore dei campi della View
    // Prende in input un oggetto di classe House
    public void setData(House house) {
        this.house = house;

        id = house.getId();
        name.setText(house.getName());
        price.setText("$ " + house.getPrice());
        // Divido la stringa utilizzando il carattere "|"
        final var addressSplit = house.getAddress().split("\\|");
        address1.setText(addressSplit[0]);
        if (addressSplit.length > 1) {
            address2.setText(addressSplit[1]);
        }

        var pic = house.getPics(0);
        if (pic == null) {
            final var defaultResource = Objects.requireNonNull(getClass().getResourceAsStream("/main/propertease/img/icons/placeholder.png"));
            pic = new Image(defaultResource);
        }
        img.setImage(pic);
        //Proprietà dell'immagine
        img.setPreserveRatio(false); //si adatta alla dimensione
        //smusso gli angoli
        final var roundedRectangle = new Rectangle(img.getFitWidth(), img.getFitHeight());
        roundedRectangle.setArcWidth(25);
        roundedRectangle.setArcHeight(25);
        img.setClip(roundedRectangle);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Click su una casa -> mostra la View con i suoi dettagli
        anchorPane.setOnMouseClicked(event -> {
            // Istruzioni di cambio schermata
            try {
                // Il cambio schermata non è implementato con il command perché i dati sono impostati dinamicamente
                final var fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("houseDetailsView.fxml"));
                final var stage = StageSingleton.getInstance().getPrimaryStage();
                final var scene = new Scene(fxmlLoader.load());

                // Istruzioni di set per la nuova schermata
                final var houseDetailsController = fxmlLoader.<HouseDetailsController>getController();
                houseDetailsController.setData(house);

                stage.setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
