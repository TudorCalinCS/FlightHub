using FlightHubC_.Domain;
using System;
using System.Collections.Generic;

namespace FlightHubC_.Repository
{
    public interface IAgentRepository : IRepository<Guid, Agent>
    {
        Agent FindOne(Guid id);
        Agent FindByUsername(string username);
        IEnumerable<Agent> FindAll();
        Agent Save(Agent entity);
        Agent Save(Agent entity, String password);
        Agent Delete(Guid id);
        Agent Update(Agent entity);
        Agent Login(string username, string password);

    }
}
