package ar.com.petmanager.data;

import ar.com.petmanager.domain.*;
import ar.com.petmanager.persistence.*;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class DataAccessImpl implements DataAccess {
    private final OwnerDAO ownerDAO;
    private final DonorDAO donorDAO;
    private final VetDAO vetDAO;
    private final PetDAO petDAO;
    private final DBConnector dbConnector;

    public DataAccessImpl() throws PersistenceException {
        this.dbConnector = DBConnector.getInstance();
        this.ownerDAO = new OwnerDAO(dbConnector);
        this.donorDAO = new DonorDAO(dbConnector);
        this.vetDAO = new VetDAO(dbConnector);
        this.petDAO = new PetDAO(dbConnector);
    }

    // Owner operations
    @Override
    public void saveOwner(Owner owner) {
        ownerDAO.create(owner);
    }

    @Override
    public void updateOwner(Owner owner) {
        ownerDAO.update(owner);
    }

    @Override
    public void deleteOwner(int dni) {
        ownerDAO.delete(dni);
    }

    @Override
    public Owner getOwner(int dni) {
        return ownerDAO.findById(dni).orElse(null);
    }

    @Override
    public List<Owner> getAllOwners() {
        return ownerDAO.findAll();
    }

    // Donor operations
    @Override
    public void saveDonor(Donor donor) {
        donorDAO.create(donor);
    }

    @Override
    public void updateDonor(Donor donor) {
        donorDAO.update(donor);
    }

    @Override
    public void deleteDonor(int dni) {
        donorDAO.delete(dni);
    }

    @Override
    public Donor getDonor(int dni) {
        return donorDAO.findById(dni).orElse(null);
    }

    @Override
    public List<Donor> getAllDonors() {
        return donorDAO.findAll();
    }

    // Vet operations
    @Override
    public void saveVet(Vet vet) {
        vetDAO.create(vet);
    }

    @Override
    public void updateVet(Vet vet) {
        vetDAO.update(vet);
    }

    @Override
    public void deleteVet(long idVet) {
        vetDAO.delete(idVet);
    }

    @Override
    public Vet getVet(long idVet) {
        return vetDAO.findById(idVet).orElse(null);
    }

    @Override
    public List<Vet> getAllVets() {
        return vetDAO.findAll();
    }

    // Pet operations
    @Override
    public void savePet(Pet pet) {
        petDAO.create(pet);
    }

    @Override
    public void updatePet(Pet pet) {
        petDAO.update(pet);
    }

    @Override
    public void deletePet(long id) {
        petDAO.delete(id);
    }

    @Override
    public Pet getPet(long id) {
        return petDAO.findById(id).orElse(null);
    }

    @Override
    public List<Pet> getAllPets() {
        return petDAO.findAll();
    }

    // Relationship operations
    @Override
    public void addPetToOwner(int ownerDni, long petId) {
        String sql = "INSERT IGNORE INTO owner_pet (owner_dni, pet_id) VALUES (?, ?)";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ownerDni);
            stmt.setLong(2, petId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException("Error al relacionar Owner con Pet", e);
        }
    }

    @Override
    public void removeAllOwnersFromPet(long petId) {
        String sql = "DELETE FROM owner_pet WHERE pet_id = ?";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, petId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException("Error al quitar dueños del Pet", e);
        }
    }

    @Override
    public void addPreferredVetToOwner(int ownerDni, long vetId, boolean preferred) {
        String sql = "INSERT INTO owner_vet (owner_dni, vet_id, is_preferred) VALUES (?, ?, ?)";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ownerDni);
            stmt.setLong(2, vetId);
            stmt.setBoolean(3, preferred);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException("Error al relacionar Owner con Vet", e);
        }
    }
}