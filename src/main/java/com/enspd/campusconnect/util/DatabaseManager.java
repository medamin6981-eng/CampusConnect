package com.enspd.campusconnect.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Manages database connection and initialization.
 */
public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:campusconnect.db";
    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL);
        }
        return connection;
    }

    public static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Enable foreign keys
            stmt.execute("PRAGMA foreign_keys = ON;");

            // Create Etudiants table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS etudiants (
                    id TEXT PRIMARY KEY,
                    nom TEXT NOT NULL,
                    prenom TEXT NOT NULL,
                    email TEXT UNIQUE NOT NULL,
                    date_naissance TEXT,
                    matricule TEXT UNIQUE NOT NULL,
                    annee TEXT,
                    departement TEXT
                );
            """);

            // Create Enseignants table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS enseignants (
                    id TEXT PRIMARY KEY,
                    nom TEXT NOT NULL,
                    prenom TEXT NOT NULL,
                    email TEXT UNIQUE NOT NULL,
                    date_naissance TEXT,
                    statut TEXT,
                    departement TEXT
                );
            """);

            // Create Salles table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS salles (
                    code TEXT PRIMARY KEY,
                    capacite INTEGER NOT NULL,
                    type TEXT NOT NULL
                );
            """);

            // Create Cours table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS cours (
                    code TEXT PRIMARY KEY,
                    intitule TEXT NOT NULL,
                    volume_horaire INTEGER NOT NULL,
                    responsable_id TEXT,
                    FOREIGN KEY (responsable_id) REFERENCES enseignants(id)
                );
            """);

            // Create Groupes table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS groupes (
                    code TEXT PRIMARY KEY,
                    type TEXT NOT NULL,
                    capacite_max INTEGER NOT NULL,
                    cours_code TEXT NOT NULL,
                    enseignant_id TEXT,
                    salle_code TEXT,
                    nb_inscrits INTEGER DEFAULT 0,
                    FOREIGN KEY (cours_code) REFERENCES cours(code),
                    FOREIGN KEY (enseignant_id) REFERENCES enseignants(id),
                    FOREIGN KEY (salle_code) REFERENCES salles(code)
                );
            """);

            // Create Inscriptions table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS inscriptions (
                    id TEXT PRIMARY KEY,
                    etudiant_id TEXT NOT NULL,
                    groupe_code TEXT NOT NULL,
                    date_inscription TEXT NOT NULL,
                    FOREIGN KEY (etudiant_id) REFERENCES etudiants(id),
                    FOREIGN KEY (groupe_code) REFERENCES groupes(code)
                );
            """);

            // Create Notes table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS notes (
                    id TEXT PRIMARY KEY,
                    inscription_id TEXT NOT NULL,
                    libelle TEXT NOT NULL,
                    valeur REAL NOT NULL,
                    coefficient REAL NOT NULL,
                    FOREIGN KEY (inscription_id) REFERENCES inscriptions(id)
                );
            """);

            // Create Seances table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS seances (
                    id TEXT PRIMARY KEY,
                    cours_code TEXT NOT NULL,
                    groupe_code TEXT NOT NULL,
                    enseignant_id TEXT NOT NULL,
                    salle_code TEXT NOT NULL,
                    debut TEXT NOT NULL,
                    fin TEXT NOT NULL,
                    FOREIGN KEY (cours_code) REFERENCES cours(code),
                    FOREIGN KEY (groupe_code) REFERENCES groupes(code),
                    FOREIGN KEY (enseignant_id) REFERENCES enseignants(id),
                    FOREIGN KEY (salle_code) REFERENCES salles(code)
                );
            """);

            System.out.println("Database initialized successfully.");
            
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }
}
