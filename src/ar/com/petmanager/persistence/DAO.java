package ar.com.petmanager.persistence;

import java.util.List;
import java.util.Optional;

public interface DAO<T, ID> {
    void create(T entity);

    void update(T entity);

    void delete(ID id);

    Optional<T> findById(ID id);

    List<T> findAll();
}