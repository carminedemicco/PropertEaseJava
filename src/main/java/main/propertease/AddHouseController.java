package main.propertease;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.propertease.builder.*;
import main.propertease.command.ButtonReceiver;
import main.propertease.command.GoLoginViewCommand;
import main.propertease.command.GoMainViewCommand;
import main.propertease.command.Invoker;
import main.propertease.memento.Memento;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
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

    // Variabile bool che contiene true se la casa deve essere modificata
    Boolean modifyHouse = false;

    House house;
    Memento houseMemento;

    public void setModifyData(House house) {
        modifyHouse = true;

        this.house = house;

        // Precompila i campi per la modifica
        nameImg1.setStyle("-fx-text-fill: #f0f8ff");
        nameImg1.setText("img1.png");
        nameImg2.setStyle("-fx-text-fill: #f0f8ff");
        nameImg2.setText("img2.png");
        nameImg3.setStyle("-fx-text-fill: #f0f8ff");
        nameImg3.setText("img3.png");
        houseTypeComboBox.setValue(house.getType().toString());
        // Durante la modifica non è possibile modificare il tipo di casa
        houseTypeComboBox.setDisable(true);

        addressField.setText(house.getAddress());

        // Disabilito i Fields
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

        // Si rende visibile il bottone di cancellazione casa
        deleteButton.setVisible(true);
    }


    File[] picsFile = new File[3];
    Image[] pics = new Image[3];

    // l'utente sceglie l'immagine 1 da caricare
    final FileChooser fc = new FileChooser();

    @FXML
    void addImg1(ActionEvent event) {
        picsFile[0] = fc.showOpenDialog(null);

        if (picsFile[0] != null) {
            nameImg1.setStyle("-fx-text-fill: #f0f8ff");
            nameImg1.setText(picsFile[0].getName());

            // Creo un oggetto Image usando il file selezionato
            pics[0] = new Image(picsFile[0].toURI().toString());
        }
    }

    // l'utente sceglie l'immagine 2 da caricare
    @FXML
    void addImg2(ActionEvent event) {
        picsFile[1] = fc.showOpenDialog(null);

        if (picsFile[1] != null) {
            nameImg2.setStyle("-fx-text-fill: #f0f8ff");
            nameImg2.setText(picsFile[1].getName());

            // Creo un oggetto Image usando il file selezionato
            pics[1] = new Image(picsFile[1].toURI().toString());
        }
    }

    // l'utente sceglie l'immagine 3 da caricare
    @FXML
    void addImg3(ActionEvent event) {
        picsFile[2] = fc.showOpenDialog(null);

        if (picsFile[2] != null) {
            nameImg3.setStyle("-fx-text-fill: #f0f8ff");
            nameImg3.setText(picsFile[2].getName());

            // Creo un oggetto Image usando il file selezionato
            pics[2] = new Image(picsFile[2].toURI().toString());
        }
    }


    // Al click del bottone di conferma inserisce la nuova casa nel database
    @FXML
    void confirmButton(ActionEvent event) {
        var isGood = true;

        // Controllo che tutti i campi siano compilati
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
            //Se qualche campo non è compilato
            errorLabel.setText("Fill in all required fields.");
            errorLabel.setStyle("-fx-text-fill: red");
            errorLabel.setVisible(true);
        } else {
            // Se tutti i campi sono compilati

            if (modifyHouse) {
                // Se sono in modalità modifica

                // Memento: prima della modifica dell'oggetto salvo il suo stato
                houseMemento = house.createMemento();

                // Modifica l'oggetto con i nuovi parametri inseriti
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
                // Se sono in modalità di inserimento
                switch (houseTypeComboBox.getValue()) {
                    case "Apartment": {
                        final var builder = new ApartmentBuilder();
                        final var director = new HouseDirector(builder);
                        house = director.constructApartment(
                            -1, // id sarà assegnato dal DB
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
                            -1, // id sarà assegnato dal DB
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
                            -1, // id sarà assegnato dal DB
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
                // dopo aver inserito ritorna alla schermata principale
                try {
                    homeButton(null);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    // Al click del bottone di reset vengono puliti tutti i campi
    @FXML
    void resetButton(ActionEvent event) {
        if (modifyHouse) {
            // in modalità 'modifica casa' usa memento per ripristinare i Field
            // lo stato ripristinato è quello che precede l'ultima modifica
            if (houseMemento != null) {
                // Memento: ristabilisce il vecchio stato
                houseMemento.restoreState();
                // Ricarica i valori nei Field
                setModifyData(Objects.requireNonNull(house));
                // Invia la richiesta di update
                sendInsertHouseRequest(true);

                errorLabel.setText("Reset successfully completed. Click 'Confirm' to confirm changes.");
                errorLabel.setStyle("-fx-text-fill: green");
                errorLabel.setVisible(true);
            } else {
                System.out.println("Nessuna modifica vecchia");
            }
        } else {
            // in modalità 'inserimento casa' azzera tutti i Field
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

    @FXML
    void deleteButtonAction(ActionEvent event) throws Exception {
        // Apri il popup di conferma
        // Il cambio schermata non è implementato con il command perché i dati sono impostati dinamicamente
        final var primaryStage = StageSingleton.getInstance().getPrimaryStage();
        final var fxmlLoader = new FXMLLoader(StartApplication.class.getResource("popupConfirm.fxml"));
        final var newScene = new Scene(fxmlLoader.load());
        final var newStage = new Stage();
        newStage.setScene(newScene);
        newStage.setResizable(false);
        newStage.setTitle("Confirm Cancellation");
        // Blocca l'interazione con le altre finestre finché la finestra appena aperta non viene chiusa.
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.initOwner(primaryStage);
        // mostra il popup e blocca l'esecuzione fino a quando lo Stage secondario non viene chiuso
        newStage.showAndWait();
        // Prendo il controller del popup
        final var popupConfirmController = fxmlLoader.<PopupConfirmController>getController();
        // Prendo il valore di ritorno del popup dal controller
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
            // Ignora la risposta
            ClientConnection
                .getInstance()
                .getClient()
                .exchange(query);

            // dopo aver eliminato ritorna alla schermata principale
            homeButton(null);
        }

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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameUser.setText("Hi, " + UserAccess.getUser().getLastName());
        // aggiunge gli elementi della ComboBox
        houseTypeComboBox.getItems().setAll(houseType);

        // Pattern Command
        buttonReceiver = new ButtonReceiver(StageSingleton.getInstance().getPrimaryStage());
        goMainViewCommand = new GoMainViewCommand(buttonReceiver);
        goLoginViewCommand = new GoLoginViewCommand(buttonReceiver);
        invoker = new Invoker();

        // Definisco le estensioni accettate del FileChooser
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
    }

    private void sendInsertHouseRequest(boolean update) {
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
        for (final var pic : pics) {
            String data = null;
            if (pic != null) {
                try {
                    final var bytes = Base64
                        .getEncoder()
                        .encode(
                            Files.readAllBytes(Paths.get(new URI(pic.getUrl())))
                        );
                    data = new String(bytes);
                } catch (IOException | URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
            message
                .getJSONObject("data")
                .getJSONObject("parameters")
                .getJSONArray("pictures")
                .put(Objects.requireNonNullElse(data, JSONObject.NULL));
        }
        final var response = ClientConnection
            .getInstance()
            .getClient()
            .exchange(message);
        if (!update && !response.isNull("response")) {
            house.setId(response.getJSONObject("response").getInt("id"));
        }
    }

    // Al click del bottone di logout: ritorna alla View di login
    @FXML
    void logoutButton(ActionEvent event){
        // Pattern Command
        invoker.placeCommand(goLoginViewCommand);
    }

    // Al click del bottone home: ritorna alla View generale mainView.fxml
    @FXML
    void homeButton(ActionEvent event){
        // Pattern Command
        invoker.placeCommand(goMainViewCommand);
    }
}