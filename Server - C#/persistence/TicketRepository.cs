using FlightHubC_.Domain;
using FlightHubC_.Utils;
//using log4net;
using System;
using System.Collections.Generic;
using System.Data.SqlClient;

namespace FlightHubC_.Repository
{
    public class TicketRepository : ITicketRepository
    {
        private readonly SqlUtils sqlUtils;
        //private static readonly log4net.ILog log = LogManager.GetLogger(typeof(Program));

        public TicketRepository(SqlUtils sqlUtils)
        {
            this.sqlUtils = sqlUtils;
        }

        public Ticket FindOne(Guid id)
        {
            string query = "SELECT * FROM Ticket WHERE Id = @Id";

            using (SqlConnection connection = sqlUtils.GetConnection())
            {
                //log.Info("finding one Ticket");
                connection.Open();
                using (SqlCommand command = new SqlCommand(query, connection))
                {
                    command.Parameters.AddWithValue("@Id", id);
                    using (SqlDataReader reader = command.ExecuteReader())
                    {
                        if (reader.Read())
                        {
                            ClientRepository clientRepository = new ClientRepository(sqlUtils);
                            Client client = clientRepository.FindOne((Guid)reader["clientId"]);
                            FlightRepository flightRepository = new FlightRepository(sqlUtils);
                            Flight flight = flightRepository.FindOne((Guid)reader["flightId"]);
                            int seats = (int)reader["seats"];

                            // Extrageți lista de id-uri ale turiștilor din tabela de asociere Ticket_Tourist
                            List<Guid> touristIds = new List<Guid>();
                            string queryTourists = "SELECT tourist_id FROM Ticket_Tourist WHERE ticket_id = @Id";
                            using (SqlCommand commandTourists = new SqlCommand(queryTourists, connection))
                            {
                                commandTourists.Parameters.AddWithValue("@Id", id);
                                using (SqlDataReader readerTourists = commandTourists.ExecuteReader())
                                {
                                    while (readerTourists.Read())
                                    {
                                        touristIds.Add((Guid)readerTourists["tourist_id"]);
                                    }
                                }
                            }

                            List<Tourist> tourists = new List<Tourist>();
                            foreach (var touristId in touristIds)
                            {
                                string queryTouristName = "SELECT name FROM Tourist WHERE id = @TouristId";
                                using (SqlCommand commandTouristName = new SqlCommand(queryTouristName, connection))
                                {
                                    commandTouristName.Parameters.AddWithValue("@TouristId", touristId);
                                    using (SqlDataReader readerTouristName = commandTouristName.ExecuteReader())
                                    {
                                        if (readerTouristName.Read())
                                        {
                                            Tourist tourist = new Tourist((string)readerTouristName["name"]);
                                            tourist.Id = touristId;
                                            tourists.Add(tourist);
                                        }
                                    }
                                }
                            }

                            Ticket ticket = new Ticket(client, tourists, flight, seats);
                            ticket.Id = id;
                            return ticket;
                        }
                    }
                }
            }
            return null;
        }

        public IEnumerable<Ticket> FindAll()
        {
            List<Ticket> tickets = new List<Ticket>();
            string query = "SELECT * FROM Ticket ";

            using (SqlConnection connection = sqlUtils.GetConnection())
            {
                //log.Info("finding all Tickets");
                connection.Open();
                using (SqlCommand command = new SqlCommand(query, connection))
                {
                    using (SqlDataReader reader = command.ExecuteReader())
                    {
                        while (reader.Read())
                        {
                            ClientRepository clientRepository = new ClientRepository(sqlUtils);
                            Client client = clientRepository.FindOne((Guid)reader["clientId"]);
                            Guid Id = reader.GetGuid(reader.GetOrdinal("id"));
                            String[] TouristsName = reader["touristsName"].ToString().Split(',');
                            FlightRepository flightRepository = new FlightRepository(sqlUtils);
                            Flight flight = flightRepository.FindOne(Guid.Parse(reader["flightId"].ToString()));
                            int seats = (int)reader["seats"];
                            // Extrageți lista de id-uri ale turiștilor din tabela de asociere Ticket_Tourist
                            List<Guid> touristIds = new List<Guid>();
                            string queryTourists = "SELECT tourist_id FROM Ticket_Tourist WHERE ticket_id = @Id";
                            using (SqlCommand commandTourists = new SqlCommand(queryTourists, connection))
                            {
                                commandTourists.Parameters.AddWithValue("@Id", Id);
                                using (SqlDataReader readerTourists = commandTourists.ExecuteReader())
                                {
                                    while (readerTourists.Read())
                                    {
                                        touristIds.Add((Guid)readerTourists["tourist_id"]);
                                    }
                                }
                            }

                            List<Tourist> tourists = new List<Tourist>();
                            foreach (var touristId in touristIds)
                            {
                                string queryTouristName = "SELECT name FROM Tourist WHERE id = @TouristId";
                                using (SqlCommand commandTouristName = new SqlCommand(queryTouristName, connection))
                                {
                                    commandTouristName.Parameters.AddWithValue("@TouristId", touristId);
                                    using (SqlDataReader readerTouristName = commandTouristName.ExecuteReader())
                                    {
                                        if (readerTouristName.Read())
                                        {
                                            Tourist tourist = new Tourist((string)readerTouristName["name"]);
                                            tourist.Id = touristId;
                                            tourists.Add(tourist);
                                        }
                                    }
                                }
                            }

                            Ticket ticket = new Ticket(client, tourists, flight, seats);
                            ticket.Id = Id;
                            tickets.Add(ticket);
                        }
                    }
                }
            }
            return tickets;
        }

        public Ticket Save(Ticket entity)
        {
            string query = "INSERT INTO Ticket (id, clientId, flightId, seats) VALUES (@Id, @ClientId, @FlightId, @Seats)";

            using (SqlConnection connection = sqlUtils.GetConnection())
            {
                //log.Info("saving one Ticket");

                connection.Open();
                using (SqlCommand command = new SqlCommand(query, connection))
                {
                    Guid ticketId = Guid.NewGuid(); // Generați un id unic pentru bilet
                    entity.Id = ticketId;
                    command.Parameters.AddWithValue("@Id", ticketId);
                    command.Parameters.AddWithValue("@ClientId", entity.client.Id);
                    command.Parameters.AddWithValue("@FlightId", entity.flight.Id);
                    command.Parameters.AddWithValue("@Seats", entity.seats);
                    command.ExecuteNonQuery();
                }

                // Salvare asociere turisti-bilet
                foreach (Tourist tourist in entity.tourists)
                {
                    string insertQuery = "INSERT INTO Ticket_Tourist (ticket_id, tourist_id) VALUES (@TicketId, @TouristId)";
                    using (SqlCommand insertCommand = new SqlCommand(insertQuery, connection))
                    {
                        insertCommand.Parameters.AddWithValue("@TicketId", entity.Id);
                        insertCommand.Parameters.AddWithValue("@TouristId", tourist.Id);
                        insertCommand.ExecuteNonQuery();
                    }
                }

                // Actualizare disponibilitate locuri în zbor
                FlightRepository flightRepository = new FlightRepository(sqlUtils);
                Flight flight = flightRepository.FindOne(entity.flight.Id);
                if (flight != null)
                {
                    int newAvailableSeats = flight.AvailableSeats - entity.seats;
                    flightRepository.updateAvailableSeats(flight.Id, newAvailableSeats);
                }
            }

            return entity;
        }


        public Ticket Delete(Guid id)
        {
            Ticket ticket = FindOne(id);
            if (ticket != null)
            {
                string query = "DELETE FROM Ticket WHERE id = @Id";
                using (SqlConnection connection = sqlUtils.GetConnection())
                {
                    connection.Open();
                    using (SqlCommand command = new SqlCommand(query, connection))
                    {
                        command.Parameters.AddWithValue("@Id", id);
                        command.ExecuteNonQuery();
                    }
                }
            }
            return ticket;
        }

        public Ticket Update(Ticket entity)
        {

            return entity;
        }
    }
}
