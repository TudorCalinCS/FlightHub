using FlightHubC_.Domain;
using FlightHubC_.Utils;
//using log4net;
using System;
using System.Collections.Generic;
using System.Data;
using System.Data.SqlClient;
using System.Security.Cryptography;
namespace FlightHubC_.Repository
{
    public class AgentRepository : IAgentRepository
    {
        private readonly SqlUtils sqlUtils;
        //private static readonly log4net.ILog log = LogManager.GetLogger(typeof(Program));


        public AgentRepository(SqlUtils sqlUtils)
        {
            this.sqlUtils = sqlUtils;
        }

        public Agent FindOne(Guid id)
        {
          //  log.Info("finding one Agent");
            using (SqlConnection connection = sqlUtils.GetConnection())
            {
                connection.Open();
                string query = "SELECT * FROM Agent WHERE id = @id";
                using (SqlCommand command = new SqlCommand(query, connection))
                {
                    command.Parameters.AddWithValue("@id", id);
                    using (SqlDataReader reader = command.ExecuteReader())
                    {
                        if (reader.Read())
                        {
                            string username = reader.GetString(reader.GetOrdinal("username"));
                            Agent a = new Agent(username);
                            a.Id = id;
                            return a;
                        }
                    }
                }
            }
            return null;
        }
        public Agent FindByUsername(string username)
        {
            //log.Info("finding Agent by username");

            using (SqlConnection connection = sqlUtils.GetConnection())
            {
                connection.Open();
                string query = "SELECT * FROM Agent WHERE username = @Username";
                using (SqlCommand command = new SqlCommand(query, connection))
                {
                    command.Parameters.AddWithValue("@Username", username);
                    using (SqlDataReader reader = command.ExecuteReader())
                    {
                        if (reader.Read())
                        {
                            Guid id = reader.GetGuid(reader.GetOrdinal("id"));
                            Agent agent = new Agent(username);
                            agent.Id = id;
                            return agent;
                        }
                    }
                }
            }
            return null;
        }

        public IEnumerable<Agent> FindAll()
        {
            //log.Info("finding all Agents");

            List<Agent> agents = new List<Agent>();
            using (SqlConnection connection = sqlUtils.GetConnection())
            {
                connection.Open();
                string query = "SELECT * FROM Agent";
                using (SqlCommand command = new SqlCommand(query, connection))
                {
                    using (SqlDataReader reader = command.ExecuteReader())
                    {
                        while (reader.Read())
                        {
                            string username = reader.GetString(reader.GetOrdinal("username"));
                            Guid id = reader.GetGuid(reader.GetOrdinal("id"));
                            Agent a = new Agent(username);
                            a.Id = id;
                            agents.Add(a);
                        }
                    }
                }
            }
            return agents;
        }

        public Agent Save(Agent entity)
        {
            using (SqlConnection connection = sqlUtils.GetConnection())
            {
              //  log.Info("saving one Agent");

                connection.Open();
                string query = "INSERT INTO Agent (id, username) VALUES (@Id, @Username)";
                using (SqlCommand command = new SqlCommand(query, connection))
                {
                    Guid guid = Guid.NewGuid();
                    entity.Id = guid;
                    command.Parameters.AddWithValue("@Id", entity.Id);
                    command.Parameters.AddWithValue("@Username", entity.Username);
                    command.ExecuteNonQuery();
                }
            }
            return entity;
        }


        public Agent Save(Agent entity, string password)
        {
            using (SqlConnection connection = sqlUtils.GetConnection())
            {
                //log.Info("saving one Agent");

                connection.Open();
                string query = "INSERT INTO Agent (id, username, password) VALUES (@Id, @Username, @Password)";
                using (SqlCommand command = new SqlCommand(query, connection))
                {
                    Guid guid = Guid.NewGuid();
                    entity.Id = guid;
                    command.Parameters.AddWithValue("@Id", entity.Id);
                    command.Parameters.AddWithValue("@Username", entity.Username);

                    // Hash the password before saving it
                    using (SHA256 sha256Hash = SHA256.Create())
                    {
                        byte[] bytes = sha256Hash.ComputeHash(System.Text.Encoding.UTF8.GetBytes(password));
                        command.Parameters.AddWithValue("@Password", Convert.ToBase64String(bytes));
                    }

                    command.ExecuteNonQuery();
                }
            }
            return entity;
        }

        public Agent Delete(Guid id)
        {
            Agent agent = FindOne(id);
            if (agent != null)
            {
                using (SqlConnection connection = sqlUtils.GetConnection())
                {
                    connection.Open();
                    string query = "DELETE FROM Agent WHERE id = @Id";
                    using (SqlCommand command = new SqlCommand(query, connection))
                    {
                        command.Parameters.AddWithValue("@Id", id);
                        command.ExecuteNonQuery();
                    }
                }
            }
            return agent;
        }

        public Agent Update(Agent entity)
        {
            using (SqlConnection connection = sqlUtils.GetConnection())
            {
                connection.Open();
                string query = "UPDATE Agent SET username = @Username WHERE id = @Id";
                using (SqlCommand command = new SqlCommand(query, connection))
                {
                    command.Parameters.AddWithValue("@Username", entity.Username);
                    command.Parameters.AddWithValue("@Id", entity.Id);
                    command.ExecuteNonQuery();
                }
            }
            return entity;
        }
        public Agent Login(string username, string password)
        {
            //log.Info("login");
            using (SqlConnection connection = sqlUtils.GetConnection())
            {
                connection.Open();
                string query = "SELECT * FROM Agent WHERE username = @username";
                using (SqlCommand command = new SqlCommand(query, connection))
                {
                    command.Parameters.AddWithValue("@username", username);
                    using (SqlDataReader reader = command.ExecuteReader())
                    {
                        if (reader.Read())
                        {
                            string storedPasswordHash = reader.GetString(reader.GetOrdinal("password"));
                            if (VerifyPasswordHash(password, storedPasswordHash))
                            {
                                Guid id = reader.GetGuid(reader.GetOrdinal("id"));
                                Agent agent = new Agent(username);
                                agent.Id = id;
                                return agent;
                            }
                        }
                    }
                }
            }
            return null;
        }

        private bool VerifyPasswordHash(string password, string storedPasswordHash)
        {
            using (SHA256 sha256Hash = SHA256.Create())
            {
                byte[] bytes = sha256Hash.ComputeHash(System.Text.Encoding.UTF8.GetBytes(password));
                string hashedPassword = Convert.ToBase64String(bytes);
                return hashedPassword == storedPasswordHash;
            }
        }
    }
}
