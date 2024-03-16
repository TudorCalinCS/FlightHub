package org.flighthub;
import org.flighthub.Utils.JdbcUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {


        // Properties file
        Properties props = new Properties();
        try {
            props.load(new FileInputStream("src/main/java/org/flighthub/jdbc.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Creează o instanță de JdbcUtils cu proprietățile încărcate
        JdbcUtils jdbcUtils = new JdbcUtils(props);

        // Obține conexiunea la baza de date
        try (Connection connection = jdbcUtils.getConnection()) {
            System.out.println("Conexiunea la baza de date a fost stabilită cu succes!");

            // Aici poți executa interogări sau alte operații asupra bazei de date
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}