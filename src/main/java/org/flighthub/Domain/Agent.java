package org.flighthub.Domain;

import java.util.UUID;

public class Agent extends Entity<UUID>{
    private String username;

    public Agent(String username) {
        this.username = username;
    }

}
