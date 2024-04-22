using System;
using System.Collections.Generic;
using chat.services;
using FlightHubC_.Domain;

namespace chat.client{


    public class ChatClientCtrl: IChatObserver
    {
        public event EventHandler<EventArgs> updateEvent; //ctrl calls it when it has received an update
        public IChatServices server;
        public Agent currentUser;
        public ChatClientCtrl(IChatServices server)
        {
            this.server = server;
            currentUser = null;
        }

        public void login(String userId, String pass)
        {   
            currentUser=server.login(userId,pass,this);
            Console.WriteLine("Login succeeded ....");
            Console.WriteLine("Current user {0}", currentUser);
        }
        

        

        public void logout()
        {
            Console.WriteLine("Ctrl logout");
            //server.logout(currentUser, this);
            currentUser = null;
        }

        protected virtual void OnUserEvent(ChatUserEventArgs e)
        {
            if (updateEvent == null) return;
            updateEvent(this, e);
            Console.WriteLine("Update Event called");
        }
       

        public List<Flight> getFlights()
        {
            return server.getFlights();
        }

        public List<Flight> SearchFlights(string destination, DateTime dateTime)
        {
            return server.SearchFlight(destination, dateTime);
        }
        public bool BuyTicket(string clientsname, string clientsaddress, string touristsname, int numberOfSeats, Flight flight)
        {
            bool ticketBought= server.BuyTicket(clientsname, clientsaddress, touristsname, numberOfSeats, flight);

            return ticketBought;
        }

        public Flight findByDetails(string destination, DateTime dateTime, int availableSeats, string airport)
        {
            return server.findByDetails(destination, dateTime, availableSeats, airport);
        }

      
       

        public void notifyAgents()
        {
            Console.WriteLine("Agent " + currentUser.Username + " notify in ChatClientController");
            //updateEvent.Invoke(this, EventArgs.Empty);
            ChatUserEventArgs userArgs = new ChatUserEventArgs(ChatUserEvent.BoughtTicket);
            Console.WriteLine("Message received");
            OnUserEvent(userArgs);
        }

    }
}
