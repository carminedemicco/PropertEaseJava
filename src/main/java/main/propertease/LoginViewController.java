package main.propertease;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class LoginViewController implements Initializable {
    private Connection connectionDB;

    @FXML
    private Label ErrorSingInText;

    @FXML
    private Label ErrorSingUpText;

    @FXML
    private TextField NameSingUpField;

    @FXML
    private PasswordField PasswordSingInField;

    @FXML
    private PasswordField PasswordSingUpField;

    @FXML
    private VBox SingInView;

    @FXML
    private VBox SingUpView;

    @FXML
    private TextField SurnameSingUpField;

    @FXML
    private TextField UsernameSingInField;

    @FXML
    private TextField UsernameSingUpField;

    @FXML
    private Button SingInButton;

    @FXML
    private Button SubmitButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Connessione al database
        try {
            connectionDB = DBConnection.getDBConnection();
        } catch (Exception e) {throw new RuntimeException(e);}


        // annulla il prompt automatico sui TextField
        Platform.runLater(()->SingInButton.requestFocus());
    }



    /* Schermata Sing Up */
    // Al click del bottone di conferma per la creazione di un account
    @FXML
    void ConfirmButtonAction(ActionEvent event) throws Exception{
        // Controllo se c'è un campo non compilato
        if (NameSingUpField.getText().isEmpty() || SurnameSingUpField.getText().isEmpty() ||
        UsernameSingUpField.getText().isEmpty() || PasswordSingUpField.getText().isEmpty()){
            ErrorSingUpText.setText("Fill in all required fields.");
            ErrorSingUpText.setStyle("-fx-text-fill: red;");
            ErrorSingUpText.setVisible(true);
        }

        else{
            // Controllo se l'username è già registrato
            String query = String.format("SELECT * FROM useraccount WHERE username = '%s' ", UsernameSingUpField.getText());
            Statement statement = connectionDB.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if(resultSet.next()) {
                System.out.println("Utente già registrato");
                ErrorSingUpText.setText("Username already exists.");
                ErrorSingUpText.setStyle("-fx-text-fill: red;");
                ErrorSingUpText.setVisible(true);
            }
            // Se non è registrato: inserisco i dati del nuovo utente nel database
            else{
                query = String.format("INSERT INTO useraccount (username, name, surname, password, type) VALUES ('%s', '%s', '%s', '%s', 'Buyer'); ",
                        UsernameSingUpField.getText(), NameSingUpField.getText(), SurnameSingUpField.getText(), PasswordSingUpField.getText());
                System.out.println(query);
                statement = connectionDB.createStatement();
                statement.executeUpdate(query);
                ErrorSingUpText.setText("Registration Successful!");
                ErrorSingUpText.setStyle("-fx-text-fill: green;");
                ErrorSingUpText.setVisible(true);

                singUpClearFields();
            }
        }
    }


    // Al click del bottone Sing In -> visualizza la schermata di Sing In
    @FXML
    void SingInButtonSwitch(ActionEvent event) {
        SingInView.setVisible(true);
        SingUpView.setVisible(false);

        singUpClearFields();
        ErrorSingUpText.setVisible(false);

        SingInButton.requestFocus();
    }



    /* Schermata Sing In */
    // Al click del bottone di accesso
    @FXML
    void SingInButtonAction(ActionEvent event) throws Exception {
        // Query: controlla che le credenziali siano giuste
        String query = String.format("SELECT * FROM useraccount WHERE username = '%s' AND password = '%s'",
                UsernameSingInField.getText(), PasswordSingInField.getText());
        Statement statement = connectionDB.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        if(resultSet.next()) { //se le credenziali sono giuste
            String viewName = "mainView.fxml";
            Stage stage = (Stage) SingInButton.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource(viewName));
            Scene scene = new Scene(fxmlLoader.load());
            stage.hide();
            stage.setScene(scene);
            stage.show();
        }
        else{
            ErrorSingInText.setVisible(true);
            System.out.println("Accesso Fallito");
        }

        singInClearFields();
    }


    // Al click del bottone Sing Up -> visualizza la schermata di Sing Up
    @FXML
    void SingUpButtonSwitch(ActionEvent event) {
        SingInView.setVisible(false);
        SingUpView.setVisible(true);

        singInClearFields();
        ErrorSingInText.setVisible(false);

        SubmitButton.requestFocus();
    }


    // Procedure per il reset dei Field
    private void singInClearFields(){
        UsernameSingInField.setText("");
        PasswordSingInField.setText("");
    }
    private void singUpClearFields(){
        NameSingUpField.setText("");
        SurnameSingUpField.setText("");
        UsernameSingUpField.setText("");
        PasswordSingUpField.setText("");
    }
}