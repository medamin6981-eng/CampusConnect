package com.enspd.campusconnect.model;

/**
 * Représente un groupe d'étudiants au sein d'un cours.
 * Ex : CM, TD1, TD2, TP1, TP2…
 */
public class Groupe {

    public enum TypeGroupe { CM, TD, TP }

    private String     code;
    private TypeGroupe type;
    private int        capaciteMax;
    private Cours      cours;
    private Enseignant enseignant;
    private Salle      salle;
    private int        nbInscrits;

    public Groupe(String code, TypeGroupe type, int capaciteMax, Cours cours) {
        this.code        = code;
        this.type        = type;
        this.capaciteMax = capaciteMax;
        this.cours       = cours;
        this.nbInscrits  = 0;
    }

    // ── Getters ────────────────────────────────────────────────────────────
    public String     getCode()        { return code; }
    public TypeGroupe getType()        { return type; }
    public int        getCapaciteMax() { return capaciteMax; }
    public Cours      getCours()       { return cours; }
    public Enseignant getEnseignant()  { return enseignant; }
    public Salle      getSalle()       { return salle; }
    public int        getNbInscrits()  { return nbInscrits; }

    // ── Setters ────────────────────────────────────────────────────────────
    public void setEnseignant(Enseignant enseignant) { this.enseignant = enseignant; }
    public void setSalle(Salle salle)                { this.salle = salle; }
    public void setCapaciteMax(int capaciteMax)      { this.capaciteMax = capaciteMax; }

    // ── Méthodes métier ────────────────────────────────────────────────────
    public boolean estPlein() {
        return nbInscrits >= capaciteMax;
    }

    public void incrementerInscrits() { nbInscrits++; }
    public void decrementerInscrits() { if (nbInscrits > 0) nbInscrits--; }

    public int placesDisponibles() {
        return capaciteMax - nbInscrits;
    }

    @Override
    public String toString() {
        return String.format("Groupe[%s] %s | Cours: %s | Capacité: %d/%d | Salle: %s",
                code, type, cours.getCode(), nbInscrits, capaciteMax,
                salle != null ? salle.getCode() : "Non définie");
    }
}
