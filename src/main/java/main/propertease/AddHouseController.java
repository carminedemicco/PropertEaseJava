package main.propertease;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
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

    // Variabile bool che contiene true se la casa deve essere modificata
    Boolean modifyHouse = false;

    House house;

    public void setModifyData(House house){
        modifyHouse = true;

        this.house = house;

        String type = null;
        switch (house.getType()){
            case APARTMENT -> type = "Apartment";
            case GARAGE -> type = "Garage";
            case INDEPENDENT -> type = "Independent";
        }

        // Precompila i campi per la modifica
        nameImg1.setStyle("-fx-text-fill: #f0f8ff");
        nameImg1.setText("img1.png");
        nameImg2.setStyle("-fx-text-fill: #f0f8ff");
        nameImg2.setText("img2.png");
        nameImg3.setStyle("-fx-text-fill: #f0f8ff");
        nameImg3.setText("img3.png");
        houseTypeComboBox.setValue(type);
        addressField.setText(house.getAddress());
        floorField.setText(String.valueOf(house.getFloor()));
        elevatorField.setText(String.valueOf(house.hasElevator()));
        balconiesField.setText(String.valueOf(house.getBalconies()));
        terraceField.setText(String.valueOf(house.getTerrace()));
        gardenField.setText(String.valueOf(house.getGarden()));
        accessoriesField.setText(String.valueOf(house.getAccessories()));
        bedroomsField.setText(String.valueOf(house.getBedrooms()));
        sqmField.setText(String.valueOf(house.getSqm()));
        priceField.setText(String.valueOf(house.getPrice()));
        descriptionField.setText(house.getDescription());

        // Si rende visibile il bottone di cancellazione casa
        deleteButton.setVisible(true);
    }


    // Al click del bottone di logout: ritorna alla View di login
    @FXML
    void logoutButton(ActionEvent event) throws Exception {
        // Operazioni di logout
        //...

        final Stage stage = (Stage)addressField.getScene().getWindow();
        final FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("loginView.fxml"));
        final Scene scene = new Scene(fxmlLoader.load());
        stage.hide();
        stage.setScene(scene);
        stage.show();
    }


    // Al click del bottone home: ritorna alla View generale mainView.fxml
    @FXML
    void homeButton(ActionEvent event) throws Exception {
        final var stage = (Stage)addressField.getScene().getWindow();
        final var fxmlLoader = new FXMLLoader(StartApplication.class.getResource("mainView.fxml"));
        final var scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
    }


    File[] pics = new File[3];

    // l'utente sceglie l'immagine 1 da caricare
    final FileChooser fc = new FileChooser();
    @FXML
    void addImg1(ActionEvent event) {
        pics[0] = fc.showOpenDialog(null);

        if (pics[0] != null) {
            nameImg1.setStyle("-fx-text-fill: #f0f8ff");
            nameImg1.setText(pics[0].getName());
        }
    }

    // l'utente sceglie l'immagine 2 da caricare
    @FXML
    void addImg2(ActionEvent event) {
        pics[1] = fc.showOpenDialog(null);

        if (pics[1] != null) {
            nameImg2.setStyle("-fx-text-fill: #f0f8ff");
            nameImg2.setText(pics[1].getName());
        }
    }

    // l'utente sceglie l'immagine 3 da caricare
    @FXML
    void addImg3(ActionEvent event) {
        pics[2] = fc.showOpenDialog(null);

        if (pics[2] != null) {
            nameImg3.setStyle("-fx-text-fill: #f0f8ff");
            nameImg3.setText(pics[2].getName());
        }
    }

    // Al click del bottone di conferma inserisce la nuova casa nel database
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
                            sqmField.getText().isEmpty() |
                            priceField.getText().isEmpty();
                    break;

                case "Garage":
                    notGood |= addressField.getText().isEmpty() |
                            sqmField.getText().isEmpty()|
                            priceField.getText().isEmpty();
                    break;

                case "Independent":
                    notGood |= addressField.getText().isEmpty() |
                            balconiesField.getText().isEmpty() |
                            terraceField.getText().isEmpty() |
                            gardenField.getText().isEmpty() |
                            accessoriesField.getText().isEmpty() |
                            bedroomsField.getText().isEmpty() |
                            sqmField.getText().isEmpty()|
                            priceField.getText().isEmpty();
                    break;

                default:
                    System.out.println("No house type selected.");
                    break;
            }
        }

        if(notGood){
            errorLabel.setVisible(true);
        }

        if(modifyHouse){
            //TODO inserire tutta la logica di modica nel database
            // ...
        }else {
            //TODO inserire tutta la logica di inserimento nel database
            // ...
        }
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

    @FXML
    void deleteButtonAction(ActionEvent event) throws Exception{
        // Apri il popup di conferma
        Stage primaryStage = (Stage)addressField.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("popupConfirm.fxml"));
        Scene newScene = new Scene(fxmlLoader.load());
        Stage newStage = new Stage();
        newStage.setScene(newScene);
        newStage.setResizable(false);
        newStage.setTitle("Confirm Cancellation");
        // Blocca l'interazione con le altre finestre finché la finestra appena aperta non viene chiusa.
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.initOwner(primaryStage);
        // mostra il popup e blocca l'esecuzione fino a quando lo Stage secondario non viene chiuso
        newStage.showAndWait();
        // Prendo il controller del popup
        PopupConfirmController popupConfirmController = fxmlLoader.getController();
        // Prendo il valore di ritorno del popup dal controller
        if (popupConfirmController.getResult()) {
            // TODO rimozione del database della casa
            //...

            // dopo aver inserito ritorna alla schermata principale
            homeButton(null);
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameUser.setText("Hi, " + UserAccess.getUser().getLastName());
        // aggiunge gli elementi della ComboBox
        houseTypeComboBox.getItems().setAll(houseType);

        // Definisco le estensioni accettate del FileChooser
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));

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