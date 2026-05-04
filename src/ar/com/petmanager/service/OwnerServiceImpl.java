package ar.com.petmanager.service;

import ar.com.petmanager.data.DataAccess;
import ar.com.petmanager.domain.Owner;
import ar.com.petmanager.domain.Pet;
import ar.com.petmanager.domain.Vet;

import java.util.ArrayList;
import java.util.List;

public class OwnerServiceImpl implements OwnerService {

    private final DataAccess dataAccess;
    private final List<Owner> cache;    // fallback in-memory cuando no hay DB

    public OwnerServiceImpl(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
        this.cache = new ArrayList<>();
    }

    @Override
    public void add(Owner owner) {
        if (dataAccess != null) {
            dataAccess.saveOwner(owner);
        } else {
            boolean exists = cache.stream().anyMatch(o -> o.getDni() == owner.getDni());
            if (exists) throw new RuntimeException("El DNI ya está registrado");
            cache.add(owner);
        }
    }

    @Override
    public void deleteById(int dni) {
        if (dataAccess != null) {
            dataAccess.deleteOwner(dni);
        } else {
            Owner owner = getById(dni);
            if (owner != null) cache.remove(owner);
        }
    }

    @Override
    public Owner getById(int dni) {
        if (dataAccess != null) {
            return dataAccess.getOwner(dni);
        }
        return cache.stream().filter(o -> o.getDni() == dni).findFirst().orElse(null);
    }

    @Override
    public List<Owner> getAll() {
        if (dataAccess != null) {
            return dataAccess.getAllOwners();
        }
        return new ArrayList<>(cache);
    }

    @Override
    public void update(Owner owner) {
        if (dataAccess != null) {
            dataAccess.updateOwner(owner);
        } else {
            cache.stream()
                    .filter(o -> o.getDni() == owner.getDni())
                    .findFirst()
                    .ifPresent(found -> {
                        found.setName(owner.getName());
                        found.setSurname(owner.getSurname());
                        found.setPhone(owner.getPhone());
                        found.setAddress(owner.getAddress());
                        found.setPreferredVet(owner.getPreferredVet());
                    });
        }
    }

    @Override
    public void addPet(Owner owner, Pet pet) {
        owner.adoptPet(pet);
    }

    @Override
    public void addPreferredVet(Owner owner, Vet vet) {
        owner.setPreferredVet(vet);
    }
}
