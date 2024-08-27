package ar.com.petmanager.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ar.com.petmanager.domain.*;

public class PetServiceImpl implements PetService {
    private List<Pet> availablePets;

    public PetServiceImpl() {
        this.availablePets = new ArrayList<Pet>();
    }

    @Override
    public void addOwner(Owner owner, Pet pet) {

        if (owner == null || owner.getDni() <= 0) return;

        if (!availablePets.contains(pet)) return;

        owner.adoptPet(pet);
        availablePets.remove(pet);
    }

    @Override
    public List<Pet> listAvailablePets() {
        return new ArrayList<>(availablePets);
    }

    @Override
    public void add(Pet pet) {
        availablePets.add(pet);
    }

    @Override
    public void delete(Pet pet) {
        availablePets.remove(pet);
    }

    @Override
    public Pet getById(int id) {
        return availablePets.stream().filter(pet -> pet.getId() == id).findFirst().orElse(null);
    }

    @Override
    public List<Pet> getAll() {
        return new ArrayList<>(availablePets);
    }

    @Override
    public void update(Pet pet) {
        Optional<Pet> optionalPet = availablePets.stream().filter(existingPet -> pet.getId() == existingPet.getId()).findFirst();

        if (optionalPet.isPresent()) {
            Pet foundPet = optionalPet.get();

            foundPet.setName(pet.getName());
            foundPet.setAge(pet.getAge());
            foundPet.setRace(pet.getRace());
            foundPet.setWeight(pet.getWeight());
            foundPet.setSick(pet.isSick());
            foundPet.setDescription(pet.getDescription());
        }
    }
}
