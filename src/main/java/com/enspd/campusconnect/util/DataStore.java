package com.enspd.campusconnect.util;

import com.enspd.campusconnect.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Stockage central en mémoire de toutes les entités.
 * Singleton : une seule instance pour toute l'application.
 */
public class DataStore {

    private static DataStore instance;

    private final List<Etudiant>    etudiants    = new ArrayList<>();
    private final List<Enseignant>  enseignants  = new ArrayList<>();
    private final List<Cours>       cours        = new ArrayList<>();
    private final List<Groupe>      groupes      = new ArrayList<>();
    private final List<Salle>       salles       = new ArrayList<>();
    private final List<Inscription> inscriptions = new ArrayList<>();
    private final List<Note>        notes        = new ArrayList<>();
    private final List<Seance>      seances      = new ArrayList<>();

    private DataStore() {}

    public static DataStore getInstance() {
        if (instance == null) instance = new DataStore();
        return instance;
    }

    // ── Accès aux listes ───────────────────────────────────────────────────
    public List<Etudiant>    getEtudiants()    { return etudiants; }
    public List<Enseignant>  getEnseignants()  { return enseignants; }
    public List<Cours>       getCours()        { return cours; }
    public List<Groupe>      getGroupes()      { return groupes; }
    public List<Salle>       getSalles()       { return salles; }
    public List<Inscription> getInscriptions() { return inscriptions; }
    public List<Note>        getNotes()        { return notes; }
    public List<Seance>      getSeances()      { return seances; }

    // ── Recherches rapides ─────────────────────────────────────────────────
    public Etudiant findEtudiantById(String id) {
        return etudiants.stream().filter(e -> e.getId().equals(id)).findFirst().orElse(null);
    }

    public Etudiant findEtudiantByMatricule(String matricule) {
        return etudiants.stream().filter(e -> e.getMatricule().equals(matricule)).findFirst().orElse(null);
    }

    public Enseignant findEnseignantById(String id) {
        return enseignants.stream().filter(e -> e.getId().equals(id)).findFirst().orElse(null);
    }

    public Cours findCoursByCode(String code) {
        return cours.stream().filter(c -> c.getCode().equals(code)).findFirst().orElse(null);
    }

    public Groupe findGroupeByCode(String code) {
        return groupes.stream().filter(g -> g.getCode().equals(code)).findFirst().orElse(null);
    }

    public Salle findSalleByCode(String code) {
        return salles.stream().filter(s -> s.getCode().equals(code)).findFirst().orElse(null);
    }

    // ── Génération d'ID séquentiels ────────────────────────────────────────
    public String nextEtudiantId()    { return "ETU-" + String.format("%03d", etudiants.size() + 1); }
    public String nextEnseignantId()  { return "ENS-" + String.format("%03d", enseignants.size() + 1); }
    public String nextInscriptionId() { return "INS-" + String.format("%03d", inscriptions.size() + 1); }
    public String nextNoteId()        { return "NOTE-"+ String.format("%03d", notes.size() + 1); }
    public String nextSeanceId()      { return "SNC-" + String.format("%03d", seances.size() + 1); }
}
