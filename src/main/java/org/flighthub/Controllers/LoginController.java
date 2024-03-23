package org.flighthub.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.flighthub.Domain.Agent;
import org.flighthub.Service.Service;
import org.flighthub.StartApplication;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;


    private Service service;
    private Agent agent;

    public void setService(Service service) {
        this.service = service;
    }

    @FXML
    public void handleLogin() throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        this.agent = service.login(username, password);
        //Agent exists
        if (agent != null) {

            FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("/main-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            MainController mainController = fxmlLoader.getController();
            mainController.setData(service, agent);

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            //Stage stagee = (Stage) fullNameField.getScene().getWindow();
            //stagee.close();

        }
        //Agent not found
        else {
            new Alert(Alert.AlertType.ERROR, "Invalid username or password!").showAndWait();
        }
    }


}
