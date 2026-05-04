package ar.com.petmanager.service;

import ar.com.petmanager.data.DataAccess;
import ar.com.petmanager.domain.Owner;
import ar.com.petmanager.domain.Pet;

import java.util.ArrayList;
import java.util.List;

public class PetServiceImpl implements PetService {

    private final DataAccess dataAccess;
    private final List<Pet> cache;    // fallback in-memory cuando no hay DB

    public PetServiceImpl(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
        this.cache = new ArrayList<>();
    }

    @Override
    public void add(Pet pet) {
        if (dataAccess != null) {
            dataAccess.savePet(pet);
        } else {
            cache.add(pet);
        }
    }

    @Override
    public void deleteById(int id) {
        if (dataAccess != null) {
            dataAccess.deletePet(id);
        } else {
            Pet pet = getById(id);
            if (pet != null) cache.remove(pet);
        }
    }

    @Override
    public Pet getById(int id) {
        if (dataAccess != null) {
            return dataAccess.getPet(id);
        }
        return cache.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
    }

    @Override
    public List<Pet> getAll() {
        if (dataAccess != null) {
            return dataAccess.getAllPets();
        }
        return new ArrayList<>(cache);
    }

    @Override
    public void update(Pet pet) {
        if (dataAccess != null) {
            dataAccess.updatePet(pet);
        } else {
            cache.stream()
                    .filter(p -> p.getId() == pet.getId())
                    .findFirst()
                    .ifPresent(found -> {
                        found.setName(pet.getName());
                        found.setAge(pet.getAge());
                        found.setRace(pet.getRace());
                        found.setWeight(pet.getWeight());
                        found.setSick(pet.isSick());
                        found.setDescription(pet.getDescription());
                        found.setStatus(pet.getStatus());
                    });
        }
    }

    @Override
    public void addOwner(Owner owner, Pet pet) {
        if (owner == null || owner.getDni() <= 0) return;
        if (getById((int) pet.getId()) == null) return;

        if (dataAccess != null) {
            dataAccess.addPetToOwner(owner.getDni(), pet.getId());
        }
        owner.adoptPet(pet);
    }

    @Override
    public void removeAllOwnersFromPet(long petId) {
        if (dataAccess != null) {
            dataAccess.removeAllOwnersFromPet(petId);
        }
        // Limpiar dueños del objeto en memoria si está cacheado
        Pet pet = getById((int) petId);
        if (pet != null) {
            pet.setOwners(new ArrayList<>());
        }
    }

    @Override
    public List<Pet> listAvailablePets() {
        return getAll();
    }
}
