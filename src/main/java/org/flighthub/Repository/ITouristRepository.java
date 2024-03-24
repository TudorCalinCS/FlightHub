package org.flighthub.Repository;

import org.flighthub.Domain.Tourist;

import java.util.Optional;
import java.util.UUID;

public interface ITouristRepository extends Repository<UUID, Tourist> {

    Tourist save(Tourist entity);
}