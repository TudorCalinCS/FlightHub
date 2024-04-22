using FlightHubC_.Domain;
using FlightHubC_.Repository;
using System.Collections.Generic;
using System;
namespace FlightHubC_.Repository
{
    public interface IClientRepository : IRepository<Guid, Client>
    {
        Client FindOne(Guid id);
        IEnumerable<Client> FindAll();
        Client Save(Client entity);
        Client Delete(Guid id);
        Client Update(Client entity);
    }
}