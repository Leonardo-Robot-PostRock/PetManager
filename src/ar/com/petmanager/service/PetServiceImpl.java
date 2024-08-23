package ar.com.petmanager.service;

import java.util.ArrayList;
import java.util.List;

import ar.com.petmanager.domain.*;

public class PetServiceImpl implements PetService {
    private List<Pet> availablePets;

    public PetServiceImpl() {
        this.availablePets = new ArrayList<>();
    }

    @Override
    public void addOwner(Owner owner, Pet pet) {
        
        if (owner == null || owner.getDni() <= 0) return;

        if(!availablePets.contains(pet)) return;

        owner.adoptPet(pet);
        availablePets.remove(pet);
    }

    @Override
    public List<Pet> listAvailablePets() {
        return new ArrayList<>(availablePets);
    }

    @Override
    public void add(Pet entity) {

    }

    @Override
    public void delete(Pet entity) {

    }

    @Override
    public Pet getById(int id) {
        return null;
    }

    @Override
    public List<Pet> getAll() {
        return null;
    }

    @Override
    public void update(Pet entity) {

    }
}
