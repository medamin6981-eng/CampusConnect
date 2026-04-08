package com.enspd.campusconnect.repository;

import com.enspd.campusconnect.model.*;
import com.enspd.campusconnect.util.DatabaseManager;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SeanceRepository implements Repository<Seance, String> {

    private final CoursRepository coursRepository;
    private final GroupeRepository groupeRepository;
    private final EnseignantRepository enseignantRepository;
    private final SalleRepository salleRepository;

    public SeanceRepository(CoursRepository coursRepository, 
                           GroupeRepository groupeRepository, 
                           EnseignantRepository enseignantRepository, 
                           SalleRepository salleRepository) {
        this.coursRepository = coursRepository;
        this.groupeRepository = groupeRepository;
        this.enseignantRepository = enseignantRepository;
        this.salleRepository = salleRepository;
    }

    @Override
    public void save(Seance seance) {
        String sql = "INSERT INTO seances (id, cours_code, groupe_code, enseignant_id, salle_code, debut, fin) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, seance.getId());
            pstmt.setString(2, seance.getCours().getCode());
            pstmt.setString(3, seance.getGroupe().getCode());
            pstmt.setString(4, seance.getEnseignant().getId());
            pstmt.setString(5, seance.getSalle().getCode());
            pstmt.setString(6, seance.getDebut().toString());
            pstmt.setString(7, seance.getFin().toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving seance: " + e.getMessage());
        }
    }

    @Override
    public void update(Seance seance) {
        String sql = "UPDATE seances SET cours_code = ?, groupe_code = ?, enseignant_id = ?, salle_code = ?, debut = ?, fin = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, seance.getCours().getCode());
            pstmt.setString(2, seance.getGroupe().getCode());
            pstmt.setString(3, seance.getEnseignant().getId());
            pstmt.setString(4, seance.getSalle().getCode());
            pstmt.setString(5, seance.getDebut().toString());
            pstmt.setString(6, seance.getFin().toString());
            pstmt.setString(7, seance.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating seance: " + e.getMessage());
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM seances WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting seance: " + e.getMessage());
        }
    }

    @Override
    public Optional<Seance> findById(String id) {
        String sql = "SELECT * FROM seances WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToSeance(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding seance: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Seance> findAll() {
        List<Seance> seances = new ArrayList<>();
        String sql = "SELECT * FROM seances";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                seances.add(mapResultSetToSeance(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding all seances: " + e.getMessage());
        }
        return seances;
    }

    private Seance mapResultSetToSeance(ResultSet rs) throws SQLException {
        Cours cours = coursRepository.findById(rs.getString("cours_code")).orElse(null);
        Groupe groupe = groupeRepository.findById(rs.getString("groupe_code")).orElse(null);
        Enseignant enseignant = enseignantRepository.findById(rs.getString("enseignant_id")).orElse(null);
        Salle salle = salleRepository.findById(rs.getString("salle_code")).orElse(null);
        
        return new Seance(
                rs.getString("id"),
                cours,
                groupe,
                enseignant,
                salle,
                LocalDateTime.parse(rs.getString("debut")),
                LocalDateTime.parse(rs.getString("fin"))
        );
    }
}
