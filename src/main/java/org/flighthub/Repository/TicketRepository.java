package org.flighthub.Repository;

import org.flighthub.Domain.Client;
import org.flighthub.Domain.Flight;
import org.flighthub.Domain.Ticket;
import org.flighthub.Utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

public class TicketRepository implements ITicketRepository {
    private final JdbcUtils JDBCconnection;

    public TicketRepository(JdbcUtils connection) {
        this.JDBCconnection = connection;
    }

    @Override
    public Optional<Ticket> findOne(UUID id) {
        try {
            Connection connection = JDBCconnection.getConnection();
            String query = "SELECT * FROM Tickets WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, id.toString());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Client client = new Client(resultSet.getString("clientName"), resultSet.getString("clientAddress"));
                Flight flight = new Flight(resultSet.getString("flightDestination"),
                        resultSet.getObject("flightDate", LocalDateTime.class),
                        resultSet.getInt("flightAvailableSeats"));
                String[] touristsArray = resultSet.getString("touristsName").split(",");
                Ticket ticket = new Ticket(client, touristsArray, flight);
                ticket.setId(UUID.fromString(resultSet.getString("id")));
                return Optional.of(ticket);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public Iterable<Ticket> findAll() {
        List<Ticket> tickets = new ArrayList<>();

        try {
            Connection connection = JDBCconnection.getConnection();
            String query = "SELECT * FROM Tickets";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Client client = new Client(resultSet.getString("clientName"), resultSet.getString("clientAddress"));
                Flight flight = new Flight(resultSet.getString("flightDestination"),
                        resultSet.getObject("flightDate", LocalDateTime.class),
                        resultSet.getInt("flightAvailableSeats"));
                String[] touristsArray = resultSet.getString("touristsName").split(",");
                Ticket ticket = new Ticket(client, touristsArray, flight);
                ticket.setId(UUID.fromString(resultSet.getString("id")));
                tickets.add(ticket);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tickets;
    }

    @Override
    public Ticket save(Ticket entity) {
        try {
            Connection connection = JDBCconnection.getConnection();
            String query = "INSERT INTO Tickets (id, clientName, clientAddress, touristsName, flightDestination, flightDate, flightAvailableSeats) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, entity.getId().toString());
            statement.setString(2, entity.getClient().getName());
            statement.setString(3, entity.getClient().getAddress());
            statement.setString(4, String.join(",", entity.getTouristsName()));
            statement.setString(5, entity.getFlight().getDestination());
            statement.setObject(6, entity.getFlight().getDateTime());
            statement.setInt(7, entity.getFlight().getAvailableSeats());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entity;
    }

    @Override
    public Optional<Ticket> delete(UUID id) {
        Optional<Ticket> ticketOptional = findOne(id);

        if (ticketOptional.isPresent()) {
            try {
                Connection connection = JDBCconnection.getConnection();
                String query = "DELETE FROM Tickets WHERE id = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, id.toString());
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return ticketOptional;
    }

    @Override
    public Optional<Ticket> update(Ticket entity) {
        Optional<Ticket> ticketOptional = findOne(entity.getId());

        if (ticketOptional.isPresent()) {
            try {
                Connection connection = JDBCconnection.getConnection();
                String query = "UPDATE Tickets SET clientName = ?, clientAddress = ?, touristsName = ?, flightDestination = ?, flightDate = ?, flightAvailableSeats = ? WHERE id = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, entity.getClient().getName());
                statement.setString(2, entity.getClient().getAddress());
                statement.setString(3, String.join(",", entity.getTouristsName()));
                statement.setString(4, entity.getFlight().getDestination());
                statement.setObject(5, entity.getFlight().getDateTime());
                statement.setInt(6, entity.getFlight().getAvailableSeats());
                statement.setString(7, entity.getId().toString());
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return ticketOptional;
    }
}
