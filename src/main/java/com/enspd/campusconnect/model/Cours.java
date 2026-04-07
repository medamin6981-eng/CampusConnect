package com.enspd.campusconnect.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente un cours dispensé à l'ENSPD.
 */
public class Cours {

    private String      code;           // ex. INFO-301
    private String      intitule;
    private int         volumeHoraire;  // en heures
    private Enseignant  responsable;
    private List<Groupe> groupes;

    public Cours(String code, String intitule, int volumeHoraire, Enseignant responsable) {
        this.code          = code;
        this.intitule      = intitule;
        this.volumeHoraire = volumeHoraire;
        this.responsable   = responsable;
        this.groupes       = new ArrayList<>();
    }

    // ── Getters ────────────────────────────────────────────────────────────
    public String       getCode()          { return code; }
    public String       getIntitule()      { return intitule; }
    public int          getVolumeHoraire() { return volumeHoraire; }
    public Enseignant   getResponsable()   { return responsable; }
    public List<Groupe> getGroupes()       { return groupes; }

    // ── Setters ────────────────────────────────────────────────────────────
    public void setIntitule(String intitule)        { this.intitule = intitule; }
    public void setVolumeHoraire(int vh)            { this.volumeHoraire = vh; }
    public void setResponsable(Enseignant e)        { this.responsable = e; }

    // ── Méthodes métier ────────────────────────────────────────────────────
    public void ajouterGroupe(Groupe groupe) {
        if (!groupes.contains(groupe)) {
            groupes.add(groupe);
        }
    }

    public void retirerGroupe(Groupe groupe) {
        groupes.remove(groupe);
    }

    @Override
    public String toString() {
        return String.format("Cours[%s] %s | %dh | Responsable: %s %s | Groupes: %d",
                code, intitule, volumeHoraire,
                responsable.getPrenom(), responsable.getNom(),
                groupes.size());
    }
}
