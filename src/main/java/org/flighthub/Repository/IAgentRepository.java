package org.flighthub.Repository;

import org.flighthub.Domain.Agent;

import java.util.Optional;
import java.util.UUID;

public interface IAgentRepository {
    Optional<Agent> findOne(UUID id);

    Iterable<Agent> findAll();

    Agent save(Agent entity);

    Optional<Agent> delete(UUID id);

    Optional<Agent> update(Agent entity);
}
