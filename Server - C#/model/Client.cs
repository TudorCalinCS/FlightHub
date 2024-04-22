using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace FlightHubC_.Domain
{
    [Serializable]

    public class Client : Entity<Guid>
    {
        public string Name { get; set; }
        public string Address { get; set; }

        public Client(string name, string address)
        {
            Name = name;
            Address = address;
        }
    }
}
