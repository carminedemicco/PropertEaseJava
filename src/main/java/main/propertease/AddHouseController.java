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
    private ComboBox<String> houseTypeComboBox;
    private final String[] houseType = {"Apartment", "Garage", "Independent"};

    private House house;

    // Al click del bottone di logout: ritorna alla View di login
    @FXML
    void logoutButton(ActionEvent event) throws Exception {
        // Operazioni di logout
        //...

        Stage stage = (Stage) detailbox1.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("loginView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.hide();
        stage.setScene(scene);
        stage.show();
    }


    // Al click del bottone home: ritorna alla View generale mainView.fxml
    @FXML
    void homeButton(ActionEvent event) throws Exception {
        Stage stage = (Stage) detailbox1.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("mainView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
    }


    File[] pics = new File[3];

    // l'utente sceglie l'immagine 1 da caricare
    @FXML
    void addImg1(ActionEvent event) {
        FileChooser fc = new FileChooser();
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
        FileChooser fc = new FileChooser();
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
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        pics[2] = fc.showOpenDialog(null);

        if (pics[2] != null) {
            nameImg3.setStyle("-fx-text-fill: #f0f8ff");
            nameImg3.setText(pics[2].getName());
        }
    }

    // Al click del bottone di conferma inserisce la nuova casa nel database
    @FXML
    void confirmButton(ActionEvent event) {
        /* NB: inserire tutta la logica di inserimento nel database */
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

    }

    // function che si attiva ogni volta che cambia il valore della combobox(anche quando faccio reset)
    // alla selezione del tipo di casa blocca i campi non compilabili
    @FXML
    private void blockFields(){
        resetKeyFeaturesFields();
        switch (houseTypeComboBox.getValue()){
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

    private void resetKeyFeaturesFields(){
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

    public void setData(House house) {
        this.house = house;


    }
}