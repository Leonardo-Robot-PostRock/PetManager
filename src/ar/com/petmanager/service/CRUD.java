package ar.com.petmanager.service;

import java.util.List;

public interface CRUD<T> {
    void add(T entity);

    void deleteById(int id);

    T getById(int id);

    List<T> getAll();

    void update(T entity);
}
