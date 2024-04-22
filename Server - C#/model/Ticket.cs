using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace FlightHubC_.Domain
{
    [Serializable]

    public class Ticket : Entity<Guid>
    {
        public Client client { get; set; }
        public List<Tourist> tourists { get; set; }
        public Flight flight { get; set; }
        public int seats { get; set; }

        public Ticket(Client client, List<Tourist> tourists, Flight flight, int seats)
        {
            this.client = client;
            this.tourists = tourists;
            this.flight = flight;
            this.seats = seats;
        }
    }
}
