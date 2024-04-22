using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace FlightHubC_.Domain
{
    [Serializable]

    public class Flight : Entity<Guid>
    {
        public string Destination { get; set; }
        public DateTime DateTime { get; set; }
        public int AvailableSeats { get; set; }

        public string Airport;
        public Flight(string destination, DateTime date, int availableSeats, string airport)
        {
            Destination = destination;
            DateTime = date;
            AvailableSeats = availableSeats;
            Airport = airport;
        }
    }
}
