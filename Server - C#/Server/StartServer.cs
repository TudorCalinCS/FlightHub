using System;
using System.Collections.Generic;
using System.Configuration;
using System.Net.Sockets;
using System.Data.SqlClient;
using System.Threading;

using chat.services;
using chat.network.client;

using ServerTemplate;
namespace chat
{
    using FlightHubC_.Domain;
    using FlightHubC_.Repository;
    using FlightHubC_.ServiceNamespace;
    using FlightHubC_.Utils;
    class StartServer
    {
        private static int DEFAULT_PORT = 55556;
        private static String DEFAULT_IP = "127.0.0.1";
        static void Main(string[] args)
        {

            Console.WriteLine("Reading properties from app.config ...");
            int port = DEFAULT_PORT;
            String ip = DEFAULT_IP;
            String portS = ConfigurationManager.AppSettings["port"];
            if (portS == null)
            {
                Console.WriteLine("Port property not set. Using default value " + DEFAULT_PORT);
            }
            else
            {
                bool result = Int32.TryParse(portS, out port);
                if (!result)
                {
                    Console.WriteLine("Port property not a number. Using default value " + DEFAULT_PORT);
                    port = DEFAULT_PORT;
                    Console.WriteLine("Portul " + port);
                }
            }
            String ipS = ConfigurationManager.AppSettings["ip"];

            if (ipS == null)
            {
                Console.WriteLine("Port property not set. Using default value " + DEFAULT_IP);
            }
            SqlUtils sqlConnection = new SqlUtils(@"Server=DESKTOP-07IG6FN;Database=FlightHub;Integrated Security = true; TrustServerCertificate = true");
            try
            {
                using (SqlConnection connection = sqlConnection.GetConnection())
                {
                    connection.Open();
                    //log.Info("DB Connected");
                    Console.WriteLine("DB Connected");
                }
            }
            catch (Exception e)
            {
                Console.WriteLine("Error DB: " + e.Message);
                //log.Info("Error connecting DB: " + e.Message);

            }
            Console.WriteLine("Configuration Settings for database {0}", GetConnectionStringByName("chatDB"));
            IDictionary<String, string> props = new SortedList<String, String>();
            props.Add("ConnectionString", GetConnectionStringByName("chatDB"));


            var agentRepository = new AgentRepository(sqlConnection);
            var clientRepository = new ClientRepository(sqlConnection);
            var ticketRepository = new TicketRepository(sqlConnection);
            var touristRepository = new TouristRepository(sqlConnection);
            var flightRepository = new FlightRepository(sqlConnection);

            IServices serviceImpl = new Service(agentRepository, clientRepository, flightRepository, ticketRepository, touristRepository);
            Console.WriteLine("Starting server on IP {0} and port {1}", ip, port);
            SerialChatServer server = new SerialChatServer(ip, port, serviceImpl);
            server.Start();
            Console.WriteLine("Server started ...");
            Console.ReadLine();

        }



        static string GetConnectionStringByName(string name)
        {
            // Assume failure.
            string returnValue = null;

            // Look for the name in the connectionStrings section.
            ConnectionStringSettings settings = ConfigurationManager.ConnectionStrings[name];

            // If found, return the connection string.
            if (settings != null)
                returnValue = settings.ConnectionString;

            return returnValue;
        }
    }

    public class SerialChatServer : ConcurrentServer
    {
        private IServices server;
        private ClientWorker worker;
        public SerialChatServer(string host, int port, IServices server) : base(host, port)
        {
            this.server = server;
            Console.WriteLine("SerialChatServer...");
        }
        protected override Thread createWorker(TcpClient client)
        {
            worker = new ClientWorker(server, client);
            return new Thread(new ThreadStart(worker.run));
        }
    }

}
