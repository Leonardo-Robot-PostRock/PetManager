package ar.com.petmanager.service;

import ar.com.petmanager.domain.Owner;
import ar.com.petmanager.domain.Pet;
import ar.com.petmanager.domain.Vet;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OwnerServiceImpl implements OwnerService {

    private List<Owner> owners;

    public OwnerServiceImpl() {
        owners = new ArrayList<Owner>();
    }

    @Override
    public void addPet(Owner owner, Pet pet) {
        owner.adoptPet(pet);
    }

    @Override
    public void addPreferredVet(Owner owner, Vet vet) {
        owner.setPreferredVet(vet);
    }

    @Override
    public void add(Owner owner) {
        owners.add(owner);
    }

    @Override
    public void delete(Owner owner) {
        owners.remove(owner);
    }

    @Override
    public Owner getById(int dni) {
        return owners.stream()
                .filter(existingOwner -> existingOwner.getDni() == dni)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Owner> getAll() {
        return new ArrayList<>(owners);
    }

    @Override
    public void update(Owner owner) {
        Optional<Owner> optionalOwner = owners.stream().filter(existingOwner -> existingOwner.getDni() == owner.getDni()).findFirst();

        if (optionalOwner.isPresent()) {
            Owner foundOwner = optionalOwner.get();

            foundOwner.setDni(owner.getDni());
            foundOwner.setName(owner.getName());
            foundOwner.setSurname(owner.getSurname());
            foundOwner.setPhone(owner.getPhone());
            foundOwner.setAddress(owner.getAddress());
            foundOwner.setPreferredVet(owner.getPreferredVet());
        }
    }
}
