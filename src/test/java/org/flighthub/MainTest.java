package org.flighthub;

import org.flighthub.Domain.Agent;
import org.flighthub.Domain.Client;
import org.flighthub.Domain.Flight;
import org.flighthub.Domain.Ticket;
import org.flighthub.Repository.*;
import org.flighthub.Utils.JdbcUtils;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void testRepositories() throws IOException, SQLException {
        Properties props = new Properties();
        props.load(new FileInputStream("src/main/java/org/flighthub/jdbc.properties"));
        JdbcUtils jdbcUtils = new JdbcUtils(props);

        AgentRepository agentRepository=new AgentRepository(jdbcUtils);
        Agent agent = new Agent("agent1");
        agentRepository.save(agent);
        List<Agent> agentList = (List<Agent>) agentRepository.findAll();
        assertEquals(agentList.size(), 1);

        ClientRepository clientRepository=new ClientRepository(jdbcUtils);
        Client client1 = new Client("client1", "adress1");
        clientRepository.save(client1);
        List<Client> clientList = (List<Client>) clientRepository.findAll();
        assertEquals(clientList.size(), 1);

        FlightRepository flightRepository=new FlightRepository(jdbcUtils);
        Flight flight1 = new Flight("destination1", LocalDateTime.now(), 100);
        flightRepository.save(flight1);
        List<Flight> flightList= (List<Flight>) flightRepository.findAll();
        assertEquals(flightList.size(),1);

        TicketRepository ticketRepository=new TicketRepository(jdbcUtils);
        String[] tourists = {"John Doe", "Jane Smith", "Alice Johnson"};
        Ticket ticket1 = new Ticket(client1,tourists,flight1,50);
        ticketRepository.save(ticket1);
        List<Ticket> ticketList= (List<Ticket>) ticketRepository.findAll();
        assertEquals(ticketList.size(),1);

        jdbcUtils.getConnection().prepareStatement("DELETE FROM Ticket").executeUpdate();
        jdbcUtils.getConnection().prepareStatement("DELETE FROM Flight").executeUpdate();
        jdbcUtils.getConnection().prepareStatement("DELETE FROM Client").executeUpdate();
        jdbcUtils.getConnection().prepareStatement("DELETE FROM Agent").executeUpdate();

    }
}