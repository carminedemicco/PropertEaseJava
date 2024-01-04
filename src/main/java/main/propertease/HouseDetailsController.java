package main.propertease;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

// Classe che gestisce houseDetails.fxml: la view dedicata ai dettagli di una casa
public class HouseDetailsController implements Initializable {

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
    private Label usernameLabel;

    // Al click del bottone di logout: ritorna alla View di login
    @FXML
    void logoutButton(ActionEvent event) throws Exception{
        // Operazioni di logout
        //...

        Stage stage = (Stage) detailbox1.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("loginView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.hide();
        stage.setScene(scene);
        stage.show();
    }


    // Al click del bottone home: ritorna alla View generale buyerView.fxml
    @FXML
    void homeButton(ActionEvent event) throws Exception{
        Stage stage = (Stage) detailbox1.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("buyerView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);

    }


    // Avviato alla creazione della View
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /* NB: DA MODIFICARE CON I DATI PROVENIENTI DAL DATABASE */
        // Imposta l'immgine più grande
        Image image = new Image(getClass().getResourceAsStream("img/house.png"));
        img1.setPreserveRatio(false); //si adatta alla dimensione
        Rectangle roundedRectangle = new Rectangle(img1.getFitWidth(), img1.getFitHeight());
        roundedRectangle.setArcWidth(25); roundedRectangle.setArcHeight(25); img1.setClip(roundedRectangle);
        img1.setImage(image);

        // Imposta la prima immagine piccola
        image = new Image(getClass().getResourceAsStream("img/house.png"));
        img2.setPreserveRatio(false);
        roundedRectangle = new Rectangle(img2.getFitWidth(), img2.getFitHeight());
        roundedRectangle.setArcWidth(25); roundedRectangle.setArcHeight(25); img2.setClip(roundedRectangle);
        img2.setImage(image);

        // Imposta la seconda immagine piccola
        image = new Image(getClass().getResourceAsStream("img/house.png"));
        img3.setPreserveRatio(false);
        roundedRectangle = new Rectangle(img3.getFitWidth(), img3.getFitHeight());
        roundedRectangle.setArcWidth(25); roundedRectangle.setArcHeight(25); img3.setClip(roundedRectangle);
        img3.setImage(image);

        // Imposta l'effetto sfumato alle box
        detailbox1.setEffect(new DropShadow(20, Color.BLACK));
        detailbox2.setEffect(new DropShadow(20, Color.BLACK));
        detailbox3.setEffect(new DropShadow(20, Color.BLACK));
    }
}
