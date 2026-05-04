package ar.com.petmanager.persistence;

import ar.com.petmanager.domain.Vet;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VetDAO implements DAO<Vet, Long> {
    private final DBConnector dbConnector;

    public VetDAO(DBConnector dbConnector) {
        this.dbConnector = dbConnector;
    }

    @Override
    public void create(Vet vet) {
        String sql = "INSERT INTO vets (name, phone, street, city) VALUES (?, ?, ?, ?)";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, vet.getName());
            stmt.setInt(2, vet.getPhone());
            stmt.setString(3, vet.getAddress().getStreet());
            stmt.setString(4, vet.getAddress().getCity());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                vet.getClass(); // dummy to use vet
            }
        } catch (SQLException e) {
            throw new PersistenceException("Error al crear Vet", e);
        }
    }

    @Override
    public void update(Vet vet) {
        String sql = "UPDATE vets SET name = ?, phone = ?, street = ?, city = ? WHERE id_vet = ?";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, vet.getName());
            stmt.setInt(2, vet.getPhone());
           stmt.setString(3, vet.getAddress().getStreet());
            stmt.setString(4, vet.getAddress().getCity());
            stmt.setLong(5, vet.getIdVet());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException("Error al actualizar Vet", e);
        }
    }

    @Override
    public void delete(Long idVet) {
        String sql = "DELETE FROM vets WHERE id_vet = ?";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idVet);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException("Error al eliminar Vet", e);
        }
    }

    @Override
    public Optional<Vet> findById(Long idVet) {
        String sql = "SELECT * FROM vets WHERE id_vet = ?";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idVet);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToVet(rs));
            }
        } catch (SQLException e) {
            throw new PersistenceException("Error al buscar Vet por ID", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Vet> findAll() {
        String sql = "SELECT * FROM vets";
        List<Vet> vets = new ArrayList<>();
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                vets.add(mapResultSetToVet(rs));
            }
        } catch (SQLException e) {
            throw new PersistenceException("Error al buscar todos los Vets", e);
        }
        return vets;
    }

    private Vet mapResultSetToVet(ResultSet rs) throws SQLException {
        return new Vet(
                rs.getString("name"),
                rs.getInt("phone"),
                rs.getString("street"),
                rs.getString("city")
        );
    }
}