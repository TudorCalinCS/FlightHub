using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace FlightHubC_.Domain
{
    [Serializable]
    public class Entity<Guid>
    {
        public Guid Id { get; set; }

        public override bool Equals(object obj)
        {
            if (obj == null || !(obj is Entity<Guid>))
                return false;

            return ((Entity<Guid>)obj).Id.Equals(Id);
        }

        public override int GetHashCode()
        {
            return Id.GetHashCode();
        }
    }
}
