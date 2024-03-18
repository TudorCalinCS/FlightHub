package org.flighthub.Repository;

import org.flighthub.Domain.Entity;
import org.flighthub.Domain.Flight;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface IFlightRepository extends Repository<UUID, Flight> {
    Optional<Flight> findOne(UUID id);

    Iterable<Flight> findByDestinationAndDepartureDate(String destination, LocalDateTime departureDate);

    Iterable<Flight> findAll();

    Flight save(Flight entity);

    Optional<Flight> delete(UUID id);

    Optional<Flight> update(Flight entity);
}
