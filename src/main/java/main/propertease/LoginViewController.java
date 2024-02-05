package main.propertease;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.json.JSONObject;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginViewController implements Initializable {
    @FXML
    private Label errorSingInText;

    @FXML
    private Label errorSingUpText;

    @FXML
    private TextField nameSingUpField;

    @FXML
    private PasswordField passwordSingInField;

    @FXML
    private PasswordField passwordSingUpField;

    @FXML
    private VBox singInView;

    @FXML
    private VBox singUpView;

    @FXML
    private TextField surnameSingUpField;

    @FXML
    private TextField usernameSingInField;

    @FXML
    private TextField usernameSingUpField;

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
    void confirmButtonAction(ActionEvent event) throws Exception {
        // Controllo se c'è un campo non compilato
        /*if (NameSingUpField.getText().isEmpty() || SurnameSingUpField.getText().isEmpty() ||
                UsernameSingUpField.getText().isEmpty() || PasswordSingUpField.getText().isEmpty()) {
            ErrorSingUpText.setText("Fill in all required fields.");
            ErrorSingUpText.setStyle("-fx-text-fill: red;");
            ErrorSingUpText.setVisible(true);
        } else {
            // Controllo se l'username è già registrato
            String query = String.format("SELECT * FROM useraccount WHERE username = '%s' ", UsernameSingUpField.getText());
            Statement statement = connectionDB.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                System.out.println("Utente già registrato");
                ErrorSingUpText.setText("Username already exists.");
                ErrorSingUpText.setStyle("-fx-text-fill: red;");
                ErrorSingUpText.setVisible(true);
            }
            // Se non è registrato: inserisco i dati del nuovo utente nel database
            else {
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
        }*/
    }

    // Al click del bottone Sing In -> visualizza la schermata di Sing In
    @FXML
    void singInButtonSwitch(ActionEvent event) {
        singInView.setVisible(true);
        singUpView.setVisible(false);

        singUpClearFields();
        errorSingUpText.setVisible(false);

        signInButton.requestFocus();
    }

    /* Schermata Sing In */
    // Al click del bottone di accesso
    @FXML
    void singInButtonAction(ActionEvent event) {
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
        final var username = usernameSingInField.getText();
        final var password = passwordSingInField.getText();
        final var message = new JSONObject(String.format(query, username, password));
        final var data = ClientConnection
            .getInstance()
            .getClient()
            .exchange(message);
        if (data.isNull("response")) {
            // gestisci l'errore
        } else {
            final var response = data.getJSONObject("response");
            final var firstName = response.getString("first_name");
            final var lastName = response.getString("last_name");
            final var privileges = response.getInt("privileges");
            final var user = new User(username, firstName, lastName, privileges);
            UserAccess.setUser(user);
            // vai alla schermata successiva
        }
        singInClearFields();
    }

    // Al click del bottone Sing Up -> visualizza la schermata di Sing Up
    @FXML
    void singUpButtonSwitch(ActionEvent event) {
        singInView.setVisible(false);
        singUpView.setVisible(true);

        singInClearFields();
        errorSingInText.setVisible(false);

        submitButton.requestFocus();
    }


    // Procedure per il reset dei Field
    private void singInClearFields() {
        usernameSingInField.setText("");
        passwordSingInField.setText("");
    }

    private void singUpClearFields() {
        nameSingUpField.setText("");
        surnameSingUpField.setText("");
        usernameSingUpField.setText("");
        passwordSingUpField.setText("");
    }
}