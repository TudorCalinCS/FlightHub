package org.flighthub.Repository;

import org.apache.logging.log4j.LogManager;
import org.flighthub.Domain.Tourist;
import org.flighthub.Utils.JdbcUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class TouristRepository implements ITouristRepository {
    private final JdbcUtils JDBCconnection;

    private static final Logger logger = LogManager.getLogger();

    public TouristRepository(JdbcUtils connection) {
        this.JDBCconnection = connection;
    }

    @Override
    public Optional<Tourist> findOne(UUID uuid) {
        return Optional.empty();
    }

    @Override
    public Iterable<Tourist> findAll() {
        return null;
    }

    @Override
    public Tourist save(Tourist tourist) {
        try {
            logger.traceEntry();
            logger.info("Trying to save a tourist");
            Connection connection = JDBCconnection.getConnection();
            String query = "INSERT INTO Tourist (id, name) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            UUID uuid=UUID.randomUUID();
            statement.setString(1, uuid.toString());
            statement.setString(2, tourist.getName());
            statement.executeUpdate();
            tourist.setId(uuid);
            return tourist;
        } catch (SQLException e) {
            System.out.println("Error Repo " + e);
            logger.error(e);
            return null;
        }
    }

    @Override
    public Optional<Tourist> delete(UUID uuid) {
        return Optional.empty();
    }

    @Override
    public Optional<Tourist> update(Tourist entity) {
        return Optional.empty();
    }
}