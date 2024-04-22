Create Database FlightHub
go
use FlightHub
go
CREATE TABLE Agent (
    id UNIQUEIDENTIFIER PRIMARY KEY,
    username NVARCHAR(255) NOT NULL
	password NVARCHAR(255)
);

CREATE TABLE Client (
    id UNIQUEIDENTIFIER PRIMARY KEY,
    name NVARCHAR(255) NOT NULL,
    adress NVARCHAR(255) NOT NULL
);

CREATE TABLE Flight (
    id UNIQUEIDENTIFIER PRIMARY KEY,
    destination NVARCHAR(255) NOT NULL,
    dateTime DATETIME NOT NULL,
    availableSeats INT NOT NULL
	airport NVARCHAR(255);
 
);

CREATE TABLE Ticket (
    id UNIQUEIDENTIFIER PRIMARY KEY,
    clientId UNIQUEIDENTIFIER NOT NULL,
    flightId UNIQUEIDENTIFIER NOT NULL,
    seats INT NOT NULL,
    FOREIGN KEY (clientId) REFERENCES Client(id),
    FOREIGN KEY (flightId) REFERENCES Flight(id)
);

CREATE TABLE Tourist (
    id UNIQUEIDENTIFIER PRIMARY KEY,
    name NVARCHAR(255) NOT NULL,
);

CREATE TABLE Ticket_Tourist (
    ticket_id UNIQUEIDENTIFIER NOT NULL,
    tourist_id UNIQUEIDENTIFIER NOT NULL,
    FOREIGN KEY (ticket_id) REFERENCES Ticket(id),
    FOREIGN KEY (tourist_id) REFERENCES Tourist(id)
);
DELETE FROM Ticket_Tourist
DELETE FROM Ticket
DELETE FROM Flight
DELETE FROM Client
DELETE FROM Agent
DELETE FROM Tourist

SELECT * FROM Client
SELECT * FROM Ticket_Tourist
Select * from Ticket
SELECT * FROM Tourist
SELECT * FROM Flight
SELECT * FROM Agent	

drop table ticket;
drop table ticket_tourist;

