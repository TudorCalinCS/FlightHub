package org.flighthub.Utils;

import org.flighthub.Domain.Agent;
import org.flighthub.Domain.Client;
import org.flighthub.Domain.Flight;
import org.flighthub.Repository.AgentRepository;
//import org.flighthub.Repository.ClientRepository;
import org.flighthub.Repository.FlightRepository;
import org.flighthub.Repository.TicketRepository;
//import org.junit.jupiter.*;
//import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class TestRepository {

    public TestRepository(JdbcUtils jdbcUtils) {
        this.jdbcUtils = jdbcUtils;
    }
    private JdbcUtils jdbcUtils;


    private AgentRepository agentRepository;
    //private ClientRepository clientRepository;
    private FlightRepository flightRepository;
    private TicketRepository ticketRepository;

    public void setUp() throws SQLException {
        agentRepository = new AgentRepository(jdbcUtils);
        //clientRepository = new ClientRepository(jdbcUtils);
        flightRepository = new FlightRepository(jdbcUtils);
        ticketRepository = new TicketRepository(jdbcUtils);
    }
    //@Test
    public void testAgentRepository() throws SQLException {
        Agent agent = new Agent("agent1");
        agentRepository.save(agent);
        List<Agent> agentList = (List<Agent>) agentRepository.findAll();
        //assertEquals(agentList.size(), 1);

    }
    //@Test
    public void testClientRepository() throws SQLException {
        Client client1 = new Client("client1", "adress1");
        //clientRepository.save(client1);
        //List<Client> clientList = (List<Client>) clientRepository.findAll();
        //assertEquals(clientList.size(), 1);
    }
    //@Test
    public void testFlightRepository() throws SQLException {
        Flight flight1 = new Flight("destination1", LocalDateTime.now(), 100);
        flightRepository.save(flight1);
        List<Flight> flightList= (List<Flight>) flightRepository.findAll();
        //assertEquals(flightList.size(),1);
    }

    public void testTicketRepository() throws SQLException {

    }

}
