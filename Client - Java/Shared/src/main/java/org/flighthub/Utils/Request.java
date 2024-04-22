package org.flighthub.Utils;

import org.flighthub.Domain.Flight;

import java.io.Serial;
import java.io.Serializable;

public class Request implements Serializable {
    //@Serial
    //private static final long serialVersionUID = 1L;
    private RequestType type;
    private String data;

    private Flight flight;

    public Request(RequestType type, String data) {
        this.type = type;
        this.data = data;
    }

    public Request(RequestType type, String data, Flight flight) {
        this.type = type;
        this.data = data;
        this.flight = flight;
    }

    public RequestType getType() {
        return type;
    }

    public String getData() {
        return data;
    }

    public Flight getFlight() {
        return flight;
    }
}