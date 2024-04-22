package org.flighthub;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.flighthub.Controllers.LoginController;
//import org.flighthub.Controllers.LoginController;

import java.io.IOException;

public class StartClient extends Application {
    public static void main(String[] args) throws IOException {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Client client = new Client("127.0.0.1", 55556);

        FXMLLoader fxmlLoader = new FXMLLoader(StartClient.class.getResource("/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        LoginController loginController = fxmlLoader.getController();
        loginController.setClient(client);
        stage.setTitle("Login!");
        stage.setScene(scene);
        stage.show();


    }
}
