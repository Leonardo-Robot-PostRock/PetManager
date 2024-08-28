package ar.com.petmanager.domain;

import java.util.ArrayList;
import java.util.List;

public class Owner extends Person {
    private Vet preferredVet;
    private List<Pet> pets;

    public Owner(int dni, String name, String surname, int phone, String street, String city) {
        super(dni, name, surname, phone, street, city);
    }

    public Vet getPreferredVet() {
        return preferredVet;
    }

    public void setPreferredVet(Vet preferredVet) {
        this.preferredVet = preferredVet;
    }

    public void removePreferredVet() {
        this.preferredVet = null;
    }

    public void adoptPet(Pet pet) {
        if (pet != null) {
            pets.add(pet);
            pet.addOwner(this);
        }
    }

    public List<Pet> getPets() {
        return pets;
    }

    @Override
    public int getDni() {
        return super.getDni();
    }

    @Override
    public void setDni(int dni) {
        super.setDni(dni);
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public void setName(String name) {
        super.setName(name);
    }

    @Override
    public String getSurname() {
        return super.getSurname();
    }

    @Override
    public void setSurname(String surname) {
        super.setSurname(surname);
    }

    @Override
    public long getPhone() {
        return super.getPhone();
    }

    @Override
    public void setPhone(long phone) {
        super.setPhone(phone);
    }

    @Override
    public Address getAddress() {
        return super.getAddress();
    }

    @Override
    public void setAddress(Address address) {
        super.setAddress(address);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
