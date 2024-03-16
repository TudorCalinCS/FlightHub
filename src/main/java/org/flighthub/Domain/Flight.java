package org.flighthub.Domain;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public class Flight extends Entity<UUID>{
    private String destination;
    private LocalDateTime dateTime;
    private Integer availableSeats;

    public Flight(String destination, LocalDateTime dateTime, Integer availableSeats) {
        this.destination = destination;
        this.dateTime=dateTime;
        this.availableSeats = availableSeats;
    }

    public String getDestination() {
        return destination;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }
}
