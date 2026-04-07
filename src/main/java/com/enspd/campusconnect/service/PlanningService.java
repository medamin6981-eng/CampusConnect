package com.enspd.campusconnect.service;

import com.enspd.campusconnect.model.*;
import com.enspd.campusconnect.util.DataStore;

import java.util.ArrayList;
import java.util.List;

/**
 * Service de gestion du planning et de détection des conflits.
 * TODO Phase 2 : implémenter les 3 types de conflits.
 */
public class PlanningService {

    private final DataStore store = DataStore.getInstance();

    /**
     * Tente d'ajouter une séance après vérification des conflits.
     * @return message de succès ou d'erreur.
     */
    public String ajouterSeance(Seance seance) {
        List<String> conflits = detecterConflits(seance);
        if (!conflits.isEmpty()) {
            return "CONFLIT DÉTECTÉ :\n" + String.join("\n", conflits);
        }
        store.getSeances().add(seance);
        return "Séance ajoutée avec succès.";
    }

    /**
     * Détecte tous les conflits potentiels pour une séance donnée.
     */
    public List<String> detecterConflits(Seance nouvelle) {
        List<String> conflits = new ArrayList<>();
        for (Seance existante : store.getSeances()) {
            if (!existante.getId().equals(nouvelle.getId()) && existante.chevauche(nouvelle)) {
                // TODO : conflit salle
                // TODO : conflit enseignant
                // TODO : conflit groupe (étudiants)
            }
        }
        return conflits;
    }

    public void afficherPlanning() {
        if (store.getSeances().isEmpty()) {
            System.out.println("Aucune séance planifiée.");
            return;
        }
        store.getSeances().stream()
             .sorted((a, b) -> a.getDebut().compareTo(b.getDebut()))
             .forEach(System.out::println);
    }
}
