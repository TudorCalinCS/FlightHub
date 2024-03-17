package org.flighthub.Domain;

import java.util.UUID;

public class Client extends Entity<UUID>{
    private String name;
    private String address;

    public Client(String name,String address) {
        this.name = name;
        this.address=address;
        //if(this.getId()==null) this.setId(UUID.randomUUID());
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }
}
