package com.enspd.campusconnect.repository;

import com.enspd.campusconnect.model.Cours;
import com.enspd.campusconnect.model.Enseignant;
import com.enspd.campusconnect.util.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CoursRepository implements Repository<Cours, String> {

    private final EnseignantRepository enseignantRepository;

    public CoursRepository(EnseignantRepository enseignantRepository) {
        this.enseignantRepository = enseignantRepository;
    }

    @Override
    public void save(Cours cours) {
        String sql = "INSERT INTO cours (code, intitule, volume_horaire, responsable_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cours.getCode());
            pstmt.setString(2, cours.getIntitule());
            pstmt.setInt(3, cours.getVolumeHoraire());
            pstmt.setString(4, cours.getResponsable().getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving cours: " + e.getMessage());
        }
    }

    @Override
    public void update(Cours cours) {
        String sql = "UPDATE cours SET intitule = ?, volume_horaire = ?, responsable_id = ? WHERE code = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cours.getIntitule());
            pstmt.setInt(2, cours.getVolumeHoraire());
            pstmt.setString(3, cours.getResponsable().getId());
            pstmt.setString(4, cours.getCode());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating cours: " + e.getMessage());
        }
    }

    @Override
    public void delete(String code) {
        String sql = "DELETE FROM cours WHERE code = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, code);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting cours: " + e.getMessage());
        }
    }

    @Override
    public Optional<Cours> findById(String code) {
        String sql = "SELECT * FROM cours WHERE code = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, code);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToCours(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding cours: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Cours> findAll() {
        List<Cours> courses = new ArrayList<>();
        String sql = "SELECT * FROM cours";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                courses.add(mapResultSetToCours(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding all courses: " + e.getMessage());
        }
        return courses;
    }

    private Cours mapResultSetToCours(ResultSet rs) throws SQLException {
        String responsableId = rs.getString("responsable_id");
        Enseignant responsable = enseignantRepository.findById(responsableId).orElse(null);
        
        return new Cours(
                rs.getString("code"),
                rs.getString("intitule"),
                rs.getInt("volume_horaire"),
                responsable
        );
    }
}
