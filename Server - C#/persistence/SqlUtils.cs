using System;
using System.Data.SqlClient;
using System.IO;

namespace FlightHubC_.Utils
{
    public class SqlUtils
    {
        private string connectionString;

        public SqlUtils(string conString)
        {

            this.connectionString = conString;
        }

        public SqlConnection GetConnection()
        {
            try
            {
                return new SqlConnection(connectionString);
            }
            catch (Exception e)
            {
                Console.WriteLine("Error creating SQL connection: " + e.Message);
                throw;
            }
        }
    }
}
