package org.flighthub.Repository;

import org.flighthub.Domain.Agent;
import org.flighthub.Repository.IAgentRepository;
import org.flighthub.Utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AgentRepository implements IAgentRepository {
    private final JdbcUtils JDBCconnection;

    public AgentRepository(JdbcUtils connection) {
        this.JDBCconnection = connection;
    }

    @Override
    public Optional<Agent> findOne(UUID id) {
        try {
            Connection connection= JDBCconnection.getConnection();
            String query = "SELECT * FROM Agents WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, id.toString());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Agent agent = new Agent(resultSet.getString("username"));
                agent.setId(UUID.fromString(resultSet.getString("id")));
                return Optional.of(agent);
            }
        } catch (SQLException e) {
            System.out.println("Error DB "+e);
        }

        return Optional.empty();
    }

    @Override
    public Iterable<Agent> findAll() {
        List<Agent> agents = new ArrayList<>();

        try {
            Connection connection= JDBCconnection.getConnection();
            String query = "SELECT * FROM Agents";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Agent agent = new Agent(resultSet.getString("username"));
                agent.setId(UUID.fromString(resultSet.getString("id")));
                agents.add(agent);
            }
        } catch (SQLException e) {
            System.out.println("Error DB "+e);
        }

        return agents;
    }

    @Override
    public Agent save(Agent entity) {
        try {
            Connection connection= JDBCconnection.getConnection();
            String query = "INSERT INTO Agents (id, username) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, entity.getId().toString());
            statement.setString(2, entity.getUsername());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error DB "+e);
        }

        return entity;
    }

    @Override
    public Optional<Agent> delete(UUID id) {
        Optional<Agent> agentOptional = findOne(id);

        if (agentOptional.isPresent()) {
            try {
                Connection connection= JDBCconnection.getConnection();
                String query = "DELETE FROM Agents WHERE id = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, id.toString());
                statement.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error DB "+e);
            }
        }

        return agentOptional;
    }

    @Override
    public Optional<Agent> update(Agent entity) {
        Optional<Agent> agentOptional = findOne(entity.getId());

        if (agentOptional.isPresent()) {
            try {
                Connection connection= JDBCconnection.getConnection();
                String query = "UPDATE Agents SET username = ? WHERE id = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, entity.getUsername());
                statement.setString(2, entity.getId().toString());
                statement.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error DB "+e);
            }
        }

        return agentOptional;
    }
}
