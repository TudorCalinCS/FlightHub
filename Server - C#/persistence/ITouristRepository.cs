using FlightHubC_.Domain;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace FlightHubC_.Repository
{
    public interface ITouristRepository
    {
        Tourist save(Tourist entity);
    }
}
