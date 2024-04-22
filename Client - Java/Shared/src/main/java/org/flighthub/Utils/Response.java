package org.flighthub.Utils;

import java.io.Serial;
import java.io.Serializable;

public class Response implements Serializable {
    //@Serial
    //private static final long serialVersionUID = 1L;
    private ResponseType type;
    private String message;
    private Object data;

    public Response(ResponseType type, String message, Object data) {
        this.type = type;
        this.message = message;
        this.data = data;
    }

    public ResponseType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }
}