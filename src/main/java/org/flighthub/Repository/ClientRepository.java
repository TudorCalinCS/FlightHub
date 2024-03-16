package org.flighthub.Repository;

import org.flighthub.Domain.Client;
import org.flighthub.Utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ClientRepository implements IClientRepository{
    private final JdbcUtils JDBCconnection;

    public ClientRepository(JdbcUtils connection) {
        this.JDBCconnection = connection;
    }

    @Override
    public Optional<Client> findOne(UUID id) {
        try {
            Connection connection = JDBCconnection.getConnection();
            String query = "SELECT * FROM Clients WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, id.toString());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Client client = new Client(resultSet.getString("name"), resultSet.getString("address"));
                client.setId(UUID.fromString(resultSet.getString("id")));
                return Optional.of(client);
            }
        } catch (SQLException e) {
            System.out.println("Error DB "+e);
        }

        return Optional.empty();
    }

    @Override
    public Iterable<Client> findAll() {
        List<Client> clients = new ArrayList<>();

        try {
            Connection connection = JDBCconnection.getConnection();
            String query = "SELECT * FROM Clients";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Client client = new Client(resultSet.getString("name"), resultSet.getString("address"));
                client.setId(UUID.fromString(resultSet.getString("id")));
                clients.add(client);
            }
        } catch (SQLException e) {
            System.out.println("Error DB "+e);
        }

        return clients;
    }

    @Override
    public Client save(Client entity) {
        try {
            Connection connection = JDBCconnection.getConnection();
            String query = "INSERT INTO Clients (id, name, address) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, entity.getId().toString());
            statement.setString(2, entity.getName());
            statement.setString(3, entity.getAddress());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error DB "+e);
        }

        return entity;
    }

    @Override
    public Optional<Client> delete(UUID id) {
        Optional<Client> clientOptional = findOne(id);

        if (clientOptional.isPresent()) {
            try {
                Connection connection = JDBCconnection.getConnection();
                String query = "DELETE FROM Clients WHERE id = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, id.toString());
                statement.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error DB "+e);
            }
        }

        return clientOptional;
    }

    @Override
    public Optional<Client> update(Client entity) {
        Optional<Client> clientOptional = findOne(entity.getId());

        if (clientOptional.isPresent()) {
            try {
                Connection connection = JDBCconnection.getConnection();
                String query = "UPDATE Clients SET name = ?, address = ? WHERE id = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, entity.getName());
                statement.setString(2, entity.getAddress());
                statement.setString(3, entity.getId().toString());
                statement.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error DB "+e);
            }
        }

        return clientOptional;
    }
}
