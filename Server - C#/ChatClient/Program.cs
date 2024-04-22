using FlightHubC_.Utils;
using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Threading.Tasks;
using System.Windows.Forms;
using log4net;
using FlightHubC_.Repository;
using FlightHubC_.ServiceNamespace;

namespace FlightHubC_
{
    internal static class Program
    {
        private static readonly log4net.ILog log = LogManager.GetLogger(typeof(Program));
        [STAThread]
        static void Main()
        {
            log.Info("Entering Main");
            SqlUtils sqlConnection = new SqlUtils(@"Server=DESKTOP-07IG6FN;Database=FlightHub;Integrated Security = true; TrustServerCertificate = true");

            try
            {
                using (SqlConnection connection = sqlConnection.GetConnection())
                {
                    connection.Open();
                    log.Info("DB Connected");

                }
            }
            catch (Exception e)
            {
                //Console.WriteLine("Error DB: " + e.Message);
                log.Info("Error connecting DB: " + e.Message);

            }
            var agentRepository = new AgentRepository(sqlConnection);
            var clientRepository = new ClientRepository(sqlConnection);
            var ticketRepository = new TicketRepository(sqlConnection);
            var touristRepository = new TouristRepository(sqlConnection);
            var flightRepository = new FlightRepository(sqlConnection);
            var service = new Service(agentRepository, clientRepository, flightRepository, ticketRepository, touristRepository);
            //service.AddEntities();
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            Application.Run(new LoginForm(service));
        }
    }
}
