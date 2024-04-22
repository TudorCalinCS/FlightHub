
package org.flighthub.Controllers;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.flighthub.Client;

import org.flighthub.ClientListener;
import org.flighthub.StartClient;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.flighthub.Domain.*;

public class MainController implements ClientListener {
    private Agent agent;
    @FXML
    public Label usersName;

    @FXML
    private TableView<Flight> flightTable;

    @FXML
    private TableColumn<Flight, String> destinationColumn;

    @FXML
    private TableColumn<Flight, String> dateTimeColumn;

    @FXML
    private TableColumn<Flight, Integer> availableSeatsColumn;

    @FXML
    private TableColumn<Flight, String> airportColumn;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField destinationTextField;

    @FXML
    private TableView<Flight> foundFlightsTable;

    @FXML
    private TableColumn<Flight, String> foundDestinationColumn;

    @FXML
    private TableColumn<Flight, String> foundDateTimeColumn;

    @FXML
    private TableColumn<Flight, Integer> foundAvailableSeatsColumn;

    @FXML
    private TableColumn<Flight, String> foundAirportColumn;
    @FXML
    private Button searchFlightButton;
    @FXML
    private Button buyTicketButton;
    @FXML
    private Button logoutButton;

    private Client client;

    public void setData(Client client, Agent agent) throws IOException, ClassNotFoundException, InterruptedException {
        this.client = client;
        this.client.addClientListener(this);
        this.agent = agent;
        initialize();
    }

    private void initialize() throws IOException, ClassNotFoundException, InterruptedException {
        usersName.setText(agent.getUsername());
        // Set columns for the flights table
        destinationColumn.setCellValueFactory(new PropertyValueFactory<>("destination"));
        dateTimeColumn.setCellValueFactory(cellData -> {
            LocalDateTime dateTimeProperty = cellData.getValue().getDateTime();
            if (dateTimeProperty != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                return new SimpleStringProperty(formatter.format(dateTimeProperty));
            } else {
                return new SimpleStringProperty("");
            }
        });
        availableSeatsColumn.setCellValueFactory(new PropertyValueFactory<>("availableSeats"));
        airportColumn.setCellValueFactory(new PropertyValueFactory<>("airport"));

        // Set columns for the found flights table
        foundDestinationColumn.setCellValueFactory(new PropertyValueFactory<>("destination"));
        foundDateTimeColumn.setCellValueFactory(cellData -> {
            LocalDateTime dateTimeProperty = cellData.getValue().getDateTime();
            if (dateTimeProperty != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                return new SimpleStringProperty(formatter.format(dateTimeProperty));
            } else {
                return new SimpleStringProperty("");
            }
        });
        foundAvailableSeatsColumn.setCellValueFactory(new PropertyValueFactory<>("availableSeats"));
        foundAirportColumn.setCellValueFactory(new PropertyValueFactory<>("airport"));

        // Load flights into table
        loadFlights();

    }

    private void loadFlights() throws IOException, ClassNotFoundException, InterruptedException {
        foundFlightsTable.getItems().clear();
        flightTable.getItems().clear();
        flightTable.getItems().addAll(client.getFlights());
    }

    @FXML
    private void searchFlight() throws IOException, ClassNotFoundException, InterruptedException {
        // Clear previous search results
        foundFlightsTable.getItems().clear();

        // Retrieve search criteria
        String destination = destinationTextField.getText();
        LocalDate departureDate = datePicker.getValue();

        // Validate input
        if (destination.isEmpty() || departureDate == null) {
            // Display error message or handle invalid input
            return;
        }

        // Perform search
        List<Flight> foundFlights = client.searchFlights(destination, departureDate);

        // Display search results
        foundFlightsTable.getItems().addAll(foundFlights);
    }

    @FXML
    private void buyTicket() throws IOException, ClassNotFoundException, InterruptedException {
        Flight selectedFlight = foundFlightsTable.getSelectionModel().getSelectedItem();

        if (selectedFlight != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ticket-view.fxml"));
            Parent root = loader.load();

            TicketController controller = loader.getController();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            Map<String, String> ticketData = controller.getData();
            String clientName = ticketData.get("clientName");
            String clientAddress = ticketData.get("clientAddress");
            String touristsName = ticketData.get("touristsName");
            int seats = Integer.parseInt(ticketData.get("seats"));

            client.buyTicket(clientName, clientAddress, touristsName, seats, selectedFlight);

            new Alert(Alert.AlertType.CONFIRMATION, "Ticket bought").showAndWait();
        } else {
            new Alert(Alert.AlertType.ERROR, "No flight selected").showAndWait();

        }
    }

    @FXML
    private void logout() throws IOException {
        Stage stage = (Stage) flightTable.getScene().getWindow();
        stage.close();

        FXMLLoader fxmlLoader = new FXMLLoader(StartClient.class.getResource("/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        LoginController loginController = fxmlLoader.getController();
        loginController.setClient(client);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void onClientUpdate() {
        Platform.runLater(() ->
                {
                    try {
                        loadFlights();
                    } catch (IOException | ClassNotFoundException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
        );

    }

}
