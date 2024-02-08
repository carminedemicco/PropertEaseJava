package main.propertease;

import javafx.stage.Stage;

// Utilizzo il pattern Singleton per
public class StageSingleton {
    private static StageSingleton instance;
    private Stage primaryStage;

    private StageSingleton() {
        // Costruttore privato per evitare l'istanziazione diretta
    }

    public static StageSingleton getInstance() {
        if (instance == null) {
            instance = new StageSingleton();
        }
        return instance;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }
}
