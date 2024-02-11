package main.propertease;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.json.JSONObject;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginViewController implements Initializable {
    @FXML
    private Label errorSignInText;

    @FXML
    private Label errorSignUpText;

    @FXML
    private TextField firstNameSignUpField;

    @FXML
    private PasswordField passwordSingInField;

    @FXML
    private PasswordField passwordSingUpField;

    @FXML
    private VBox singInView;

    @FXML
    private VBox singUpView;

    @FXML
    private TextField lastNameSignUpField;

    @FXML
    private TextField usernameSignInField;

    @FXML
    private TextField usernameSignUpField;

    @FXML
    private Button signInButton;

    @FXML
    private Button submitButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> signInButton.requestFocus());
    }

    /* Schermata Sing Up */
    // Al click del bottone di conferma per la creazione di un account
    @FXML
    void confirmButtonAction(ActionEvent event) {
        // Controllo se c'è un campo non compilato
        if (firstNameSignUpField.getText().isEmpty() || lastNameSignUpField.getText().isEmpty() ||
            usernameSignUpField.getText().isEmpty() || passwordSingUpField.getText().isEmpty()) {
            errorSignUpText.setText("Fill in all required fields.");
            errorSignUpText.setStyle("-fx-text-fill: red;");
            errorSignUpText.setVisible(true);
        } else {
            final var query = """
                      {
                        "type": "generic",
                        "data": {
                          "request": "signup",
                          "parameters": {
                            "username": "%s",
                            "first_name": "%s",
                            "last_name": "%s",
                            "password": "%s",
                          }
                        }
                      }
                """;
            final var message = new JSONObject(
                String.format(
                    query,
                    usernameSignUpField.getText(),
                    firstNameSignUpField.getText(),
                    lastNameSignUpField.getText(),
                    passwordSingUpField.getText()
                )
            );
            final var data = ClientConnection
                .getInstance()
                .getClient()
                .exchange(message);
            if (data.isNull("response")) {
                // Se l'utente è già registrato, visualizzo un messaggio di errore
                errorSignUpText.setText("Username already exists.");
                errorSignUpText.setStyle("-fx-text-fill: red;");
                errorSignUpText.setVisible(true);
            } else {
                // Se non è registrato: inserisco i dati del nuovo utente nel database
                errorSignUpText.setText("Registration Successful!");
                errorSignUpText.setStyle("-fx-text-fill: green;");
                errorSignUpText.setVisible(true);

                singUpClearFields();
            }
        }
    }

    // Al click del bottone Sing In -> visualizza la schermata di Sing In
    @FXML
    void singInButtonSwitch(ActionEvent event) {
        singInView.setVisible(true);
        singUpView.setVisible(false);

        singUpClearFields();
        errorSignUpText.setVisible(false);

        signInButton.requestFocus();
    }

    /* Schermata Sing In */
    // Al click del bottone di accesso
    @FXML
    void singInButtonAction(ActionEvent event) throws Exception {
        final var query = """
              {
                "type": "generic",
                "data": {
                  "request": "signin",
                  "parameters": {
                    "username": "%s",
                    "password": "%s"
                  }
                }
              }
            """;
        final var username = usernameSignInField.getText();
        final var password = passwordSingInField.getText();
        final var message = new JSONObject(String.format(query, username, password));
        final var data = ClientConnection
            .getInstance()
            .getClient()
            .exchange(message);
        if (data.isNull("response")) {
            // gestisci l'errore
            errorSignInText.setVisible(true);
        } else {
            final var response = data.getJSONObject("response");
            final var firstName = response.getString("first_name");
            final var lastName = response.getString("last_name");
            final var privileges = response.getInt("privileges");
            final var user = new User(firstName, lastName, username, privileges);
            UserAccess.setUser(user);
            // vai alla schermata successiva
            final var stage = StageSingleton.getInstance().getPrimaryStage();
            final var fxmlLoader = new FXMLLoader(StartApplication.class.getResource("mainView.fxml"));
            final var scene = new Scene(fxmlLoader.load());
            stage.hide();
            stage.setScene(scene);
            stage.show();
        }
        singInClearFields();
    }

    // Al click del bottone Sing Up -> visualizza la schermata di Sing Up
    @FXML
    void singUpButtonSwitch(ActionEvent event) {
        singInView.setVisible(false);
        singUpView.setVisible(true);

        singInClearFields();
        errorSignInText.setVisible(false);

        submitButton.requestFocus();
    }


    // Procedure per il reset dei Field
    private void singInClearFields() {
        usernameSignInField.setText("");
        passwordSingInField.setText("");
    }

    private void singUpClearFields() {
        firstNameSignUpField.setText("");
        lastNameSignUpField.setText("");
        usernameSignUpField.setText("");
        passwordSingUpField.setText("");
    }
}