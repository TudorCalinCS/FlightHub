package org.flighthub.Domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class Flight extends Entity<UUID> {
    private String destination;
    private LocalDateTime dateTime;
    private Integer availableSeats;
    private String airport;

    public Flight(String destination, LocalDateTime dateTime, Integer availableSeats, String airport) {
        this.destination = destination;
        this.dateTime = dateTime;
        this.availableSeats = availableSeats;
        this.airport = airport;
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

    public String getAirport() {
        return airport;
    }

    ;
}
