using FlightHubC_.Domain;
using FlightHubC_.Utils;
//using log4net;
using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace FlightHubC_.Repository
{
    public class TouristRepository : ITouristRepository
    {
        private readonly SqlUtils sqlUtils;
       // private static readonly log4net.ILog log = LogManager.GetLogger(typeof(Program));

        public TouristRepository(SqlUtils sqlUtils)
        {
            this.sqlUtils = sqlUtils;
        }
        public Tourist save(Tourist entity)
        {
            using (SqlConnection connection = sqlUtils.GetConnection())
            {
                String query = "INSERT INTO Tourist (id, name) VALUES (@Id, @Name)";
                SqlCommand command = new SqlCommand(query, connection);
                Guid guid = Guid.NewGuid();
                command.Parameters.AddWithValue("@Id", guid);
                command.Parameters.AddWithValue("@Name", entity.name);
                entity.Id = guid;
                connection.Open();
                command.ExecuteNonQuery();
                return entity;
            }
        }
    }
}
