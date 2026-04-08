package com.enspd.campusconnect.repository;

import com.enspd.campusconnect.model.Enseignant;
import com.enspd.campusconnect.util.DatabaseManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EnseignantRepository implements Repository<Enseignant, String> {

    @Override
    public void save(Enseignant enseignant) {
        String sql = "INSERT INTO enseignants (id, nom, prenom, email, date_naissance, statut, departement) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, enseignant.getId());
            pstmt.setString(2, enseignant.getNom());
            pstmt.setString(3, enseignant.getPrenom());
            pstmt.setString(4, enseignant.getEmail());
            pstmt.setString(5, enseignant.getDateNaissance().toString());
            pstmt.setString(6, enseignant.getStatut().name());
            pstmt.setString(7, enseignant.getDepartement());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving enseignant: " + e.getMessage());
        }
    }

    @Override
    public void update(Enseignant enseignant) {
        String sql = "UPDATE enseignants SET nom = ?, prenom = ?, email = ?, date_naissance = ?, statut = ?, departement = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, enseignant.getNom());
            pstmt.setString(2, enseignant.getPrenom());
            pstmt.setString(3, enseignant.getEmail());
            pstmt.setString(4, enseignant.getDateNaissance().toString());
            pstmt.setString(5, enseignant.getStatut().name());
            pstmt.setString(6, enseignant.getDepartement());
            pstmt.setString(7, enseignant.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating enseignant: " + e.getMessage());
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM enseignants WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting enseignant: " + e.getMessage());
        }
    }

    @Override
    public Optional<Enseignant> findById(String id) {
        String sql = "SELECT * FROM enseignants WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToEnseignant(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding enseignant: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Enseignant> findAll() {
        List<Enseignant> enseignants = new ArrayList<>();
        String sql = "SELECT * FROM enseignants";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                enseignants.add(mapResultSetToEnseignant(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding all enseignants: " + e.getMessage());
        }
        return enseignants;
    }

    private Enseignant mapResultSetToEnseignant(ResultSet rs) throws SQLException {
        return new Enseignant(
                rs.getString("id"),
                rs.getString("nom"),
                rs.getString("prenom"),
                rs.getString("email"),
                LocalDate.parse(rs.getString("date_naissance")),
                Enseignant.Statut.valueOf(rs.getString("statut")),
                rs.getString("departement")
        );
    }
}
