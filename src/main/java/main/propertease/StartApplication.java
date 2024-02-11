package main.propertease;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class StartApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        final var icon = new Image(Objects.requireNonNull(StartApplication.class.getResourceAsStream("img/icons/app_icon.png")));
        final var fxmlLoader = new FXMLLoader(StartApplication.class.getResource("loginView.fxml"));

        // Imposto lo stage primario che sarà utilizzato per il cambio tra le view
        StageSingleton.getInstance().setPrimaryStage(stage);

        final var scene = new Scene(fxmlLoader.load());
        stage.setResizable(false);
        stage.setTitle("PropertEase");
        stage.setScene(scene);
        stage.getIcons().add(icon);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
