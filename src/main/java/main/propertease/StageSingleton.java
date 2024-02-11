package main.propertease;

import javafx.stage.Stage;

/**
 * Singleton class to store the primary stage
 */
public class StageSingleton {
    private static StageSingleton instance;
    private Stage primaryStage;

    /**
     * Private constructor to prevent instantiation
     */
    private StageSingleton() {}

    /**
     * Get the instance of the singleton
     * @return the instance of the singleton
     */
    public static StageSingleton getInstance() {
        if (instance == null) {
            instance = new StageSingleton();
        }
        return instance;
    }

    /**
     * Get the primary stage
     * @return the primary stage
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Set the primary stage
     * @param stage the primary stage
     */
    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }
}
