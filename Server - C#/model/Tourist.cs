using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace FlightHubC_.Domain
{
    [Serializable]

    public class Tourist : Entity<Guid>
    {
        public string name { get; set; }

        public Tourist(string name)
        {
            this.name = name;
        }

    }
}
