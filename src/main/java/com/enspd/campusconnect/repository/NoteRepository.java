package com.enspd.campusconnect.repository;

import com.enspd.campusconnect.model.Inscription;
import com.enspd.campusconnect.model.Note;
import com.enspd.campusconnect.util.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NoteRepository implements Repository<Note, String> {

    private final InscriptionRepository inscriptionRepository;

    public NoteRepository(InscriptionRepository inscriptionRepository) {
        this.inscriptionRepository = inscriptionRepository;
    }

    @Override
    public void save(Note note) {
        String sql = "INSERT INTO notes (id, inscription_id, libelle, valeur, coefficient) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, note.getId());
            pstmt.setString(2, note.getInscription().getId());
            pstmt.setString(3, note.getLibelle());
            pstmt.setDouble(4, note.getValeur());
            pstmt.setDouble(5, note.getCoefficient());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving note: " + e.getMessage());
        }
    }

    @Override
    public void update(Note note) {
        String sql = "UPDATE notes SET inscription_id = ?, libelle = ?, valeur = ?, coefficient = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, note.getInscription().getId());
            pstmt.setString(2, note.getLibelle());
            pstmt.setDouble(3, note.getValeur());
            pstmt.setDouble(4, note.getCoefficient());
            pstmt.setString(5, note.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating note: " + e.getMessage());
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM notes WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting note: " + e.getMessage());
        }
    }

    @Override
    public Optional<Note> findById(String id) {
        String sql = "SELECT * FROM notes WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToNote(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding note: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Note> findAll() {
        List<Note> notes = new ArrayList<>();
        String sql = "SELECT * FROM notes";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                notes.add(mapResultSetToNote(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding all notes: " + e.getMessage());
        }
        return notes;
    }

    private Note mapResultSetToNote(ResultSet rs) throws SQLException {
        String inscriptionId = rs.getString("inscription_id");
        Inscription inscription = inscriptionRepository.findById(inscriptionId).orElse(null);
        
        return new Note(
                rs.getString("id"),
                inscription,
                rs.getString("libelle"),
                rs.getDouble("valeur"),
                rs.getDouble("coefficient")
        );
    }
}
