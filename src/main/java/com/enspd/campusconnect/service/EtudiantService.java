package com.enspd.campusconnect.service;

import com.enspd.campusconnect.model.*;
import com.enspd.campusconnect.util.DataStore;

import java.util.List;

/**
 * Service de gestion des étudiants.
 * TODO Phase 2 : compléter les méthodes.
 */
public class EtudiantService {

    private final DataStore store = DataStore.getInstance();

    public void ajouterEtudiant(Etudiant etudiant) {
        store.getEtudiants().add(etudiant);
    }

    public List<Etudiant> listerTous() {
        return store.getEtudiants();
    }

    public Etudiant rechercherParMatricule(String matricule) {
        return store.findEtudiantByMatricule(matricule);
    }

    public boolean supprimerEtudiant(String id) {
        return store.getEtudiants().removeIf(e -> e.getId().equals(id));
    }
}
