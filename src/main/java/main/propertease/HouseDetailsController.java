package main.propertease;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.propertease.builder.House;
import main.propertease.command.ButtonReceiver;
import main.propertease.command.GoLoginViewCommand;
import main.propertease.command.GoMainViewCommand;
import main.propertease.command.Invoker;
import main.propertease.decorator.HouseInterface;
import main.propertease.decorator.HouseVat;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

// Classe che gestisce houseDetails.fxml: la view dedicata ai dettagli di una casa
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


    // Avviato alla creazione della View
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameUser.setText("Hi, " + UserAccess.getUser().getLastName());

        // Pattern Command
        buttonReceiver = new ButtonReceiver(StageSingleton.getInstance().getPrimaryStage());
        goMainViewCommand = new GoMainViewCommand(buttonReceiver);
        goLoginViewCommand = new GoLoginViewCommand(buttonReceiver);
        invoker = new Invoker();

        // Imposta le immagini tonde
        img1.setPreserveRatio(false); //si adatta alla dimensione
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

        // Imposta l'effetto sfumato alle box
        detailbox1.setEffect(new DropShadow(20, Color.BLACK));
        detailbox2.setEffect(new DropShadow(20, Color.BLACK));
        detailbox3.setEffect(new DropShadow(20, Color.BLACK));

        if (UserAccess.getUser().getPrivileges() == 1) {
            addAdminButtons();

            makeAppButton.setVisible(false);
        }
    }

    // Funzione che aggiunge il bottone di modifica casa solo se i log è admin
    private void addAdminButtons() {
        final var btn1 = new Button();
        btn1.getStyleClass().add("modify-house-button");
        adminButtonsArea.getChildren().add(btn1);

        btn1.setOnAction(e -> {
            try {
                // Il cambio schermata non è implementato con il command perché i dati sono impostati dinamicamente
                final var stage = StageSingleton.getInstance().getPrimaryStage();
                final var fxmlLoader = new FXMLLoader(StartApplication.class.getResource("addHouseView.fxml"));
                final var scene = new Scene(fxmlLoader.load());
                final var addHouseController = fxmlLoader.<AddHouseController>getController();
                addHouseController.setModifyData(house);
                stage.setScene(scene);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

    }

    // al click di 'Price without VAT' usa il Pattern Decorator per calcolare il prezzo senza VAT
    @FXML
    void vatButton(ActionEvent event) {
        // Uso il Pattern Decorator per estendere l'oggetto house a run-time
        HouseInterface vatHouse = new HouseVat(house);
        // Ottengo e imposto il nuovo prezzo con l'eliminazione della VAT
        priceLabel.setText(String.valueOf(vatHouse.getPrice()));
    }

    // al click di 'Make an Appointment' apre una finestra che fa selezionare la data dell'appuntamento
    @FXML
    void makeAppointmentButton(ActionEvent event) throws IOException {
        final var primaryStage = (Stage)detailbox1.getScene().getWindow();

        // Crea la nuova scena
        final var newStage = new Stage();
        final var fxmlLoader = new FXMLLoader(StartApplication.class.getResource("popupSelectDate.fxml"));
        final var newScene = new Scene(fxmlLoader.load());
        final var selectDataController = fxmlLoader.<SelectDateController>getController();
        selectDataController.setData(house.getId());
        newStage.setScene(newScene);

        // Blocca l'interazione con le altre finestre finché la finestra appena aperta non viene chiusa.
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.initOwner(primaryStage);

        // Mostra la nuova finestra
        newStage.setResizable(false);
        newStage.setTitle("Make an Appointment");
        newStage.show();
    }

    public void setData(House house) {
        this.house = house;

        img1.setImage(house.getPics(0));
        img2.setImage(house.getPics(1));
        img3.setImage(house.getPics(2));
        addressLabel.setText(house.getAddress());
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
