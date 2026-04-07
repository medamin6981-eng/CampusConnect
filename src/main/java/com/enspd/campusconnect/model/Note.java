package com.enspd.campusconnect.model;

/**
 * Représente une note obtenue dans le cadre d'une inscription.
 */
public class Note {

    private String      id;
    private Inscription inscription;
    private String      libelle;      // ex. "Examen final", "Devoir 1"
    private double      valeur;       // 0 – 20
    private double      coefficient;

    public Note(String id, Inscription inscription, String libelle,
                double valeur, double coefficient) {
        if (valeur < 0 || valeur > 20)
            throw new IllegalArgumentException("La note doit être comprise entre 0 et 20.");
        if (coefficient <= 0)
            throw new IllegalArgumentException("Le coefficient doit être positif.");

        this.id          = id;
        this.inscription = inscription;
        this.libelle     = libelle;
        this.valeur      = valeur;
        this.coefficient = coefficient;
    }

    // ── Getters ────────────────────────────────────────────────────────────
    public String      getId()          { return id; }
    public Inscription getInscription() { return inscription; }
    public String      getLibelle()     { return libelle; }
    public double      getValeur()      { return valeur; }
    public double      getCoefficient() { return coefficient; }

    // ── Setters ────────────────────────────────────────────────────────────
    public void setValeur(double valeur) {
        if (valeur < 0 || valeur > 20)
            throw new IllegalArgumentException("La note doit être comprise entre 0 et 20.");
        this.valeur = valeur;
    }

    @Override
    public String toString() {
        return String.format("Note[%s] %s : %.2f/20 (coeff. %.1f)",
                id, libelle, valeur, coefficient);
    }
}
