package org.flighthub.Repository;

import org.flighthub.Domain.Ticket;

import java.util.Optional;
import java.util.UUID;

public interface ITicketRepository extends Repository<UUID, Ticket>{
    Optional<Ticket> findOne(UUID id);

    Iterable<Ticket> findAll();

    Ticket save(Ticket entity);

    Optional<Ticket> delete(UUID id);

    Optional<Ticket> update(Ticket entity);
}
