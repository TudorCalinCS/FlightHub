package org.flighthub.Repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flighthub.Domain.Agent;
import org.flighthub.Repository.IAgentRepository;
import org.flighthub.Utils.JdbcUtils;
import org.mindrot.jbcrypt.BCrypt;

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
    private static final Logger logger = LogManager.getLogger();


    public AgentRepository(JdbcUtils connection) {
        this.JDBCconnection = connection;
    }

    @Override
    public Optional<Agent> findOne(UUID id) {
        try {
            logger.traceEntry();
            logger.info("trying to find one Agent");
            Connection connection = JDBCconnection.getConnection();
            String query = "SELECT * FROM Agent WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, id.toString());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Agent agent = new Agent(resultSet.getString("username"));
                agent.setId(UUID.fromString(resultSet.getString("id")));
                return Optional.of(agent);
            }
        } catch (SQLException e) {
            System.out.println("Error Repo: " + e);
            logger.error(e);

        }

        return Optional.empty();
    }

    @Override
    public Optional<Agent> findOneByUsername(String username) {
        try {
            logger.traceEntry();
            logger.info("trying to find one Agent by username");
            Connection connection = JDBCconnection.getConnection();
            String query = "SELECT * FROM Agent WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Agent agent = new Agent(resultSet.getString("username"));
                agent.setId(UUID.fromString(resultSet.getString("id")));
                return Optional.of(agent);
            }
        } catch (SQLException e) {
            System.out.println("Error Repo " + e);
            logger.error(e);
        }

        return Optional.empty();
    }


    @Override
    public Iterable<Agent> findAll() {
        List<Agent> agents = new ArrayList<>();

        try {
            logger.traceEntry();
            logger.info("trying to find multiple Agents");
            Connection connection = JDBCconnection.getConnection();
            String query = "SELECT * FROM Agent";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Agent agent = new Agent(resultSet.getString("username"));
                agent.setId(UUID.fromString(resultSet.getString("id")));
                agents.add(agent);
            }
        } catch (SQLException e) {
            System.out.println("Error Repo " + e);
            logger.error(e);
        }

        return agents;
    }

    @Override
    public Agent save(Agent entity) {
        return null;
    }

    @Override
    public Agent save(Agent entity, String password) {
        try {
            logger.traceEntry();
            logger.info("trying to save one Agent");
            Connection connection = JDBCconnection.getConnection();
            String query = "INSERT INTO Agent (id, username, password) VALUES (?,?,?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, UUID.randomUUID().toString());
            statement.setString(2, entity.getUsername());
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            statement.setString(3, hashedPassword);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error Repo " + e);
            logger.error(e);
        }

        return entity;
    }


    @Override
    public Optional<Agent> delete(UUID id) {
        Optional<Agent> agentOptional = findOne(id);

        if (agentOptional.isPresent()) {
            try {
                logger.traceEntry();
                logger.info("trying to delete one Agent");
                Connection connection = JDBCconnection.getConnection();
                String query = "DELETE FROM Agent WHERE id = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, id.toString());
                statement.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error Repo " + e);
                logger.error(e);
            }
        }

        return agentOptional;
    }

    @Override
    public Optional<Agent> update(Agent entity) {
        Optional<Agent> agentOptional = findOne(entity.getId());

        if (agentOptional.isPresent()) {
            try {
                logger.traceEntry();
                logger.info("trying to update one Agent");
                Connection connection = JDBCconnection.getConnection();
                String query = "UPDATE Agent SET username = ? WHERE id = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, entity.getUsername());
                statement.setString(2, entity.getId().toString());
                statement.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error Repo " + e);
                logger.error(e);
            }
        }

        return agentOptional;
    }

    @Override
    public Agent login(String username, String password) {
        try {
            logger.traceEntry();
            logger.info("trying to login Agent");
            Connection connection = JDBCconnection.getConnection();
            String query = "SELECT * FROM Agent WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String hashedPassword = resultSet.getString("password");
                if (BCrypt.checkpw(password, hashedPassword)) {
                    Agent agent = new Agent(resultSet.getString("username"));
                    agent.setId(UUID.fromString(resultSet.getString("id")));
                    return agent;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error Repo " + e);
            logger.error(e);
        }

        return null;
    }

}
