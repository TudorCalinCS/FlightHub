using System;
using System.Threading;
using System.Net;
using System.Net.Sockets;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Formatters.Binary;
using chat.services;
using FlightHubC_.Domain;
using System.Collections.Generic;
using System.IO;
using System.Text;
using System.Globalization;

namespace chat.network.client
{
	public class ClientWorker :  IObserver 
	{
		private IServices server;
		private TcpClient connection;

        private NetworkStream stream;
		private IFormatter formatter;
		private volatile bool connected;

        public ClientWorker(IServices server, TcpClient connection)
		{
			this.server = server;
			this.connection = connection;
			try
			{
				
				stream=connection.GetStream();
                formatter = new BinaryFormatter();
				connected=true;
			}
			catch (Exception e)
			{
                Console.WriteLine(e.StackTrace);
			}
		}
		
		public virtual void run()
		{
			while(connected)
			{
				try
				{
                    StreamReader reader = new StreamReader(stream);
                    string request = reader.ReadLine();
                    Console.WriteLine("Request: " + request);

                    object response =handleRequest((string)request);
					if (response!=null)
					{
					   sendResponse((string) response);
					}
				}
				catch (Exception e)
				{
                    Console.WriteLine(e.StackTrace);
				}
				
				try
				{
					Thread.Sleep(1000);
				}
				catch (Exception e)
				{
                    Console.WriteLine(e.StackTrace);
				}
			}
			try
			{
				stream.Close();
				connection.Close();
			}
			catch (Exception e)
			{
				Console.WriteLine("Error "+e);
			}
		}

		private string handleRequest(string request)
		{
			string response =null;
            string[] requestParts = request.Split(':');

  
            string requestType = requestParts[0];

			if (requestType == "LOGIN")
			{
				Console.WriteLine("Login request ...");
				Agent foundAgent = null;
				try
				{
					lock (server)
					{
						foundAgent = server.login(requestParts[1], requestParts[2], this);
					}
					response = "OK:" + foundAgent.Id.ToString();
				}
				catch (Exception e)
				{
					connected = false;
					response = "ERROR:"+e.ToString();

					return response;
				}

			}
            if (requestType=="GET_FLIGHTS")
            {
                Console.WriteLine("GetFLightsRequest...");
                try
                {
                    List<Flight> flights;
                    lock (server)
                    {
                        flights = server.getFlights();
                    }
                    response += "OK:" + flights.Count + "\n";
                    foreach(Flight flight in flights)
                    {
                        response += flight.Destination + "#" + flight.DateTime.ToString() + "#" + flight.AvailableSeats + "#" + flight.Airport + "#"+flight.Id.ToString()+"\n";
                    }
                }
                catch (Exception e)
                {
                    response = "ERROR:" + e.ToString();

                    return response;
                }
            }
            if (requestType == "SEARCH_FLIGHTS")
            {
                Console.WriteLine("SearchFlightsRequest...");
                try
                {
                    List<Flight> flights;
                    lock (server)
                    {
                        Console.WriteLine(requestParts[2]);
                        String format = "yyyy-MM-dd";
                        DateTime dateTime = DateTime.ParseExact(requestParts[2], format, CultureInfo.InvariantCulture);

                        flights = server.SearchFlight(requestParts[1], dateTime);
                    }
                    response += "OK:" + flights.Count + "\n";
                    foreach (Flight flight in flights)
                    {
                        response += flight.Destination + "#" + flight.DateTime.ToString() + "#" + flight.AvailableSeats + "#" + flight.Airport + "#" + flight.Id.ToString() + "\n";
                    }
                }
                catch (Exception e)
                {
                    response = "ERROR:" + e.ToString();

                    return response;
                }
            }
            if (requestType== "BUY_TICKET")
            {
                Console.WriteLine("BuyTicketRequest...");
                try
                {
                    bool answer;
                    lock (server)
                    {
                        Flight flight = server.findFlightId(requestParts[5]);
                        answer = server.BuyTicket(requestParts[1], requestParts[2], requestParts[3], int.Parse( requestParts[4]), flight);
                    }

                    if(answer ) { response = "OK";
                    }
                }
                catch (Exception e)
                {
                    response = "ERROR:" + e.ToString();

                    return response;
                }
            }
            if (response == null)
                return "NULL";
            return response;
		}

		private void sendResponse(string response)
		{
			Console.WriteLine("sending response "+response);
			lock (stream)
			{

                StreamWriter writer = new StreamWriter(stream, Encoding.UTF8, 1024, false);
                writer.WriteLine(response);
                writer.Flush();
                
            }
            Console.WriteLine("Response sent");

		}


		public void notifyAgents()
		{
			Console.WriteLine("Agent " + " notify in ChatClientWorker");
            sendResponse("NOTIFY:data \n");
		}
    }

}