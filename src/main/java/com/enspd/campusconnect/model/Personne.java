package com.enspd.campusconnect.model;

import java.time.LocalDate;

/**
 * Classe abstraite représentant toute personne du système.
 * Parente de Etudiant et Enseignant.
 */
public abstract class Personne {

    private String id;
    private String nom;
    private String prenom;
    private String email;
    private LocalDate dateNaissance;

    public Personne(String id, String nom, String prenom, String email, LocalDate dateNaissance) {
        this.id            = id;
        this.nom           = nom;
        this.prenom        = prenom;
        this.email         = email;
        this.dateNaissance = dateNaissance;
    }

    // ── Getters ────────────────────────────────────────────────────────────
    public String    getId()            { return id; }
    public String    getNom()           { return nom; }
    public String    getPrenom()        { return prenom; }
    public String    getEmail()         { return email; }
    public LocalDate getDateNaissance() { return dateNaissance; }

    // ── Setters ────────────────────────────────────────────────────────────
    public void setNom(String nom)                    { this.nom = nom; }
    public void setPrenom(String prenom)              { this.prenom = prenom; }
    public void setEmail(String email)                { this.email = email; }
    public void setDateNaissance(LocalDate d)         { this.dateNaissance = d; }

    // ── Méthode abstraite : chaque sous-classe se présente ─────────────────
    public abstract String getRole();

    @Override
    public String toString() {
        return String.format("[%s] %s %s (%s) | Email: %s | Naissance: %s",
                getRole(), prenom, nom, id, email, dateNaissance);
    }
}
