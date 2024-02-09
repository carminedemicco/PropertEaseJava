package main.propertease.command;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.propertease.StartApplication;

import java.io.IOException;

public class ButtonReceiver {
    private final Stage primaryStage;
    private FXMLLoader fxmlLoader;
    private Scene newScene;

    public ButtonReceiver(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void goMainView() {
        // Istruzioni per andare alla homepage
        fxmlLoader = new FXMLLoader(StartApplication.class.getResource("mainView.fxml"));
        try {
            newScene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        primaryStage.setScene(newScene);
    }

    public void goLoginView() {
        // Istruzioni per andare alla LoginView
        fxmlLoader = new FXMLLoader(StartApplication.class.getResource("loginView.fxml"));
        try {
            newScene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        primaryStage.hide();
        primaryStage.setScene(newScene);
        primaryStage.show();
    }

    public void goAddHouseView() {
        // Istruzioni per andare alla pagina di inserimento casa
        fxmlLoader = new FXMLLoader(StartApplication.class.getResource("addHouseView.fxml"));
        try {
            newScene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        primaryStage.setScene(newScene);
    }

    public void goInsertAvailabilityView() {
        // Crea la nuova scena
        final var newStage = new Stage();
        final var fxmlLoader = new FXMLLoader(StartApplication.class.getResource("popupInsertAvailability.fxml"));
        try {
            newScene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        newStage.setScene(newScene);

        // Blocca l'interazione con le altre finestre finché la finestra appena aperta non viene chiusa.
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.initOwner(primaryStage);

        // Mostra la nuova finestra
        newStage.setResizable(false);
        newStage.setTitle("Select Available Dates");
        newStage.show();
    }
}
