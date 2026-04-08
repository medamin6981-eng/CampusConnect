package com.enspd.campusconnect.repository;

import com.enspd.campusconnect.model.Etudiant;
import com.enspd.campusconnect.model.Groupe;
import com.enspd.campusconnect.model.Inscription;
import com.enspd.campusconnect.util.DatabaseManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InscriptionRepository implements Repository<Inscription, String> {

    private final EtudiantRepository etudiantRepository;
    private final GroupeRepository groupeRepository;

    public InscriptionRepository(EtudiantRepository etudiantRepository, GroupeRepository groupeRepository) {
        this.etudiantRepository = etudiantRepository;
        this.groupeRepository = groupeRepository;
    }

    @Override
    public void save(Inscription inscription) {
        String sql = "INSERT INTO inscriptions (id, etudiant_id, groupe_code, date_inscription) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, inscription.getId());
            pstmt.setString(2, inscription.getEtudiant().getId());
            pstmt.setString(3, inscription.getGroupe().getCode());
            pstmt.setString(4, inscription.getDateInscription().toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving inscription: " + e.getMessage());
        }
    }

    @Override
    public void update(Inscription inscription) {
        String sql = "UPDATE inscriptions SET etudiant_id = ?, groupe_code = ?, date_inscription = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, inscription.getEtudiant().getId());
            pstmt.setString(2, inscription.getGroupe().getCode());
            pstmt.setString(3, inscription.getDateInscription().toString());
            pstmt.setString(4, inscription.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating inscription: " + e.getMessage());
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM inscriptions WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting inscription: " + e.getMessage());
        }
    }

    @Override
    public Optional<Inscription> findById(String id) {
        String sql = "SELECT * FROM inscriptions WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToInscription(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding inscription: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Inscription> findAll() {
        List<Inscription> inscriptions = new ArrayList<>();
        String sql = "SELECT * FROM inscriptions";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                inscriptions.add(mapResultSetToInscription(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding all inscriptions: " + e.getMessage());
        }
        return inscriptions;
    }

    private Inscription mapResultSetToInscription(ResultSet rs) throws SQLException {
        String etudiantId = rs.getString("etudiant_id");
        Etudiant etudiant = etudiantRepository.findById(etudiantId).orElse(null);
        
        String groupeCode = rs.getString("groupe_code");
        Groupe groupe = groupeRepository.findById(groupeCode).orElse(null);
        
        Inscription inscription = new Inscription(
                rs.getString("id"),
                etudiant,
                groupe
        );
        
        // Note: The model's constructor sets dateInscription to LocalDate.now()
        // We might want to set it from the result set if we had a setter.
        // Reflection or package-private setter would be needed.
        
        return inscription;
    }
}
