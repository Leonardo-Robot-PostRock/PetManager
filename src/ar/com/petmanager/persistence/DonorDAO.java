package ar.com.petmanager.persistence;

import ar.com.petmanager.domain.Donor;
import ar.com.petmanager.domain.Sex;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DonorDAO implements DAO<Donor, Integer> {
    private final DBConnector dbConnector;

    public DonorDAO(DBConnector dbConnector) {
        this.dbConnector = dbConnector;
    }

    @Override
    public void create(Donor donor) {
        String sql = "INSERT INTO persons (dni, name, surname, phone, street, city, type, sex) VALUES (?, ?, ?, ?, ?, ?, 'DONOR', ?)";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, donor.getDni());
            stmt.setString(2, donor.getName());
            stmt.setString(3, donor.getSurname());
            stmt.setLong(4, donor.getPhone());
            stmt.setString(5, donor.getAddress().getStreet());
            stmt.setString(6, donor.getAddress().getCity());
            stmt.setString(7, donor.getSex().name());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException("Error al crear Donor", e);
        }
    }

    @Override
    public void update(Donor donor) {
        String sql = "UPDATE persons SET name = ?, surname = ?, phone = ?, street = ?, city = ?, sex = ? WHERE dni = ? AND type = 'DONOR'";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, donor.getName());
            stmt.setString(2, donor.getSurname());
            stmt.setLong(3, donor.getPhone());
            stmt.setString(4, donor.getAddress().getStreet());
            stmt.setString(5, donor.getAddress().getCity());
            stmt.setString(6, donor.getSex().name());
            stmt.setInt(7, donor.getDni());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException("Error al actualizar Donor", e);
        }
    }

    @Override
    public void delete(Integer dni) {
        String sql = "DELETE FROM persons WHERE dni = ? AND type = 'DONOR'";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dni);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException("Error al eliminar Donor", e);
        }
    }

    @Override
    public Optional<Donor> findById(Integer dni) {
        String sql = "SELECT * FROM persons WHERE dni = ? AND type = 'DONOR'";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, dni);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToDonor(rs));
            }
        } catch (SQLException e) {
            throw new PersistenceException("Error al buscar Donor por DNI", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Donor> findAll() {
        String sql = "SELECT * FROM persons WHERE type = 'DONOR'";
        List<Donor> donors = new ArrayList<>();
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                donors.add(mapResultSetToDonor(rs));
            }
        } catch (SQLException e) {
            throw new PersistenceException("Error al buscar todos los Donors", e);
        }
        return donors;
    }

    private Donor mapResultSetToDonor(ResultSet rs) throws SQLException {
        Sex sex;
        try {
            sex = Sex.valueOf(rs.getString("sex"));
        } catch (IllegalArgumentException | SQLException e) {
            sex = Sex.MASCULINO; // default
        }
        return new Donor(
                rs.getInt("dni"),
                rs.getString("name"),
                rs.getString("surname"),
                rs.getLong("phone"),
                sex,
                rs.getString("street"),
                rs.getString("city")
        );
    }
}