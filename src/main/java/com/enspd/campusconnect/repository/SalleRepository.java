package com.enspd.campusconnect.repository;

import com.enspd.campusconnect.model.Salle;
import com.enspd.campusconnect.util.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SalleRepository implements Repository<Salle, String> {

    @Override
    public void save(Salle salle) {
        String sql = "INSERT INTO salles (code, capacite, type) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, salle.getCode());
            pstmt.setInt(2, salle.getCapacite());
            pstmt.setString(3, salle.getType().name());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving salle: " + e.getMessage());
        }
    }

    @Override
    public void update(Salle salle) {
        String sql = "UPDATE salles SET capacite = ?, type = ? WHERE code = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, salle.getCapacite());
            pstmt.setString(2, salle.getType().name());
            pstmt.setString(3, salle.getCode());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating salle: " + e.getMessage());
        }
    }

    @Override
    public void delete(String code) {
        String sql = "DELETE FROM salles WHERE code = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, code);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting salle: " + e.getMessage());
        }
    }

    @Override
    public Optional<Salle> findById(String code) {
        String sql = "SELECT * FROM salles WHERE code = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, code);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToSalle(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding salle: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Salle> findAll() {
        List<Salle> salles = new ArrayList<>();
        String sql = "SELECT * FROM salles";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                salles.add(mapResultSetToSalle(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding all salles: " + e.getMessage());
        }
        return salles;
    }

    private Salle mapResultSetToSalle(ResultSet rs) throws SQLException {
        return new Salle(
                rs.getString("code"),
                rs.getInt("capacite"),
                Salle.TypeSalle.valueOf(rs.getString("type"))
        );
    }
}
