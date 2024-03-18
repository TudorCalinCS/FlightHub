package org.flighthub;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flighthub.Utils.JdbcUtils;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class Main {
    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) throws SQLException {
        logger.traceEntry();

        Properties props = new Properties();
        try {
            props.load(new FileInputStream("src/main/java/org/flighthub/jdbc.properties"));
        } catch (IOException e) {
            logger.error(e);
            return;
        }
        JdbcUtils jdbcUtils = new JdbcUtils(props);


        try (Connection connection = jdbcUtils.getConnection()) {
            System.out.println("DB connected");


        } catch (SQLException e) {
            logger.error(e);
        }


    }

}