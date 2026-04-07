package com.enspd.campusconnect.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente un enseignant de l'ENSPD.
 * Hérite de Personne.
 */
public class Enseignant extends Personne {

    public enum Statut { PERMANENT, VACATAIRE }

    private Statut statut;
    private String departement;
    private List<Cours> enseignements; // liste des cours qu'il dispense

    public Enseignant(String id, String nom, String prenom, String email,
                      LocalDate dateNaissance, Statut statut, String departement) {
        super(id, nom, prenom, email, dateNaissance);
        this.statut        = statut;
        this.departement   = departement;
        this.enseignements = new ArrayList<>();
    }

    // ── Getters ────────────────────────────────────────────────────────────
    public Statut       getStatut()        { return statut; }
    public String       getDepartement()   { return departement; }
    public List<Cours>  getEnseignements() { return enseignements; }

    // ── Setters ────────────────────────────────────────────────────────────
    public void setStatut(Statut statut)           { this.statut = statut; }
    public void setDepartement(String departement) { this.departement = departement; }

    // ── Méthodes métier ────────────────────────────────────────────────────
    public void ajouterCours(Cours cours) {
        if (!enseignements.contains(cours)) {
            enseignements.add(cours);
        }
    }

    public void retirerCours(Cours cours) {
        enseignements.remove(cours);
    }

    @Override
    public String getRole() { return "ENSEIGNANT"; }

    @Override
    public String toString() {
        return super.toString() +
               String.format(" | Statut: %s | Dept: %s | Cours: %d",
                       statut, departement, enseignements.size());
    }
}
