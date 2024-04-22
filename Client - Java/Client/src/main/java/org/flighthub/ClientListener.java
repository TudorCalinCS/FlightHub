package org.flighthub;

import java.io.IOException;

public interface ClientListener {
    void onClientUpdate() throws IOException, ClassNotFoundException, InterruptedException;
}