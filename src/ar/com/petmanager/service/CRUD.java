package ar.com.petmanager.service;

import java.util.List;

public interface CRUD<T> {
    void add(T entity);

    void delete(T entity);

    T getById(int id);

    List<T> getAll();

    void update(T entity);
}
