package ar.com.petmanager.data;

import ar.com.petmanager.domain.*;

import java.util.List;

public interface DataAccess {

    void saveOwner(Owner owner);
    void updateOwner(Owner owner);
    void deleteOwner(int dni);
    Owner getOwner(int dni);
    List<Owner> getAllOwners();

    void saveDonor(Donor donor);
    void updateDonor(Donor donor);
    void deleteDonor(int dni);
    Donor getDonor(int dni);
    List<Donor> getAllDonors();

    void saveVet(Vet vet);
    void updateVet(Vet vet);
    void deleteVet(long idVet);
    Vet getVet(long idVet);
    List<Vet> getAllVets();

    void savePet(Pet pet);
    void updatePet(Pet pet);
    void deletePet(long id);
    Pet getPet(long id);
    List<Pet> getAllPets();

    void addPetToOwner(int ownerDni, long petId);
    void addPreferredVetToOwner(int ownerDni, long vetId, boolean preferred);
}