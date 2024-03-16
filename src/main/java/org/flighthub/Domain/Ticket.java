package org.flighthub.Domain;

import java.util.Optional;
import java.util.UUID;

public class Ticket extends Entity<UUID> {

    private Client client;
    private String[] touristsName;
    private Flight flight;
    private Integer seats;

    public Ticket(Client client, String[] touristsName, Flight flight,int seats) {
        this.client = client;
        this.touristsName = touristsName;
        this.flight = flight;
        this.seats=seats;
    }

      public Client getClient() {
        return client;
    }

    public String[] getTouristsName() {
        return touristsName;
    }

    public Flight getFlight() {
        return flight;
    }

    public Integer getSeats() {
        return seats;
    }
}
