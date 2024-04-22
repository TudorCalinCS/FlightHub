using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using FlightHubC_.Domain;

namespace chat.services
{
    public interface IServices
    
    {
        Agent login(String username,String password, IObserver client);
        void logout(Agent agent, IObserver client);
        List<Flight> getFlights();
        List<Flight> SearchFlight(string destination, DateTime dateTime);
        bool BuyTicket(string clientsname, string clientsaddress, string touristsname, int numberOfSeats, Flight flight);
        Flight findByDetails(string destination, DateTime dateTime, int availableSeats, string airport);
        Flight findFlightId(String id);
    }
}
