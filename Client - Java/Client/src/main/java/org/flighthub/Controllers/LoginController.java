
package org.flighthub.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.flighthub.Client;
import org.flighthub.Domain.Agent;
import org.flighthub.StartClient;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    private Agent agent;
    private Client client;

    public void setClient(Client client){
        this.client=client;
    }
    @FXML
    public void handleLogin() throws IOException, ClassNotFoundException, InterruptedException {

        String username = usernameField.getText();
        String password = passwordField.getText();
        this.agent = client.login(username,password);

        //Agent exists
        if (agent != null) {

            FXMLLoader fxmlLoader = new FXMLLoader(StartClient.class.getResource("/main-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            MainController mainController = fxmlLoader.getController();
            mainController.setData(client, agent);

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            Stage stagee = (Stage) usernameField.getScene().getWindow();
            stagee.close();

        }
        //Agent not found
        else {
            new Alert(Alert.AlertType.ERROR, "Invalid username or password!").showAndWait();
        }

    }


}
