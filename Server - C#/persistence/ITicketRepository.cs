using FlightHubC_.Domain;
using FlightHubC_.Repository;
using System.Collections.Generic;
using System;
namespace FlightHubC_.Repository
{

    public interface ITicketRepository : IRepository<Guid, Ticket>
    {
        Ticket FindOne(Guid id);
        IEnumerable<Ticket> FindAll();
        Ticket Save(Ticket entity);
        Ticket Delete(Guid id);
        Ticket Update(Ticket entity);
    }
}