package ar.com.petmanager.service;

import java.util.List;

import ar.com.petmanager.domain.*;

public interface PetService extends CRUD<Pet> {
    void addOwner(Owner owner, Pet pet);

    List<Pet> listAvailablePets();
}
