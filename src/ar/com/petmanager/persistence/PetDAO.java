package ar.com.petmanager.persistence;

import ar.com.petmanager.domain.Pet;
import ar.com.petmanager.domain.Cat;
import ar.com.petmanager.domain.Dog;
import ar.com.petmanager.domain.Owner;
import ar.com.petmanager.domain.Sex;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PetDAO implements DAO<Pet, Long> {
    private final DBConnector dbConnector;

    public PetDAO(DBConnector dbConnector) {
        this.dbConnector = dbConnector;
    }

    @Override
    public void create(Pet pet) {
        String sql = "INSERT INTO pets (name, age, weight, race, is_sick, description, type, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, pet.getName());
            stmt.setString(2, pet.getAge());
            stmt.setDouble(3, pet.getWeight());
            stmt.setString(4, pet.getRace());
            stmt.setBoolean(5, pet.isSick());
            stmt.setString(6, pet.getDescription());
            stmt.setString(7, pet instanceof Dog ? "DOG" : "CAT");
            stmt.setString(8, pet.getStatus().name());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                pet.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            throw new PersistenceException("Error al crear Pet", e);
        }
    }

    @Override
    public void update(Pet pet) {
        String sql = "UPDATE pets SET name = ?, age = ?, weight = ?, race = ?, is_sick = ?, description = ?, status = ? WHERE id = ?";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, pet.getName());
            stmt.setString(2, pet.getAge());
            stmt.setDouble(3, pet.getWeight());
            stmt.setString(4, pet.getRace());
            stmt.setBoolean(5, pet.isSick());
            stmt.setString(6, pet.getDescription());
            stmt.setString(7, pet.getStatus().name());
            stmt.setLong(8, pet.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException("Error al actualizar Pet", e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM pets WHERE id = ?";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException("Error al eliminar Pet", e);
        }
    }

    @Override
    public Optional<Pet> findById(Long id) {
        String sql = "SELECT * FROM pets WHERE id = ?";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToPet(rs));
            }
        } catch (SQLException e) {
            throw new PersistenceException("Error al buscar Pet por ID", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Pet> findAll() {
        String sql = "SELECT * FROM pets";
        List<Pet> pets = new ArrayList<>();
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                pets.add(mapResultSetToPet(rs));
            }
        } catch (SQLException e) {
            throw new PersistenceException("Error al buscar todos los Pets", e);
        }
        return pets;
    }

    private Pet mapResultSetToPet(ResultSet rs) throws SQLException {
        String type = rs.getString("type");
        Pet pet;
        if ("DOG".equals(type)) {
            pet = new Dog(
                    rs.getString("name"),
                    rs.getString("age"),
                    rs.getDouble("weight"),
                    rs.getString("race"),
                    rs.getBoolean("is_sick"),
                    rs.getString("description")
            );
        } else {
            pet = new Cat(
                    rs.getString("name"),
                    rs.getString("age"),
                    rs.getDouble("weight"),
                    rs.getString("race"),
                    rs.getBoolean("is_sick"),
                    rs.getString("description")
            );
        }
        pet.setId(rs.getLong("id"));
        pet.setOwners(loadOwnersForPet(pet.getId()));
        try {
            pet.setStatus(ar.com.petmanager.domain.PetStatus.valueOf(rs.getString("status")));
        } catch (IllegalArgumentException | SQLException e) {
            // Si el status no existe, dejar default ACTIVA
        }
        return pet;
    }

    private List<Owner> loadOwnersForPet(long petId) {
        List<Owner> owners = new ArrayList<>();
        String sql = "SELECT p.* FROM persons p INNER JOIN owner_pet op ON p.dni = op.owner_dni WHERE op.pet_id = ? AND p.type = 'OWNER'";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, petId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Sex sex;
                try {
                    sex = Sex.valueOf(rs.getString("sex"));
                } catch (IllegalArgumentException | SQLException e) {
                    sex = Sex.MASCULINO;
                }
                Owner owner = new Owner(
                        rs.getInt("dni"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        (int) rs.getLong("phone"),
                        sex,
                        rs.getString("street"),
                        rs.getString("city")
                );
                owners.add(owner);
            }
        } catch (SQLException e) {
            throw new PersistenceException("Error al cargar dueños del Pet", e);
        }
        return owners;
    }
}