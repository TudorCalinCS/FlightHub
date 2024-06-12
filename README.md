# Flighthub

Flighthub is a cross-platform client-server application that facilitates travel agencies in booking tickets for clients through a centralized flight company. Developed to manage diverse functionalities like login, flight search, ticket purchase, and logout, Flighthub operates as a seamless integration of Java-based front end and C#-based back end.

## Features

1. **Login**: Employees can authenticate themselves using their credentials. Upon successful login, a new window displays flight information including destination, departure date and time, airport, and available seats.
2. **Flight Search**: Employees can search for flights by entering the destination and departure date after logging in. The application will display all flights matching the criteria along with their departure time and the number of available seats.
3. **Ticket Purchase**: Employees can purchase tickets for clients for a specific destination, date, and time. When purchasing a ticket, employees need to enter the client's name, tourists' names, the client's address, and the number of seats. Once a ticket is purchased, the flight information and the number of available seats are updated for all employees. If there are no more available seats for a flight, that flight will no longer appear in the list.
4. **Logout**: Employees can log out of the system.

## Project Structure

### Client (Java)
- **Model-View-Controller (MVC) Pattern**: Follows the MVC architectural pattern where:
  - **Model**: Represents the data and business logic.
  - **View**: Defines the user interface layout using FXML files. JavaFX is utilized for UI development.
  - **Controller**: Handles user interactions from the UI and updates the views.
- **Request Sending**: The `Client` class sends requests to the server for various operations such as login, flight retrieval, flight search, and ticket purchase. It also handles server responses and updates the application state accordingly.

### Server (C#)
- **Networking**: Utilizes the `TcpListener` class to manage client-server communication over TCP/IP. The server listens for incoming client connections and processes requests sent by the client. 
- **ORM**: Uses Entity Framework to handle database operations and ensure data persistence.
- **Request Handling**: The server processes requests sent by the client, performs necessary operations (such as database queries and updates), and sends back appropriate responses.

## Application Showcase
![Animation](https://github.com/TudorCalinCS/FlightHub/assets/128086342/a9cfc10e-cc65-4f42-8030-3c3a59eeeb9d)
