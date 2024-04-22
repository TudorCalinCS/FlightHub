using FlightHubC_.Domain;
using FlightHubC_.Repository;
using System.Collections.Generic;
using System;
namespace FlightHubC_.Repository
{
    public interface IFlightRepository : IRepository<Guid, Flight>
    {
        Flight FindOne(Guid id);
        IEnumerable<Flight> FindByDestinationAndDepartureDate(string destination, DateTime departureDate);

        IEnumerable<Flight> FindAll();
        Flight Save(Flight entity);
        Flight Delete(Guid id);
        Flight Update(Flight entity);
    }
}