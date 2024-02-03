package main.propertease;

import main.propertease.builder.House;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

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


    // Aggiornamento del valore dei campi della View
    // Prende in input un oggetto di classe House
    public void setData(House house) {
        id = house.getId();
        name.setText(house.getName());
        price.setText("$ " + house.getPrice());
        address1.setText(house.getAddress());
        address2.setText(house.getAddress());

        //Proprietà dell'immagine
        final var image = house.getPics(0);
        img.setPreserveRatio(false); //si adatta alla dimensione
        //smusso gli angoli
        Rectangle roundedRectangle = new Rectangle(img.getFitWidth(), img.getFitHeight());
        roundedRectangle.setArcWidth(25);
        roundedRectangle.setArcHeight(25);
        img.setClip(roundedRectangle);

        img.setImage(image);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Click su una casa -> mostra la View con i suoi dettagli
        anchorPane.setOnMouseClicked(event -> {
            System.out.println("AnchorPane Clicked " + id);

            // Istruzioni di set per la nuova schermata
            // ...

            // Istruzioni di cambio schermata
            Stage stage = (Stage)anchorPane.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("houseDetailsView.fxml"));
            Scene scene;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.setScene(scene);
        });

    }


}
