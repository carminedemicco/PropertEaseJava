package main.propertease;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.propertease.builder.House;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

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
    private VBox detailbox1;

    @FXML
    private VBox detailbox11;

    @FXML
    private VBox detailbox3;

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
    private ComboBox<String> houseTypeComboBox;
    private final String[] houseType = {"Apartment", "Garage", "Independent"};


    // Al click del bottone di logout: ritorna alla View di login
    @FXML
    void logoutButton(ActionEvent event) throws Exception {
        // Operazioni di logout
        //...

        final var stage = (Stage)detailbox1.getScene().getWindow();
        final var fxmlLoader = new FXMLLoader(StartApplication.class.getResource("loginView.fxml"));
        final var scene = new Scene(fxmlLoader.load());
        stage.hide();
        stage.setScene(scene);
        stage.show();
    }


    // Al click del bottone home: ritorna alla View generale mainView.fxml
    @FXML
    void homeButton(ActionEvent event) throws Exception {
        final var stage = (Stage)detailbox1.getScene().getWindow();
        final var fxmlLoader = new FXMLLoader(StartApplication.class.getResource("mainView.fxml"));
        final var scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
    }


    File[] pics = new File[3];

    // l'utente sceglie l'immagine 1 da caricare
    @FXML
    void addImg1(ActionEvent event) {
        final var fc = new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));

        pics[0] = fc.showOpenDialog(null);

        if (pics[0] != null) {
            nameImg1.setStyle("-fx-text-fill: #f0f8ff");
            nameImg1.setText(pics[0].getName());
        }
    }

    // l'utente sceglie l'immagine 2 da caricare
    @FXML
    void addImg2(ActionEvent event) {
        final var fc = new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        pics[1] = fc.showOpenDialog(null);

        if (pics[1] != null) {
            nameImg2.setStyle("-fx-text-fill: #f0f8ff");
            nameImg2.setText(pics[1].getName());
        }
    }

    // l'utente sceglie l'immagine 3 da caricare
    @FXML
    void addImg3(ActionEvent event) {
        final var fc = new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        pics[2] = fc.showOpenDialog(null);

        if (pics[2] != null) {
            nameImg3.setStyle("-fx-text-fill: #f0f8ff");
            nameImg3.setText(pics[2].getName());
        }
    }

    // Al click del bottone di conferma inserisce la nuova casa nel database //TODO aggiungere price
    @FXML
    void confirmButton(ActionEvent event) {
        boolean notGood = false;

        if(houseTypeComboBox.getValue() == null)
        {
            System.out.println("No house type selected.");
        }
        else
        {
            switch (houseTypeComboBox.getValue()) {
                case "Apartment":
                    notGood |= addressField.getText().isEmpty() |
                            floorField.getText().isEmpty() |
                            elevatorField.getText().isEmpty() |
                            balconiesField.getText().isEmpty() |
                            terraceField.getText().isEmpty() |
                            accessoriesField.getText().isEmpty() |
                            bedroomsField.getText().isEmpty() |
                            sqmField.getText().isEmpty();
                    break;

                case "Garage":
                    notGood |= addressField.getText().isEmpty() |
                            sqmField.getText().isEmpty();
                    break;

                case "Independent":
                    notGood |= addressField.getText().isEmpty() |
                            balconiesField.getText().isEmpty() |
                            terraceField.getText().isEmpty() |
                            gardenField.getText().isEmpty() |
                            accessoriesField.getText().isEmpty() |
                            bedroomsField.getText().isEmpty() |
                            sqmField.getText().isEmpty();
                    break;

                default:
                    System.out.println("No house type selected.");
                    break;
            }
        }

        if(notGood){
            errorLabel.setVisible(true);
        }
        //TODO inserire tutta la logica di inserimento nel database
        // ...
    }

    // Al click del bottone di reset vengono puliti tutti i campi
    @FXML
    void resetButton(ActionEvent event) {
        pics = new File[3];
        nameImg1.setStyle("-fx-text-fill: red");
        nameImg1.setText("No file selected yet.");
        nameImg2.setStyle("-fx-text-fill: red");
        nameImg2.setText("No file selected yet.");
        nameImg3.setStyle("-fx-text-fill: red");
        nameImg3.setText("No file selected yet.");

        houseTypeComboBox.setValue("");

        descriptionField.clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameUser.setText("Hi, " + UserAccess.getUser().getLastName());
        // aggiunge gli elementi della ComboBox
        houseTypeComboBox.getItems().setAll(houseType);
        errorLabel.setVisible(false);
    }

    // function che si attiva ogni volta che cambia il valore della combobox(anche quando faccio reset)
    // alla selezione del tipo di casa blocca i campi non compilabili
    @FXML
    private void blockFields() {
        resetKeyFeaturesFields();
        switch (houseTypeComboBox.getValue()) {
            case "Apartment":
                gardenField.setDisable(true);

                break;

            case "Garage":
                floorField.setDisable(true);
                elevatorField.setDisable(true);
                balconiesField.setDisable(true);
                terraceField.setDisable(true);
                gardenField.setDisable(true);
                accessoriesField.setDisable(true);
                bedroomsField.setDisable(true);

                break;

            case "Independent":
                floorField.setDisable(true);
                elevatorField.setDisable(true);
                break;

            default:
                System.out.println("No house type selected.");
                break;
        }

    }

    private void resetKeyFeaturesFields() {
        addressField.clear();
        floorField.clear();
        elevatorField.clear();
        balconiesField.clear();
        terraceField.clear();
        gardenField.clear();
        accessoriesField.clear();
        bedroomsField.clear();
        sqmField.clear();

        floorField.setDisable(false);
        elevatorField.setDisable(false);
        balconiesField.setDisable(false);
        terraceField.setDisable(false);
        gardenField.setDisable(false);
        accessoriesField.setDisable(false);
        bedroomsField.setDisable(false);
    }


}