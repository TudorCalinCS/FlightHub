package org.flighthub;

import org.flighthub.Domain.Agent;
import org.flighthub.Domain.Flight;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Client {


    private Socket socket;
    private BufferedWriter outputStream;
    private BufferedReader inputStream;
    private List<ClientListener> clientListeners;
    private static final int CONNECTION_TIMEOUT = 5000; // 5 seconds

    private BlockingQueue<String> responseQueue;

    public Client(String serverAddress, int serverPort) {
        clientListeners = new ArrayList<>();
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(serverAddress, serverPort), CONNECTION_TIMEOUT);
            System.out.println("Client connected");
            socket.setSoTimeout(CONNECTION_TIMEOUT);

            outputStream = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            outputStream.flush();
            inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            responseQueue = new LinkedBlockingQueue<>();
            Thread receiveThread = new Thread(this::receiveResponsesFromServer);
            receiveThread.start();
        } catch (SocketTimeoutException timeoutException) {
            System.err.println("Connection timed out while connecting to the server.");
        } catch (IOException exception) {
            System.err.println("Error connecting to server: " + exception.getMessage());
        }

    }


    public Agent login(String name, String password) throws IOException, InterruptedException {

        String requestData = "LOGIN:" + name + ":" + password;
        outputStream.write(requestData);
        outputStream.newLine();
        outputStream.flush();

        String response = getNextResponse();
        String[] parts = response.split(":");
        Agent agent = null;

        if (parts[0].contains("OK")) {
            agent = new Agent(name);
            agent.setId(UUID.fromString(parts[1]));
        }
        return agent;
    }

    public List<Flight> getFlights() throws IOException, InterruptedException {

        String requestData = "GET_FLIGHTS:";
        outputStream.write(requestData);
        outputStream.newLine();
        outputStream.flush();

        List<Flight> list = new ArrayList<>();
        String line = getNextResponse();
        String[] lineParts = line.split(":");

        if (lineParts[0].contains("OK")) {
            int size = Integer.parseInt(lineParts[1]);
            for (int i = 0; i < size; i++) {
                line = getNextResponse();

                String[] lineparts = line.split("#");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy h:mm:ss a");
                LocalDateTime dateTime = LocalDateTime.parse(lineparts[1], formatter);

                Flight flight = new Flight(lineparts[0], dateTime, Integer.parseInt(lineparts[2]), lineparts[3]);
                flight.setId(UUID.fromString(lineparts[4]));
                list.add(flight);
            }
        }

        return list;
    }

    public List<Flight> searchFlights(String destination, LocalDate departureDate) throws IOException, ClassNotFoundException, InterruptedException {

        String requestData = "SEARCH_FLIGHTS:" + destination + ":" + departureDate;
        System.out.println(requestData);
        outputStream = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        outputStream.write(requestData);
        outputStream.newLine();
        outputStream.flush();

        List<Flight> list = new ArrayList<>();
        String line = getNextResponse();
        String[] lineParts = line.split(":");

        if (lineParts[0].contains("OK")) {
            int size = Integer.parseInt(lineParts[1]);
            for (int i = 0; i < size; i++) {
                line = getNextResponse();
                String[] lineparts = line.split("#");

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy h:mm:ss a");
                LocalDateTime dateTime = LocalDateTime.parse(lineparts[1], formatter);

                Flight flight = new Flight(lineparts[0], dateTime, Integer.parseInt(lineparts[2]), lineparts[3]);
                flight.setId(UUID.fromString(lineparts[4]));
                list.add(flight);
            }
        }
        return list;
    }

    public void buyTicket(String clientName, String clientAddress, String touristsName, int seats, Flight flight) throws IOException, ClassNotFoundException, InterruptedException {

        String requestData = "BUY_TICKET:" + clientName + ":" + clientAddress + ":" + touristsName + ":" + seats + ":" + flight.getId().toString();
        System.out.println(requestData);
        outputStream = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        outputStream.write(requestData);
        outputStream.newLine();
        outputStream.flush();

        inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String response = getNextResponse();

        if (response.contains("OK")) {
            System.out.println("Ticket Bought");
        } else {
            System.out.println("'Buy ticket' failed: " + response);
        }
    }

    public void updateTable() throws IOException, ClassNotFoundException, InterruptedException {
        try {
            System.out.println("Update table \n");
            for (ClientListener listener : clientListeners) {
                listener.onClientUpdate();
            }
        } catch (IOException e) {
            System.err.println("IOException occurred: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("ClassNotFoundException occurred: " + e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.err.println("InterruptedException occurred: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("An unexpected exception occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void addClientListener(ClientListener listener) {
        this.clientListeners.add(listener);
    }

    private void receiveResponsesFromServer() {
        try {
            while (true) {
                if (inputStream.ready()) {
                    String response = inputStream.readLine();
                    System.out.println("Response from server: " + response);
                    if (!response.isEmpty()) {
                        if (response.contains("NOTIFY")) {
                            updateTable();
                        } else {
                            responseQueue.put(response);
                        }
                    }

                }
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Error receiving response from server: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public String getNextResponse() throws InterruptedException {
        return responseQueue.take();
    }


}