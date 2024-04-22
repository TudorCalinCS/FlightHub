package org.flighthub.Domain;

import java.util.UUID;

public class Tourist extends Entity<UUID> {

    private String name;

    public Tourist(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }


}
