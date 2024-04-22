using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace FlightHubC_.Domain
{
    [Serializable]
    public class Agent : Entity<Guid>
    {
        public string Username { get; set; }
        public Agent(string username)
        {
            this.Username = username;
        }
    }

}
