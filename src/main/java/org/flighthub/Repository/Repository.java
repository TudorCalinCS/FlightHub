package org.flighthub.Repository;



import org.flighthub.Domain.Entity;

import java.util.Optional;

/**
 * CRUD operations repository interface
 * @param <ID> - type E must have an attribute of type ID
 * @param <E> - type of entities saved in repository
 */
public interface Repository<ID, E extends Entity<ID>> {

    Optional<E> findOne(ID id);

    Iterable<E> findAll();

    E save(E entity);

    Optional<E> delete(ID id);

    Optional<E> update(E entity);
}
