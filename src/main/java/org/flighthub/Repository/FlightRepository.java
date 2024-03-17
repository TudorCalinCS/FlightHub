package org.flighthub.Repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private static final Logger logger = LogManager.getLogger();

    public FlightRepository(JdbcUtils connection) {
        this.JDBCconnection = connection;
    }

    @Override
    public Optional<Flight> findOne(UUID id) {
        try {
            logger.traceEntry();
            logger.info("trying to find one Flight");
            Connection connection = JDBCconnection.getConnection();
            String query = "SELECT * FROM Flight WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, id.toString());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Flight flight = new Flight(resultSet.getString("destination"),
                        resultSet.getObject("dateTime", LocalDateTime.class),
                        resultSet.getInt("availableSeats"));
                flight.setId(UUID.fromString(resultSet.getString("id")));
                return Optional.of(flight);
            }
        } catch (SQLException e) {
            System.out.println("Error Repo " + e);
            logger.error(e);
        }

        return Optional.empty();
    }

    @Override
    public Iterable<Flight> findAll() {
        List<Flight> flights = new ArrayList<>();

        try {
            logger.traceEntry();
            logger.info("trying to find multiple Flights");
            Connection connection = JDBCconnection.getConnection();
            String query = "SELECT * FROM Flight";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Flight flight = new Flight(resultSet.getString("destination"),
                        resultSet.getObject("dateTime", LocalDateTime.class),
                        resultSet.getInt("availableSeats"));
                flight.setId(UUID.fromString(resultSet.getString("id")));
                flights.add(flight);
            }
        } catch (SQLException e) {
            System.out.println("Error Repo " + e);
            logger.error(e);
        }

        return flights;
    }

    @Override
    public Flight save(Flight entity) {
        try {
            logger.traceEntry();
            logger.info("trying to save one Flight");
            Connection connection = JDBCconnection.getConnection();
            String query = "INSERT INTO Flight (id, destination, dateTime, availableSeats) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            UUID uuid = UUID.randomUUID();
            entity.setId(uuid);
            statement.setString(1, uuid.toString());
            statement.setString(2, entity.getDestination());
            statement.setObject(3, entity.getDateTime());
            statement.setInt(4, entity.getAvailableSeats());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error Repo " + e);
            logger.error(e);
        }

        return entity;
    }

    @Override
    public Optional<Flight> delete(UUID id) {
        Optional<Flight> flightOptional = findOne(id);

        if (flightOptional.isPresent()) {
            try {
                logger.traceEntry();
                logger.info("trying to delete one Flight");
                Connection connection = JDBCconnection.getConnection();
                String query = "DELETE FROM Flight WHERE id = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, id.toString());
                statement.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error Repo " + e);
                logger.error(e);
            }
        }

        return flightOptional;
    }

    @Override
    public Optional<Flight> update(Flight entity) {
        Optional<Flight> flightOptional = findOne(entity.getId());

        if (flightOptional.isPresent()) {
            try {
                logger.traceEntry();
                logger.info("trying to update one Flight");
                Connection connection = JDBCconnection.getConnection();
                String query = "UPDATE Flight SET destination = ?, dateTime = ?, availableSeats = ? WHERE id = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, entity.getDestination());
                statement.setObject(2, entity.getDateTime());
                statement.setInt(3, entity.getAvailableSeats());
                statement.setString(4, entity.getId().toString());
                statement.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error Repo " + e);
                logger.error(e);
            }
        }

        return flightOptional;
    }
}
