package org.flighthub;

import org.flighthub.Utils.JdbcUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import org.flighthub.Utils.TestRepository;

public class Main {
    public static void main(String[] args) throws SQLException {

        Properties props = new Properties();
        try {
            props.load(new FileInputStream("src/main/java/org/flighthub/jdbc.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        JdbcUtils jdbcUtils = new JdbcUtils(props);


        try (Connection connection = jdbcUtils.getConnection()) {
            System.out.println("DB connected");

            /*
            TestRepository testRepository=new TestRepository(jdbcUtils);
            testRepository.setUp();
            testRepository.testAgentRepository();
            testRepository.testClientRepository();
            */


        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

}