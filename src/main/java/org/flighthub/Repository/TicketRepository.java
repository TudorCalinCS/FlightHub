package org.flighthub.Repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flighthub.Domain.Client;
import org.flighthub.Domain.Flight;
import org.flighthub.Domain.Ticket;
import org.flighthub.Domain.Tourist;
import org.flighthub.Utils.JdbcUtils;
import org.flighthub.Repository.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import org.flighthub.Domain.Tourist;
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

                // Extrageți lista de id-uri ale turiștilor din tabela de asociere Ticket_Tourist
                String queryTourists = "SELECT tourist_id FROM Ticket_Tourist WHERE ticket_id = ?";
                PreparedStatement statementTourists = connection.prepareStatement(queryTourists);
                statementTourists.setString(1, id.toString());
                ResultSet resultSetTourists = statementTourists.executeQuery();

                List<Tourist> touristList=new ArrayList<>();
                while (resultSetTourists.next()) {
                    // Pentru fiecare id de turist, găsiți numele corespunzător în tabela Tourist
                    String queryTouristName = "SELECT name FROM Tourist WHERE id = ?";
                    PreparedStatement statementTouristName = connection.prepareStatement(queryTouristName);
                    statementTouristName.setString(1, resultSetTourists.getString("tourist_id"));
                    ResultSet resultSetTouristName = statementTouristName.executeQuery();

                    if (resultSetTouristName.next()) {
                        Tourist tourist=new Tourist(resultSetTouristName.getString("name"));
                        tourist.setId(UUID.fromString(queryTourists));
                        touristList.add(tourist);
                    }
                }


                int seats = resultSet.getInt("seats");
                Ticket ticket = new Ticket(client1, touristList, flight1, seats);
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
                /// Extrageți lista de id-uri ale turiștilor din tabela de asociere Ticket_Tourist
                String queryTourists = "SELECT tourist_id FROM Ticket_Tourist WHERE ticket_id = ?";
                PreparedStatement statementTourists = connection.prepareStatement(queryTourists);
                statementTourists.setString(1, resultSet.getString("id"));
                ResultSet resultSetTourists = statementTourists.executeQuery();

                List<Tourist> touristList=new ArrayList<>();
                while (resultSetTourists.next()) {
                    // Pentru fiecare id de turist, găsiți numele corespunzător în tabela Tourist
                    String queryTouristName = "SELECT name FROM Tourist WHERE id = ?";
                    PreparedStatement statementTouristName = connection.prepareStatement(queryTouristName);
                    statementTouristName.setString(1, resultSetTourists.getString("tourist_id"));
                    ResultSet resultSetTouristName = statementTouristName.executeQuery();

                    if (resultSetTouristName.next()) {
                        Tourist tourist=new Tourist(resultSetTouristName.getString("name"));
                        //tourist.setId(UUID.fromString(queryTourists));
                        tourist.setId(UUID.fromString(resultSetTourists.getString("tourist_id")));
                        touristList.add(tourist);
                    }
                }

                int seats = resultSet.getInt("seats");
                Ticket ticket = new Ticket(client1, touristList, flight1, seats);
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
            String query = "INSERT INTO Ticket (id, clientId, flightId, seats) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            UUID ticketId = UUID.randomUUID(); // Generați un id unic pentru bilet
            statement.setString(1, ticketId.toString());
            statement.setString(2, entity.getClient().getId().toString());
            statement.setString(3, entity.getFlight().getId().toString());
            statement.setInt(4, entity.getSeats());
            entity.setId(ticketId); // Setează id-ul biletului cu id-ul generat
            statement.executeUpdate();

            // Salvare asociere turisti-bilet
            for (Tourist tourist : entity.getTouristList()) {
                String insertQuery = "INSERT INTO Ticket_Tourist (ticket_id, tourist_id) VALUES (?, ?)";
                PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                insertStatement.setString(1, ticketId.toString());
                insertStatement.setString(2, tourist.getId().toString());
                insertStatement.executeUpdate();
            }
            FlightRepository flightRepository = new FlightRepository(JDBCconnection);
            Optional<Flight> optionalFlight = flightRepository.findOne(entity.getFlight().getId());
            if (optionalFlight.isPresent()) {
                Flight flight = optionalFlight.get();
                int newAvailableSeats = flight.getAvailableSeats() - entity.getSeats();
                flightRepository.updateAvailableSeats(flight.getId(), newAvailableSeats);
            }
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
                String query = "UPDATE Ticket SET clientId = ?,  flightId = ?, seats = ? WHERE id = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, entity.getClient().getId().toString());
                statement.setString(2, entity.getFlight().getId().toString());
                statement.setObject(3, entity.getSeats());
                statement.setString(4, entity.getId().toString());
                statement.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error Repo " + e);
                logger.error(e);
            }
        }

        return ticketOptional;
    }
}
