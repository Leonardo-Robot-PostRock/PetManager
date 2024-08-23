package ar.com.petmanager.domain;

import java.util.ArrayList;
import java.util.List;

public class Owner extends Person {
    private Vet preferredVet;
    private List<Pet> pets;

    public Owner(int dni, String name, String surname, int phone, String city, String address) {
        super(dni, name, surname, phone, city, address);
        this.pets = new ArrayList<>();
    }

    public Vet getPreferredVet() {
        return preferredVet;
    }

    public void setPreferredVet(Vet preferredVet) {
        this.preferredVet = preferredVet;
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

}
