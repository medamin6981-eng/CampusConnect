package com.enspd.campusconnect.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Représente une séance de cours planifiée.
 * Utilisée pour la détection de conflits de planning.
 */
public class Seance {

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private String        id;
    private Cours         cours;
    private Groupe        groupe;
    private Enseignant    enseignant;
    private Salle         salle;
    private LocalDateTime debut;
    private LocalDateTime fin;

    public Seance(String id, Cours cours, Groupe groupe,
                  Enseignant enseignant, Salle salle,
                  LocalDateTime debut, LocalDateTime fin) {
        if (!fin.isAfter(debut))
            throw new IllegalArgumentException("La fin doit être après le début.");
        this.id         = id;
        this.cours      = cours;
        this.groupe     = groupe;
        this.enseignant = enseignant;
        this.salle      = salle;
        this.debut      = debut;
        this.fin        = fin;
    }

    // ── Getters ────────────────────────────────────────────────────────────
    public String        getId()         { return id; }
    public Cours         getCours()      { return cours; }
    public Groupe        getGroupe()     { return groupe; }
    public Enseignant    getEnseignant() { return enseignant; }
    public Salle         getSalle()      { return salle; }
    public LocalDateTime getDebut()      { return debut; }
    public LocalDateTime getFin()        { return fin; }

    // ── Setters ────────────────────────────────────────────────────────────
    public void setDebut(LocalDateTime debut) { this.debut = debut; }
    public void setFin(LocalDateTime fin)     { this.fin = fin; }

    /**
     * Vérifie si cette séance chevauche temporellement une autre.
     */
    public boolean chevauche(Seance autre) {
        return this.debut.isBefore(autre.fin) && this.fin.isAfter(autre.debut);
    }

    @Override
    public String toString() {
        return String.format("Séance[%s] %s | Groupe: %s | Enseignant: %s %s | Salle: %s | %s → %s",
                id,
                cours.getIntitule(),
                groupe.getCode(),
                enseignant.getPrenom(), enseignant.getNom(),
                salle.getCode(),
                debut.format(FMT),
                fin.format(FMT));
    }
}
