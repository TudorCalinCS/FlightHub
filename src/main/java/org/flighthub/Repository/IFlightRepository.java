package org.flighthub.Repository;

import org.flighthub.Domain.Flight;

import java.util.Optional;
import java.util.UUID;

public interface IFlightRepository {
    Optional<Flight> findOne(UUID id);

    Iterable<Flight> findAll();

    Flight save(Flight entity);

    Optional<Flight> delete(UUID id);

    Optional<Flight> update(Flight entity);
}
