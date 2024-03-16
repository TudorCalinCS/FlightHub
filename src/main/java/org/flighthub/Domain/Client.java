package org.flighthub.Domain;

import java.util.UUID;

public class Client extends Entity<UUID>{
    private String name;
    private String adress;

    public Client(String name,String adress) {
        this.name = name;
        this.adress=adress;
    }

}
