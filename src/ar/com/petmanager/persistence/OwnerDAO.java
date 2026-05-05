package ar.com.petmanager.persistence;

import ar.com.petmanager.domain.Owner;
import ar.com.petmanager.domain.Pet;
import ar.com.petmanager.domain.Sex;
import ar.com.petmanager.domain.Vet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OwnerDAO implements DAO<Owner, Integer> {
    private final DBConnector dbConnector;

    public OwnerDAO(DBConnector dbConnector) {
        this.dbConnector = dbConnector;
    }

    @Override
    public void create(Owner owner) {
        String sql = "INSERT INTO persons (dni, name, surname, phone, street, city, type, sex, preferred_vet_id) VALUES (?, ?, ?, ?, ?, ?, 'OWNER', ?, ?)";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, owner.getDni());
            stmt.setString(2, owner.getName());
            stmt.setString(3, owner.getSurname());
            stmt.setLong(4, owner.getPhone());
            stmt.setString(5, owner.getAddress().getStreet());
            stmt.setString(6, owner.getAddress().getCity());
            stmt.setString(7, owner.getSex().name());
            if (owner.getPreferredVet()!=null) {
                stmt.setLong(8, owner.getPreferredVet().getIdVet());
            } else {
                stmt.setNull(8, java.sql.Types.BIGINT);
            }
            stmt.executeUpdate();
            savePets(conn, owner);
        } catch (SQLException e) {
            throw new PersistenceException("Error al crear Owner", e);
        }
    }

    @Override
    public void update(Owner owner) {
        String sql = "UPDATE persons SET name = ?, surname = ?, phone = ?, street = ?, city = ?, sex = ?, preferred_vet_id = ? WHERE dni = ? AND type = 'OWNER'";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, owner.getName());
            stmt.setString(2, owner.getSurname());
            stmt.setLong(3, owner.getPhone());
            stmt.setString(4, owner.getAddress().getStreet());
            stmt.setString(5, owner.getAddress().getCity());
            stmt.setString(6, owner.getSex().name());
            if (owner.getPreferredVet()!=null) {
                stmt.setLong(7, owner.getPreferredVet().getIdVet());
            } else {
                stmt.setNull(7, java.sql.Types.BIGINT);
            }
            stmt.setInt(8, owner.getDni());
            stmt.executeUpdate();
            updatePets(conn, owner);
        } catch (SQLException e) {
            throw new PersistenceException("Error al actualizar Owner", e);
        }
    }

    @Override
    public void delete(Integer dni) {
        String sql = "DELETE FROM persons WHERE dni = ? AND type = 'OWNER'";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dni);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException("Error al eliminar Owner", e);
        }
    }

    @Override
    public Optional<Owner> findById(Integer dni) {
        String sql = "SELECT * FROM persons WHERE dni = ? AND type = 'OWNER'";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dni);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToOwner(rs));
            }
        } catch (SQLException e) {
            throw new PersistenceException("Error al buscar Owner por DNI", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Owner> findAll() {
        String sql = "SELECT * FROM persons WHERE type = 'OWNER'";
        List<Owner> owners = new ArrayList<>();
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                owners.add(mapResultSetToOwner(rs));
            }
        } catch (SQLException e) {
            throw new PersistenceException("Error al buscar todos los Owners", e);
        }
        return owners;
    }

    private Owner mapResultSetToOwner(ResultSet rs) throws SQLException {
        Sex sex;
        try {
            sex = Sex.valueOf(rs.getString("sex"));
        } catch (IllegalArgumentException | SQLException e) {
            sex = Sex.MASCULINO; // default si no existe
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
        owner.setPets(loadPetsForOwner(rs.getInt("dni")));

        long vetId = rs.getLong("preferred_vet_id");
        if (!rs.wasNull()) {
            owner.setPreferredVet(loadVet(vetId));
        }

        return owner;
    }

    private List<Pet> loadPetsForOwner(int ownerDni) {
        List<Pet> pets = new ArrayList<>();
        String sql = "SELECT p.* FROM pets p INNER JOIN owner_pet op ON p.id = op.pet_id WHERE op.owner_dni = ?";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ownerDni);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                pets.add(mapResultSetToPet(rs));
            }
        } catch (SQLException e) {
            throw new PersistenceException("Error al cargar pets del Owner", e);
        }
        return pets;
    }

    private Pet mapResultSetToPet(ResultSet rs) throws SQLException {
        String type = rs.getString("type");
        Pet pet;
        if ("DOG".equals(type)) {
            pet = new ar.com.petmanager.domain.Dog(rs.getString("name"), rs.getString("age"), rs.getDouble("weight"), rs.getString("race"),
                    rs.getBoolean("is_sick"), rs.getString("description"));
        } else {
            pet = new ar.com.petmanager.domain.Cat(rs.getString("name"), rs.getString("age"), rs.getDouble("weight"), rs.getString("race"),
                    rs.getBoolean("is_sick"), rs.getString("description"));
        }
        pet.setId(rs.getLong("id"));
        try {
            pet.setStatus(ar.com.petmanager.domain.PetStatus.valueOf(rs.getString("status")));
        } catch (IllegalArgumentException | SQLException e) {
            // default ACTIVA
        }
        return pet;
    }

    private void savePets(Connection conn, Owner owner) {
        if (owner.getPets()==null || owner.getPets().isEmpty()) return;
        for (Pet pet : owner.getPets()) {
            try (PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO owner_pet (owner_dni, pet_id) VALUES (?, ?)")) {
                stmt.setInt(1, owner.getDni());
                stmt.setLong(2, pet.getId());
                stmt.executeUpdate();
            } catch (SQLException e) {
                // El pet puede no existir aún en BD (está solo en memoria)
                System.err.println("No se pudo relacionar pet " + pet.getId() + ": " + e.getMessage());
            }
        }
    }

    private void updatePets(Connection conn, Owner owner) {
        try (PreparedStatement stmt = conn.prepareStatement(
                "DELETE FROM owner_pet WHERE owner_dni = ?")) {
            stmt.setInt(1, owner.getDni());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al limpiar relaciones: " + e.getMessage());
        }
        savePets(conn, owner);
    }

    private Vet loadVet(long vetId) {
        String sql = "SELECT * FROM vets WHERE id_vet = ?";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, vetId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Vet vet = new Vet(
                        rs.getString("name"),
                        rs.getInt("phone"),
                        rs.getString("street"),
                        rs.getString("city")
                );
                vet.setIdVet(rs.getLong("id_vet"));
                return vet;
            }
        } catch (SQLException e) {
            throw new PersistenceException("Error al cargar Vet preferida del Owner", e);
        }
        return null;
    }
}