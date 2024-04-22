using FlightHubC_.Domain;
using FlightHubC_.Utils;
//using log4net;
using System;
using System.Collections.Generic;
using System.Data.SqlClient;

namespace FlightHubC_.Repository
{
    public class FlightRepository : IFlightRepository
    {
        private readonly SqlUtils sqlUtils;
        //private static readonly log4net.ILog log = LogManager.GetLogger(typeof(Program));

        public FlightRepository(SqlUtils sqlUtils)
        {
            this.sqlUtils = sqlUtils;
        }

        public Flight FindOne(Guid id)
        {
            //log.Info("finding one Flight");
            Flight flight = null;
            string query = "SELECT * FROM Flight WHERE id = @Id";

            using (SqlConnection connection = sqlUtils.GetConnection())
            {
                using (SqlCommand command = new SqlCommand(query, connection))
                {
                    command.Parameters.AddWithValue("@Id", id);
                    connection.Open();
                    SqlDataReader reader = command.ExecuteReader();
                    if (reader.Read())
                    {
                        flight = new Flight(
                            reader["destination"].ToString(),
                            Convert.ToDateTime(reader["dateTime"]),
                            Convert.ToInt32(reader["availableSeats"]),
                            reader["airport"].ToString()
                        );
                        flight.Id = id;
                    }
                }
            }

            return flight;
        }

        public IEnumerable<Flight> FindByDestinationAndDepartureDate(string destination, DateTime departureDate)
        {
            //log.Info("searching flight by destination and departure date");

            List<Flight> flights = new List<Flight>();
            string query = "SELECT * FROM Flight WHERE destination = @Destination AND CAST(dateTime AS DATE) = @DepartureDate AND availableSeats > 0";

            using (SqlConnection connection = sqlUtils.GetConnection())
            {
                using (SqlCommand command = new SqlCommand(query, connection))
                {
                    command.Parameters.AddWithValue("@Destination", destination);
                    command.Parameters.AddWithValue("@DepartureDate", departureDate.Date);
                    connection.Open();
                    SqlDataReader reader = command.ExecuteReader();
                    while (reader.Read())
                    {
                        Flight flight = new Flight(
                            reader["destination"].ToString(),
                            Convert.ToDateTime(reader["dateTime"]),
                            Convert.ToInt32(reader["availableSeats"]),
                            reader["airport"].ToString()
                        );
                        flight.Id = reader.GetGuid(reader.GetOrdinal("id"));
                        flights.Add(flight);
                    }
                }
            }

            return flights;
        }
        public Flight FindByDetails(string destination, DateTime departureDateTime, int availableSeats, string airport)
        {
            //log.Info("searching flight by details");
            Flight foundFlight = null;
            string query = "SELECT * FROM Flight WHERE destination = @Destination " +
                           "AND CAST(dateTime AS DATE) = @DepartureDateTime " +
                           "AND availableSeats = @AvailableSeats " +
                           "AND airport = @Airport";

            using (SqlConnection connection = sqlUtils.GetConnection())
            {
                using (SqlCommand command = new SqlCommand(query, connection))
                {
                    command.Parameters.AddWithValue("@Destination", destination);
                    command.Parameters.AddWithValue("@DepartureDateTime", departureDateTime.Date);
                    command.Parameters.AddWithValue("@AvailableSeats", availableSeats);
                    command.Parameters.AddWithValue("@Airport", airport);
                    connection.Open();
                    SqlDataReader reader = command.ExecuteReader();
                    if (reader.Read())
                    {
                        foundFlight = new Flight(
                            reader["destination"].ToString(),
                            Convert.ToDateTime(reader["dateTime"]),
                            Convert.ToInt32(reader["availableSeats"]),
                            reader["airport"].ToString()
                        );
                        foundFlight.Id = reader.GetGuid(reader.GetOrdinal("id"));
                    }
                }
            }

            return foundFlight;
        }


        public IEnumerable<Flight> FindAll()
        {

            List<Flight> flights = new List<Flight>();
            string query = "SELECT * FROM Flight WHERE availableSeats > 0";

            using (SqlConnection connection = sqlUtils.GetConnection())
            {
                using (SqlCommand command = new SqlCommand(query, connection))
                {
                    //log.Info("finding all Flight");
                    connection.Open();
                    SqlDataReader reader = command.ExecuteReader();
                    while (reader.Read())
                    {
                        Flight flight = new Flight(
                        reader["destination"].ToString(),
                        Convert.ToDateTime(reader["dateTime"]),
                        Convert.ToInt32(reader["availableSeats"]),
                        reader["airport"].ToString()
                    );
                        flight.Id = reader.GetGuid(reader.GetOrdinal("id"));
                        flights.Add(flight);
                    }
                }
            }

            return flights;
        }

        public Flight Save(Flight entity)
        {
            string query = "INSERT INTO Flight (id, destination, dateTime, availableSeats, airport) " +
                           "VALUES (@Id, @Destination, @DateTime, @AvailableSeats, @Airport)";

            using (SqlConnection connection = sqlUtils.GetConnection())
            {
                using (SqlCommand command = new SqlCommand(query, connection))
                {
                    //log.Info("saving one Flight");
                    Guid guid = Guid.NewGuid();
                    entity.Id = guid;
                    command.Parameters.AddWithValue("@Id", entity.Id);
                    command.Parameters.AddWithValue("@Destination", entity.Destination);
                    command.Parameters.AddWithValue("@DateTime", entity.DateTime);
                    command.Parameters.AddWithValue("@AvailableSeats", entity.AvailableSeats);
                    command.Parameters.AddWithValue("@Airport", entity.Airport);
                    connection.Open();
                    command.ExecuteNonQuery();
                }
            }

            return entity;
        }

        public Flight Delete(Guid id)
        {
            Flight deletedFlight = FindOne(id);
            if (deletedFlight != null)
            {
                string query = "DELETE FROM Flight WHERE id = @Id";

                using (SqlConnection connection = sqlUtils.GetConnection())
                {
                    using (SqlCommand command = new SqlCommand(query, connection))
                    {
                        command.Parameters.AddWithValue("@Id", id);
                        connection.Open();
                        command.ExecuteNonQuery();
                    }
                }
            }

            return deletedFlight;
        }

        public Flight Update(Flight entity)
        {


            return entity;
        }
        public void updateAvailableSeats(Guid flightId, int newAvailableSeats)
        {
            //log.Info("updating available seats for one Flight");
            String query = "UPDATE Flight SET availableSeats = @New WHERE id = @Id";
            using (SqlConnection connection = sqlUtils.GetConnection())
            {
                using (SqlCommand command = new SqlCommand(query, connection))
                {
                    command.Parameters.AddWithValue("@New", newAvailableSeats);
                    command.Parameters.AddWithValue("@Id", flightId);
                    connection.Open();
                    command.ExecuteNonQuery();
                }
            }

        }
    }
}
