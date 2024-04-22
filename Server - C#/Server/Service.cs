using chat.services;
using FlightHubC_.Domain;
using FlightHubC_.Repository;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace FlightHubC_.ServiceNamespace
{
    public class Service : IServices
    {
        private readonly AgentRepository agentRepository;
        private readonly ClientRepository clientRepository;
        private readonly FlightRepository flightRepository;
        private readonly TicketRepository ticketRepository;
        private readonly TouristRepository touristRepository;

        private readonly IDictionary<String, IObserver> loggedClients;


        public Service(AgentRepository agentRepository, ClientRepository clientRepository, FlightRepository flightRepository, TicketRepository ticketRepository, TouristRepository touristRepository)
        {
            this.agentRepository = agentRepository;
            this.clientRepository = clientRepository;
            this.flightRepository = flightRepository;
            this.ticketRepository = ticketRepository;
            this.touristRepository = touristRepository;
            loggedClients = new Dictionary<String, IObserver>();

        }

        // Login the Agent
        public Agent Login(string username, string password)
        {
            return agentRepository.Login(username, password);
        }

        // Save one Agent
        public void SaveAgent(string username, string password)
        {
            Agent agent = new Agent(username);
            agentRepository.Save(agent, password);
        }

        // Get all Flights - not showing flights with no available seats
        public List<Flight> getFlights()
        {
            return (List<Flight>)flightRepository.FindAll();
        }

        // Search flight by destination & date
        public List<Flight> SearchFlight(string destination, DateTime departureDate)
        {
            return (List<Flight>)flightRepository.FindByDestinationAndDepartureDate(destination, departureDate);
        }

        // Buy ticket
        public bool BuyTicket(string clientName, string clientAddress, string touristsName, int seats, Flight flight)
        {

            Client client = new Client(clientName, clientAddress);
            clientRepository.Save(client);

            string[] touristsNameArray = touristsName.Split(',');
            List<Tourist> touristList = new List<Tourist>();
            foreach (string name in touristsNameArray)
            {
                Tourist tourist = new Tourist(name);
                touristRepository.save(tourist);
                touristList.Add(tourist);
            }

            Ticket ticket = new Ticket(client, touristList, flight, seats);
            ticketRepository.Save(ticket);
            NotifyClientsTicketBought();
            return true;

        }
        public Flight findByDetails(string destination, DateTime departureDateTime, int availableSeats, string airport)
        {
            return flightRepository.FindByDetails(destination, departureDateTime, availableSeats, airport);
        }

        // Add entities
        public void AddEntities()
        {
            Agent agent = new Agent("TudorCalin");
            agentRepository.Save(agent, "password");

            Client client1 = new Client("Alice Johnson", "123 Main St, Springfield, IL 62701, USA");
            clientRepository.Save(client1);

            Flight flight1 = new Flight("New York", DateTime.Now.AddDays(1), 100, "NY Airport");

            Flight flight2 = new Flight("Los Angeles", DateTime.Now.AddDays(2), 150, "LA Airport");
            flightRepository.Save(flight2);

            Flight flight3 = new Flight("London", DateTime.Now.AddDays(3), 200, "Heathrow Airport");
            flightRepository.Save(flight3);

            Flight flight4 = new Flight("Paris", DateTime.Now.AddDays(4), 120, "Charles de Gaulle Airport");
            flightRepository.Save(flight4);

            Flight flight5 = new Flight("Tokyo", DateTime.Now.AddDays(5), 180, "Narita International Airport");
            flightRepository.Save(flight5);

            Flight flight6 = new Flight("Dubai", DateTime.Now.AddDays(6), 250, "Dubai International Airport");
            flightRepository.Save(flight6);

            Flight flight7 = new Flight("Sydney", DateTime.Now.AddDays(7), 180, "Sydney Airport");
            flightRepository.Save(flight7);
            
        }
        public Flight findFlightId(String id)
        {
            return flightRepository.FindOne(Guid.Parse(id));
        }

        public void logout(Agent agent, IObserver client)
        {
            loggedClients.Remove(agent.Id.ToString());
        }


        public Agent login(string username, string password, IObserver client)
        {
            Agent agent = agentRepository.Login(username,password);
            if (agent != null)
            {
               
                loggedClients[agent.Id.ToString()] = client;
                return agent;
            }
            else
                throw new TException("Authentication failed.");
        }
        public void NotifyClientsTicketBought()
        {
            foreach(var observer in loggedClients.Values)
            {
                Task.Run(() => observer.notifyAgents());
            }
        }
    }
}
