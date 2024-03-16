package org.flighthub.Repository;

import org.flighthub.Domain.Flight;
import org.flighthub.Utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FlightRepository implements IFlightRepository {
    private final JdbcUtils JDBCconnection;

    public FlightRepository(JdbcUtils connection) {
        this.JDBCconnection = connection;
    }

    @Override
    public Optional<Flight> findOne(UUID id) {
        try {
            Connection connection = JDBCconnection.getConnection();
            String query = "SELECT * FROM Flights WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, id.toString());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Flight flight = new Flight(resultSet.getString("destination"),
                        resultSet.getObject("date", LocalDateTime.class),
                        resultSet.getInt("availableSeats"));
                flight.setId(UUID.fromString(resultSet.getString("id")));
                return Optional.of(flight);
            }
        } catch (SQLException e) {
            System.out.println("Error DB "+e);
        }

        return Optional.empty();
    }

    @Override
    public Iterable<Flight> findAll() {
        List<Flight> flights = new ArrayList<>();

        try {
            Connection connection = JDBCconnection.getConnection();
            String query = "SELECT * FROM Flights";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Flight flight = new Flight(resultSet.getString("destination"),
                        resultSet.getObject("date", LocalDateTime.class),
                        resultSet.getInt("availableSeats"));
                flight.setId(UUID.fromString(resultSet.getString("id")));
                flights.add(flight);
            }
        } catch (SQLException e) {
            System.out.println("Error DB "+e);
        }

        return flights;
    }

    @Override
    public Flight save(Flight entity) {
        try {
            Connection connection = JDBCconnection.getConnection();
            String query = "INSERT INTO Flights (id, destination, date, availableSeats) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, entity.getId().toString());
            statement.setString(2, entity.getDestination());
            statement.setObject(3, entity.getDateTime());
            statement.setInt(4, entity.getAvailableSeats());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error DB "+e);
        }

        return entity;
    }

    @Override
    public Optional<Flight> delete(UUID id) {
        Optional<Flight> flightOptional = findOne(id);

        if (flightOptional.isPresent()) {
            try {
                Connection connection = JDBCconnection.getConnection();
                String query = "DELETE FROM Flights WHERE id = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, id.toString());
                statement.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error DB "+e);
            }
        }

        return flightOptional;
    }

    @Override
    public Optional<Flight> update(Flight entity) {
        Optional<Flight> flightOptional = findOne(entity.getId());

        if (flightOptional.isPresent()) {
            try {
                Connection connection = JDBCconnection.getConnection();
                String query = "UPDATE Flights SET destination = ?, date = ?, availableSeats = ? WHERE id = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, entity.getDestination());
                statement.setObject(2, entity.getDateTime());
                statement.setInt(3, entity.getAvailableSeats());
                statement.setString(4, entity.getId().toString());
                statement.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error DB "+e);
            }
        }

        return flightOptional;
    }
}
