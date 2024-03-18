package org.flighthub.Repository;

import org.flighthub.Domain.Client;

import java.util.Optional;
import java.util.UUID;

public interface IClientRepository extends Repository<UUID, Client> {
    Optional<Client> findOne(UUID id);

    Iterable<Client> findAll();

    Client save(Client entity);

    Optional<Client> delete(UUID id);

    Optional<Client> update(Client entity);
}
