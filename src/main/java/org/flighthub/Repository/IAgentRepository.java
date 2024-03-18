package org.flighthub.Repository;

import org.flighthub.Domain.Agent;

import java.util.Optional;
import java.util.UUID;

public interface IAgentRepository extends Repository<UUID, Agent> {
    Optional<Agent> findOne(UUID id);

    Iterable<Agent> findAll();

    Agent save(Agent entity);

    Agent save(Agent entity, String password);

    Optional<Agent> findOneByUsername(String username);

    Optional<Agent> delete(UUID id);

    Optional<Agent> update(Agent entity);

    Agent login(String username, String password);
}
