package org.flighthub;

import org.flighthub.Controllers.LoginController;
import org.flighthub.Domain.*;
import org.flighthub.Repository.*;
import org.flighthub.Service.Service;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.flighthub.Utils.JdbcUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class StartApplication extends Application {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public void start(Stage stage) throws IOException {

        logger.traceEntry();

        Properties props = new Properties();
        props.load(new FileInputStream("src/main/java/org/flighthub/jdbc.properties"));
        JdbcUtils jdbcUtils = new JdbcUtils(props);
        AgentRepository agentRepository = new AgentRepository(jdbcUtils);
        ClientRepository clientRepository = new ClientRepository(jdbcUtils);
        FlightRepository flightRepository = new FlightRepository(jdbcUtils);
        TicketRepository ticketRepository = new TicketRepository(jdbcUtils);
        TouristRepository touristRepository=new TouristRepository(jdbcUtils);

        Service service = new Service(agentRepository,clientRepository,flightRepository,ticketRepository,touristRepository);

        //service.addEntities();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        LoginController loginController = fxmlLoader.getController();
        loginController.setService(service);
        stage.setTitle("Login!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}