package org.flighthub.Repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flighthub.Domain.Client;
import org.flighthub.Domain.Flight;
import org.flighthub.Domain.Ticket;
import org.flighthub.Utils.JdbcUtils;
import org.flighthub.Repository.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

public class TicketRepository implements ITicketRepository {
    private final JdbcUtils JDBCconnection;

    private static final Logger logger = LogManager.getLogger();

    public TicketRepository(JdbcUtils connection) {
        this.JDBCconnection = connection;
    }

    @Override
    public Optional<Ticket> findOne(UUID id) {
        try {
            logger.traceEntry();
            logger.info("trying to find one Ticket");
            Connection connection = JDBCconnection.getConnection();
            String query = "SELECT * FROM Ticket WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, id.toString());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                ClientRepository clientRepository = new ClientRepository(JDBCconnection);
                Optional<Client> client = clientRepository.findOne(UUID.fromString(resultSet.getString("clientId")));
                Client client1 = client.orElse(null);
                FlightRepository flightRepository = new FlightRepository(JDBCconnection);
                Optional<Flight> flight = flightRepository.findOne(UUID.fromString(resultSet.getString("flightId")));
                Flight flight1 = flight.orElse(null);
                String[] touristsArray = resultSet.getString("touristsName").split(",");
                int seats = resultSet.getInt("seats");
                Ticket ticket = new Ticket(client1, touristsArray, flight1, seats);
                ticket.setId(UUID.fromString(resultSet.getString("id")));
                return Optional.of(ticket);
            }
        } catch (SQLException e) {
            System.out.println("Error Repo " + e);
            logger.error(e);
        }

        return Optional.empty();
    }

    @Override
    public Iterable<Ticket> findAll() {
        List<Ticket> tickets = new ArrayList<>();

        try {
            logger.traceEntry();
            logger.info("trying to find multiple Tickets");
            Connection connection = JDBCconnection.getConnection();
            String query = "SELECT * FROM Ticket";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                ClientRepository clientRepository = new ClientRepository(JDBCconnection);
                Optional<Client> client = clientRepository.findOne(UUID.fromString(resultSet.getString("clientId")));
                Client client1 = client.orElse(null);
                FlightRepository flightRepository = new FlightRepository(JDBCconnection);
                Optional<Flight> flight = flightRepository.findOne(UUID.fromString(resultSet.getString("flightId")));
                Flight flight1 = flight.orElse(null);
                String[] touristsArray = resultSet.getString("touristsName").split(",");
                int seats = resultSet.getInt("seats");
                Ticket ticket = new Ticket(client1, touristsArray, flight1, seats);
                ticket.setId(UUID.fromString(resultSet.getString("id")));
                tickets.add(ticket);
            }
        } catch (SQLException e) {
            System.out.println("Error Repo " + e);
            logger.error(e);
            ;
        }

        return tickets;
    }

    @Override
    public Ticket save(Ticket entity) {
        try {
            logger.traceEntry();
            logger.info("trying to save one Ticket");
            Connection connection = JDBCconnection.getConnection();
            String query = "INSERT INTO Ticket (id, clientId, touristsName, flightId,seats) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, UUID.randomUUID().toString());
            statement.setString(2, entity.getClient().getId().toString());
            statement.setString(3, String.join(",", entity.getTouristsName()));
            statement.setString(4, entity.getFlight().getId().toString());
            statement.setInt(5, entity.getSeats());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error Repo " + e);
            logger.error(e);
        }

        return entity;
    }

    @Override
    public Optional<Ticket> delete(UUID id) {
        Optional<Ticket> ticketOptional = findOne(id);

        if (ticketOptional.isPresent()) {
            try {
                logger.traceEntry();
                logger.info("trying to delete one Ticket");
                Connection connection = JDBCconnection.getConnection();
                String query = "DELETE FROM Ticket WHERE id = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, id.toString());
                statement.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error Repo " + e);
                logger.error(e);
            }
        }

        return ticketOptional;
    }

    @Override
    public Optional<Ticket> update(Ticket entity) {
        Optional<Ticket> ticketOptional = findOne(entity.getId());

        if (ticketOptional.isPresent()) {
            try {
                logger.traceEntry();
                logger.info("trying to update one Ticket");
                Connection connection = JDBCconnection.getConnection();
                String query = "UPDATE Ticket SET clientId = ?, touristsName = ?, flightId = ?, seats = ? WHERE id = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, entity.getClient().getId().toString());
                statement.setString(2, String.join(",", entity.getTouristsName()));
                statement.setString(3, entity.getFlight().getId().toString());
                statement.setObject(4, entity.getSeats());
                statement.setString(5, entity.getId().toString());
                statement.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error Repo " + e);
                logger.error(e);
            }
        }

        return ticketOptional;
    }
}
