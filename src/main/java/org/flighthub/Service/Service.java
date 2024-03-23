package org.flighthub.Service;
import org.flighthub.Domain.Agent;
import org.flighthub.Repository.*;

import java.sql.SQLException;

public class Service {
    private AgentRepository agentRepository;
    private ClientRepository clientRepository;
    private FlightRepository flightRepository;
    private TicketRepository ticketRepository;

    public Service(AgentRepository agentRepository, ClientRepository clientRepository, FlightRepository flightRepository, TicketRepository ticketRepository) {
        this.agentRepository = agentRepository;
        this.clientRepository = clientRepository;
        this.flightRepository = flightRepository;
        this.ticketRepository = ticketRepository;
    }

    //Login the Agent
    public Agent login(String username, String password){
        Agent agent = agentRepository.login(username,password);
        return agent;
    }
    //Save one Agent
    public void saveAgent(String username,String passwword){
        Agent agent=new Agent(username);
        agentRepository.save(agent,passwword);
    }
}
