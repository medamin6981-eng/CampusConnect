package com.enspd.campusconnect.repository;

import com.enspd.campusconnect.model.Etudiant;
import com.enspd.campusconnect.util.DatabaseManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EtudiantRepository implements Repository<Etudiant, String> {

    @Override
    public void save(Etudiant etudiant) {
        String sql = "INSERT INTO etudiants (id, nom, prenom, email, date_naissance, matricule, annee, departement) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, etudiant.getId());
            pstmt.setString(2, etudiant.getNom());
            pstmt.setString(3, etudiant.getPrenom());
            pstmt.setString(4, etudiant.getEmail());
            pstmt.setString(5, etudiant.getDateNaissance().toString());
            pstmt.setString(6, etudiant.getMatricule());
            pstmt.setString(7, etudiant.getAnnee());
            pstmt.setString(8, etudiant.getDepartement());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving etudiant: " + e.getMessage());
        }
    }

    @Override
    public void update(Etudiant etudiant) {
        String sql = "UPDATE etudiants SET nom = ?, prenom = ?, email = ?, date_naissance = ?, matricule = ?, annee = ?, departement = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, etudiant.getNom());
            pstmt.setString(2, etudiant.getPrenom());
            pstmt.setString(3, etudiant.getEmail());
            pstmt.setString(4, etudiant.getDateNaissance().toString());
            pstmt.setString(5, etudiant.getMatricule());
            pstmt.setString(6, etudiant.getAnnee());
            pstmt.setString(7, etudiant.getDepartement());
            pstmt.setString(8, etudiant.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating etudiant: " + e.getMessage());
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM etudiants WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting etudiant: " + e.getMessage());
        }
    }

    @Override
    public Optional<Etudiant> findById(String id) {
        String sql = "SELECT * FROM etudiants WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToEtudiant(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding etudiant: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Etudiant> findAll() {
        List<Etudiant> etudiants = new ArrayList<>();
        String sql = "SELECT * FROM etudiants";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                etudiants.add(mapResultSetToEtudiant(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding all etudiants: " + e.getMessage());
        }
        return etudiants;
    }

    private Etudiant mapResultSetToEtudiant(ResultSet rs) throws SQLException {
        return new Etudiant(
                rs.getString("id"),
                rs.getString("nom"),
                rs.getString("prenom"),
                rs.getString("email"),
                LocalDate.parse(rs.getString("date_naissance")),
                rs.getString("matricule"),
                rs.getString("annee"),
                rs.getString("departement")
        );
    }
}
