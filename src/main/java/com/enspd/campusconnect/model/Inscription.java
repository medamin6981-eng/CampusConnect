package com.enspd.campusconnect.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente l'inscription d'un étudiant à un groupe.
 * Porte également les notes obtenues.
 */
public class Inscription {

    private String     id;
    private Etudiant   etudiant;
    private Groupe     groupe;
    private LocalDate  dateInscription;
    private List<Note> notes;

    public Inscription(String id, Etudiant etudiant, Groupe groupe) {
        this.id              = id;
        this.etudiant        = etudiant;
        this.groupe          = groupe;
        this.dateInscription = LocalDate.now();
        this.notes           = new ArrayList<>();
    }

    // ── Getters ────────────────────────────────────────────────────────────
    public String    getId()              { return id; }
    public Etudiant  getEtudiant()        { return etudiant; }
    public Groupe    getGroupe()          { return groupe; }
    public LocalDate getDateInscription() { return dateInscription; }
    public List<Note> getNotes()          { return notes; }

    // ── Méthodes métier ────────────────────────────────────────────────────
    public void ajouterNote(Note note) {
        notes.add(note);
    }

    /**
     * Calcule la moyenne pondérée de toutes les notes de cette inscription.
     * @return moyenne entre 0 et 20, ou -1 si aucune note.
     */
    public double calculerMoyenne() {
        if (notes.isEmpty()) return -1;
        double sommeNotes        = 0;
        double sommeCoefficients = 0;
        for (Note note : notes) {
            sommeNotes        += note.getValeur() * note.getCoefficient();
            sommeCoefficients += note.getCoefficient();
        }
        return sommeCoefficients == 0 ? -1 : sommeNotes / sommeCoefficients;
    }

    @Override
    public String toString() {
        double moy = calculerMoyenne();
        return String.format("Inscription[%s] %s %s → Groupe %s | Date: %s | Moyenne: %s",
                id,
                etudiant.getPrenom(), etudiant.getNom(),
                groupe.getCode(),
                dateInscription,
                moy < 0 ? "N/A" : String.format("%.2f/20", moy));
    }
}
