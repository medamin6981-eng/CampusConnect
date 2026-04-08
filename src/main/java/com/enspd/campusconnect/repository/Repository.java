package com.enspd.campusconnect.repository;

import java.util.List;
import java.util.Optional;

/**
 * Generic repository interface for CRUD operations.
 * @param <T> The entity type
 * @param <ID> The type of the entity's identifier
 */
public interface Repository<T, ID> {
    void save(T entity);
    void update(T entity);
    void delete(ID id);
    Optional<T> findById(ID id);
    List<T> findAll();
}
