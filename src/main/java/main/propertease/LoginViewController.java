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

/**
 * Controller class for managing loginView.fxml: the view for user login.
 */
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

    /**
     * Initializes the controller.
     * Sets focus on the sign-in button when the view is loaded.
     *
     * @param url            The location used to resolve relative paths for the root object, or null if the location
     *                      is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not
     *                       localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> signInButton.requestFocus());
    }

    /* Sign Up Screen */
    // When the confirm button is clicked for creating an account
    @FXML
    void confirmButtonAction(ActionEvent event) {
        // Check if any field is left empty
        if (firstNameSignUpField.getText().isEmpty() || lastNameSignUpField.getText().isEmpty() ||
                usernameSignUpField.getText().isEmpty() || passwordSingUpField.getText().isEmpty()) {
            errorSignUpText.setText("Fill in all required fields.");
            errorSignUpText.setStyle("-fx-text-fill: red;");
            errorSignUpText.setVisible(true);
        }
        else {
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
                // If the user is already registered, show an error message
                errorSignUpText.setText("Username already exists.");
                errorSignUpText.setStyle("-fx-text-fill: red;");
                errorSignUpText.setVisible(true);
            }
            else {
                // If not registered: insert the new user's data into the database
                errorSignUpText.setText("Registration Successful!");
                errorSignUpText.setStyle("-fx-text-fill: green;");
                errorSignUpText.setVisible(true);

                singUpClearFields();
            }
        }
    }

    /**
     * Switches the view to the Sign In screen and clears the Sign Up fields.
     * Hides the Sign Up error message and sets focus on the Sign In button.
     *
     * @param event The ActionEvent triggered by clicking the button.
     */
    @FXML
    void singInButtonSwitch(ActionEvent event) {
        singInView.setVisible(true);
        singUpView.setVisible(false);

        singUpClearFields();
        errorSignUpText.setVisible(false);

        signInButton.requestFocus();
    }

    /**
     * Handles the action of signing in.
     * Retrieves the username and password from the input fields,
     * sends a sign-in request to the server, and processes the response.
     * If successful, sets the user data and navigates to the main view.
     * If unsuccessful, displays the sign-in error message.
     * Clears the Sign In fields after processing.
     *
     * @param event The ActionEvent triggered by clicking the button.
     * @throws Exception If an error occurs during the sign-in process.
     */
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
            // Handle the error
            errorSignInText.setVisible(true);
        } else {
            final var response = data.getJSONObject("response");
            final var firstName = response.getString("first_name");
            final var lastName = response.getString("last_name");
            final var privileges = response.getInt("privileges");
            final var user = new User(firstName, lastName, username, privileges);
            UserAccess.setUser(user);
            // Go to the next screen
            final var stage = StageSingleton.getInstance().getPrimaryStage();
            final var fxmlLoader = new FXMLLoader(StartApplication.class.getResource("mainView.fxml"));
            final var scene = new Scene(fxmlLoader.load());
            stage.hide();
            stage.setScene(scene);
            stage.show();
        }
        singInClearFields();
    }

    /**
     * Switches the view to the Sign-Up screen.
     * Hides the Sign In view, displays the Sign-Up view,
     * clears the Sign In fields, hides the Sign In error message,
     * and sets focus on the Submit button.
     *
     * @param event The ActionEvent triggered by clicking the button.
     */
    @FXML
    void singUpButtonSwitch(ActionEvent event) {
        singInView.setVisible(false);
        singUpView.setVisible(true);

        singInClearFields();
        errorSignInText.setVisible(false);

        submitButton.requestFocus();
    }

    /**
     * Resets the Sign In fields by clearing the username and password fields.
     */
    private void singInClearFields() {
        usernameSignInField.setText("");
        passwordSingInField.setText("");
    }

    /**
     * Resets the Sign-Up fields by clearing the first name, last name,
     * username, and password fields.
     */
    private void singUpClearFields() {
        firstNameSignUpField.setText("");
        lastNameSignUpField.setText("");
        usernameSignUpField.setText("");
        passwordSingUpField.setText("");
    }

}