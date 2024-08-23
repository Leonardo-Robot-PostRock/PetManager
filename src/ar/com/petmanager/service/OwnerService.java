package ar.com.petmanager.service;

import ar.com.petmanager.domain.Owner;
import ar.com.petmanager.domain.Pet;
import ar.com.petmanager.domain.Vet;

public interface OwnerService extends PersonService<Owner>{
    void addPet(Owner owner, Pet pet);
    void addPreferredVet(Owner owner, Vet vet);
}
