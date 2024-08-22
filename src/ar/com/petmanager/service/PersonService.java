package ar.com.petmanager.service;

public interface PersonService<T> {
    public void addPerson(T person);

    public void deletePerson(T person);

    public void getPerson(T person);

    public void updatePerson(T person);
}
