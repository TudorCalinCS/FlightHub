package org.flighthub.Domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Ticket extends Entity<UUID> {

    private Client client;
    //private String[] touristsName;

    private List<Tourist> touristList;
    private Flight flight;
    private Integer seats;

    public Ticket(Client client, List<Tourist> touristList, Flight flight,int seats) {
        this.client = client;
       // this.touristsName = touristsName;
        this.touristList=touristList;
        this.flight = flight;
        this.seats=seats;
    }

      public Client getClient() {
        return client;
    }

    public List<Tourist> getTouristList(){
        return this.touristList;
    }

    public Flight getFlight() {
        return flight;
    }

    public Integer getSeats() {
        return seats;
    }
}
