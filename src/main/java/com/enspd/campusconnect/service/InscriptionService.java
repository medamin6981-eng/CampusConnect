package com.enspd.campusconnect.service;

import com.enspd.campusconnect.model.*;
import com.enspd.campusconnect.util.DataStore;

/**
 * Service de gestion des inscriptions.
 * TODO Phase 2 : implémenter la vérification de capacité.
 */
public class InscriptionService {

    private final DataStore store = DataStore.getInstance();

    /**
     * Inscrit un étudiant à un groupe si les capacités le permettent.
     * @return true si inscription réussie, false sinon.
     */
    public boolean inscrire(Etudiant etudiant, Groupe groupe) {
        // TODO : vérifier capacité groupe
        // TODO : vérifier capacité salle
        // TODO : vérifier double inscription
        String id = store.nextInscriptionId();
        Inscription inscription = new Inscription(id, etudiant, groupe);
        store.getInscriptions().add(inscription);
        groupe.incrementerInscrits();
        return true;
    }

    public void listerInscriptionsEtudiant(Etudiant etudiant) {
        store.getInscriptions().stream()
             .filter(i -> i.getEtudiant().getId().equals(etudiant.getId()))
             .forEach(System.out::println);
    }
}
