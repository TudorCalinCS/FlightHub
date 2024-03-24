package org.flighthub.Service;
import org.flighthub.Domain.*;
import org.flighthub.Repository.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Service {
    private AgentRepository agentRepository;
    private ClientRepository clientRepository;
    private FlightRepository flightRepository;
    private TicketRepository ticketRepository;
    private TouristRepository touristRepository;

    public Service(AgentRepository agentRepository, ClientRepository clientRepository, FlightRepository flightRepository, TicketRepository ticketRepository,TouristRepository touristRepository) {
        this.agentRepository = agentRepository;
        this.clientRepository = clientRepository;
        this.flightRepository = flightRepository;
        this.ticketRepository = ticketRepository;
        this.touristRepository=touristRepository;
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
    //Get all Flights - not showing flights with no available seats
    public List<Flight> getFlights(){
        return filterFlights((List<Flight>) flightRepository.findAll());
    }
    //Search flight by destination & date
    public List<Flight> searchFlight(String destination, LocalDate departureDate){
        return filterFlights((List<Flight>) flightRepository.findByDestinationAndDepartureDate(destination,departureDate));
    }
    //Buy ticket
    public boolean buyTicket(String clientName,String clientAddress,String touristsName,int seats,Flight flight){
        try{
        Client client=new Client(clientName,clientAddress);
        clientRepository.save(client);
        String[] touristsNameArray = touristsName.split(", ");
        List<Tourist> touristList=new ArrayList<>();
        for(String name:touristsNameArray){
            Tourist tourist=new Tourist(name);
            touristRepository.save(tourist);
            touristList.add(tourist);
        }
        Ticket ticket=new Ticket(client,touristList,flight,seats);
        ticketRepository.save(ticket);
        return true;}
        catch (Exception exception){
            return false;
        }
    }
    // Filter flights
    public List<Flight> filterFlights(List<Flight> flights) {
        List<Flight> filteredFlights = new ArrayList<>();
        for (Flight flight : flights) {
            if (flight.getAvailableSeats() > 0) {
                filteredFlights.add(flight);
            }
        }
        return filteredFlights;
    }
    //Add entities
    public void addEntities() {
        Agent agent = new Agent("TudorCalin");
        agentRepository.save(agent, "password");

        Client client1 = new Client("Alice Johnson", "123 Main St, Springfield, IL 62701, USA");
        clientRepository.save(client1);

        Client client2 = new Client("Bob Smith", "456 Elm St, New York, NY 10001, USA");
        clientRepository.save(client2);

        Flight flight1 = new Flight("New York", LocalDateTime.of(2024, 3, 15, 10, 0), 200, "JFK Airport");
        flightRepository.save(flight1);

        Flight flight2 = new Flight("Los Angeles", LocalDateTime.of(2024, 4, 20, 12, 30), 150, "LAX Airport");
        flightRepository.save(flight2);

        Flight flight3 = new Flight("Chicago", LocalDateTime.of(2024, 5, 10, 9, 15), 180, "ORD Airport");
        flightRepository.save(flight3);

        Flight flight4 = new Flight("Miami", LocalDateTime.of(2024, 6, 5, 8, 45), 220, "MIA Airport");
        flightRepository.save(flight4);

        Flight flight5 = new Flight("Seattle", LocalDateTime.of(2024, 7, 18, 14, 20), 190, "SEA Airport");
        flightRepository.save(flight5);

        Flight flight6 = new Flight("San Francisco", LocalDateTime.of(2024, 8, 22, 11, 10), 170, "SFO Airport");
        flightRepository.save(flight6);

        Flight flight7 = new Flight("Las Vegas", LocalDateTime.of(2024, 9, 30, 16, 0), 160, "LAS Airport");
        flightRepository.save(flight7);

        Flight flight8 = new Flight("Orlando", LocalDateTime.of(2024, 10, 12, 13, 45), 210, "MCO Airport");
        flightRepository.save(flight8);

        Tourist tourist1 = new Tourist("Emily");
        touristRepository.save(tourist1);

        Tourist tourist2 = new Tourist("Michael");
        touristRepository.save(tourist2);
    }

}
