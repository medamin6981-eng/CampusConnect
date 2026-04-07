package com.enspd.campusconnect.model;

/**
 * Représente une salle physique de l'ENSPD.
 */
public class Salle {

    public enum TypeSalle { AMPHI, CLASSIQUE, TP_INFORMATIQUE, TP_LABO }

    private String    code;
    private int       capacite;
    private TypeSalle type;

    public Salle(String code, int capacite, TypeSalle type) {
        this.code     = code;
        this.capacite = capacite;
        this.type     = type;
    }

    // ── Getters ────────────────────────────────────────────────────────────
    public String    getCode()     { return code; }
    public int       getCapacite() { return capacite; }
    public TypeSalle getType()     { return type; }

    // ── Setters ────────────────────────────────────────────────────────────
    public void setCapacite(int capacite) { this.capacite = capacite; }
    public void setType(TypeSalle type)   { this.type = type; }

    // ── Méthodes métier ────────────────────────────────────────────────────
    public boolean peutAccueillir(int nombreEtudiants) {
        return nombreEtudiants <= capacite;
    }

    @Override
    public String toString() {
        return String.format("Salle[%s] Type: %s | Capacité: %d places",
                code, type, capacite);
    }
}
