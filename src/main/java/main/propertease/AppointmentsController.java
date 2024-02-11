package main.propertease;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.JSONObject;

// Class that manages appointments.fxml: the view dedicated to appointment details
public class AppointmentsController {
    private int id;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Label labelHouseAddr;

    @FXML
    private Label labelHouseName;

    @FXML
    private Label labelDate;

    @FXML
    private Label labelAgent;

    /**
     * Updates the values of the appointment details displayed in the view.
     *
     * @param appointment The appointment object containing the details to display.
     */
    public void setData(Appointment appointment) {
        id = appointment.getId();
        labelDate.setText(appointment.getAppointmentDate());
        if (UserAccess.getUser().getPrivileges() == 1) {
            labelAgent.setText("Client: " + appointment.getAdministratorName());
        }
        else {
            labelAgent.setText("Estate Agent: " + appointment.getAdministratorName());
        }
        labelHouseName.setText(appointment.getHouseName());
        labelHouseAddr.setText(appointment.getHouseAddress().replace("|", ", "));
    }

    /**
     * Handles the action when the remove appointment button is clicked.
     *
     * <p>This method opens a confirmation popup and performs the removal of the appointment if confirmed.
     *
     * @param event The ActionEvent triggered by the user clicking the remove appointment button.
     * @throws Exception If an error occurs during the execution of the method.
     */
    @FXML
    void removeAppointment(ActionEvent event) throws
                                              Exception {
        // Open the confirmation popup
        final var primaryStage = StageSingleton.getInstance().getPrimaryStage();
        final var fxmlLoader = new FXMLLoader(StartApplication.class.getResource("popupConfirm.fxml"));
        final var newScene = new Scene(fxmlLoader.load());
        final var newStage = new Stage();
        newStage.setScene(newScene);
        newStage.setResizable(false);
        newStage.setTitle("Confirm Cancellation");
        // Block interaction with other windows until this window is closed.
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.initOwner(primaryStage);

        // Show the popup and block execution until the secondary Stage is closed
        newStage.showAndWait();

        // Get the controller of the popup
        final var popupConfirmController = fxmlLoader.<PopupConfirmController>getController();
        // Get the popup result from the controller
        if (popupConfirmController.getResult()) {
            // Create a JSON message to request appointment removal
            final var message = new JSONObject(
                    String.format(
                            """
                                    {
                                      "type": "appointment",
                                      "data": {
                                        "request": "removeAppointment",
                                        "parameters": {
                                          "id": %d
                                        }
                                      }
                                    }
                                    """,
                            id
                    )
            );
            // Ignore the result of exchange since it can never fail
            final var ignored = ClientConnection
                    .getInstance()
                    .getClient()
                    .exchange(message);
            // Load the main view
            final var newFxmlLoader = new FXMLLoader(StartApplication.class.getResource("mainView.fxml"));
            final var scene = new Scene(newFxmlLoader.load());
            primaryStage.setScene(scene);
        }
    }
}