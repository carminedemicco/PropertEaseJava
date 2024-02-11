package main.propertease.command;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.propertease.StartApplication;

import java.io.IOException;

/**
 * Receiver class for handling button actions in the UI.
 */
public class ButtonReceiver {

    private final Stage primaryStage;
    private FXMLLoader fxmlLoader;
    private Scene newScene;

    /**
     * Constructor for ButtonReceiver.
     *
     * @param primaryStage The primary stage of the application.
     */
    public ButtonReceiver(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Switches the scene to the main view.
     */
    public void goMainView() {
        fxmlLoader = new FXMLLoader(StartApplication.class.getResource("mainView.fxml"));
        try {
            newScene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        primaryStage.setScene(newScene);
    }

    /**
     * Switches the scene to the login view.
     */
    public void goLoginView() {
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

    /**
     * Switches the scene to the add house view.
     */
    public void goAddHouseView() {
        fxmlLoader = new FXMLLoader(StartApplication.class.getResource("addHouseView.fxml"));
        try {
            newScene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        primaryStage.setScene(newScene);
    }

    /**
     * Opens a popup window for inserting availability dates.
     */
    public void goInsertAvailabilityView() {
        final var newStage = new Stage();
        final var fxmlLoader = new FXMLLoader(StartApplication.class.getResource("popupInsertAvailability.fxml"));
        try {
            newScene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        newStage.setScene(newScene);

        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.initOwner(primaryStage);

        newStage.setResizable(false);
        newStage.setTitle("Select Available Dates");
        newStage.show();
    }
}