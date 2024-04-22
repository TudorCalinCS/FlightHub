using FlightHubC_.Domain;
using FlightHubC_.Utils;
//using log4net;
using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Security.Cryptography;

namespace FlightHubC_.Repository
{
    public class ClientRepository : IClientRepository
    {
        private readonly SqlUtils sqlUtils;
        //private static readonly log4net.ILog log = LogManager.GetLogger(typeof(Program));

        public ClientRepository(SqlUtils sqlUtils)
        {
            this.sqlUtils = sqlUtils;
        }

        public Client FindOne(Guid id)
        {
            using (SqlConnection connection = sqlUtils.GetConnection())
            {
                //log.Info("finding one Client");

                string query = "SELECT * FROM Client WHERE id = @Id";
                SqlCommand command = new SqlCommand(query, connection);
                command.Parameters.AddWithValue("@Id", id);

                connection.Open();
                SqlDataReader reader = command.ExecuteReader();

                if (reader.Read())
                {
                    string name = reader["name"].ToString();
                    string address = reader["adress"].ToString();
                    Client c = new Client(name, address);
                    c.Id = id;
                    return c;
                }

                return null;
            }
        }

        public IEnumerable<Client> FindAll()
        {
            List<Client> clients = new List<Client>();

            using (SqlConnection connection = sqlUtils.GetConnection())
            {
                //log.Info("finding all Clients");

                string query = "SELECT * FROM Client";
                SqlCommand command = new SqlCommand(query, connection);

                connection.Open();
                SqlDataReader reader = command.ExecuteReader();

                while (reader.Read())
                {
                    Guid id = reader.GetGuid(reader.GetOrdinal("id"));
                    string name = reader["name"].ToString();
                    string address = reader["adress"].ToString();
                    Client c = new Client(name, address);
                    c.Id = id;
                    clients.Add(c);
                }
            }

            return clients;
        }

        public Client Save(Client entity)
        {
            using (SqlConnection connection = sqlUtils.GetConnection())
            {
                //log.Info("saving one Clients");

                string query = "INSERT INTO Client (id, name, adress) VALUES (@Id, @Name, @Address)";
                SqlCommand command = new SqlCommand(query, connection);
                Guid guid = Guid.NewGuid();
                entity.Id = guid;
                command.Parameters.AddWithValue("@Id", entity.Id);
                command.Parameters.AddWithValue("@Name", entity.Name);
                command.Parameters.AddWithValue("@Address", entity.Address);

                connection.Open();
                command.ExecuteNonQuery();
            }

            return entity;
        }

        public Client Delete(Guid id)
        {
            Client clientToDelete = FindOne(id);

            if (clientToDelete != null)
            {
                using (SqlConnection connection = sqlUtils.GetConnection())
                {
                    //log.Info("deleting one Client");
                    string query = "DELETE FROM Client WHERE id = @Id";
                    SqlCommand command = new SqlCommand(query, connection);
                    command.Parameters.AddWithValue("@Id", id);

                    connection.Open();
                    command.ExecuteNonQuery();
                }
            }

            return clientToDelete;
        }

        public Client Update(Client entity)
        {
            using (SqlConnection connection = sqlUtils.GetConnection())
            {
                string query = "UPDATE Client SET name = @Name, adress = @Address WHERE id = @Id";
                SqlCommand command = new SqlCommand(query, connection);
                command.Parameters.AddWithValue("@Id", entity.Id);
                command.Parameters.AddWithValue("@Name", entity.Name);
                command.Parameters.AddWithValue("@Address", entity.Address);

                connection.Open();
                command.ExecuteNonQuery();
            }

            return entity;
        }
    }
}
