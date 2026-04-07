package com.enspd.campusconnect.model;

import java.time.LocalDate;

/**
 * Représente un étudiant inscrit à l'ENSPD.
 * Hérite de Personne.
 */
public class Etudiant extends Personne {

    private String matricule;
    private String annee;      // L1, L2, L3, M1, M2…
    private String departement;

    public Etudiant(String id, String nom, String prenom, String email,
                    LocalDate dateNaissance, String matricule, String annee, String departement) {
        super(id, nom, prenom, email, dateNaissance);
        this.matricule   = matricule;
        this.annee       = annee;
        this.departement = departement;
    }

    // ── Getters ────────────────────────────────────────────────────────────
    public String getMatricule()   { return matricule; }
    public String getAnnee()       { return annee; }
    public String getDepartement() { return departement; }

    // ── Setters ────────────────────────────────────────────────────────────
    public void setMatricule(String matricule)     { this.matricule = matricule; }
    public void setAnnee(String annee)             { this.annee = annee; }
    public void setDepartement(String departement) { this.departement = departement; }

    @Override
    public String getRole() { return "ETUDIANT"; }

    @Override
    public String toString() {
        return super.toString() +
               String.format(" | Matricule: %s | Année: %s | Dept: %s",
                       matricule, annee, departement);
    }
}
