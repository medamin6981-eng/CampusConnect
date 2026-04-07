package com.enspd.campusconnect.service;

import com.enspd.campusconnect.model.*;
import com.enspd.campusconnect.util.DataStore;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service de gestion des notes et des moyennes.
 * TODO Phase 2 : compléter le calcul de moyenne générale.
 */
public class NoteService {

    private final DataStore store = DataStore.getInstance();

    public void ajouterNote(Inscription inscription, String libelle,
                            double valeur, double coefficient) {
        String id = store.nextNoteId();
        Note note = new Note(id, inscription, libelle, valeur, coefficient);
        inscription.ajouterNote(note);
        store.getNotes().add(note);
        System.out.println("Note ajoutée : " + note);
    }

    /**
     * Affiche le bulletin complet d'un étudiant.
     */
    public void afficherBulletin(Etudiant etudiant) {
        List<Inscription> inscs = store.getInscriptions().stream()
                .filter(i -> i.getEtudiant().getId().equals(etudiant.getId()))
                .collect(Collectors.toList());

        if (inscs.isEmpty()) {
            System.out.println("Aucune inscription trouvée pour cet étudiant.");
            return;
        }

        System.out.println("\n══════════════════════════════════════════");
        System.out.printf("  BULLETIN — %s %s (%s)%n",
                etudiant.getPrenom(), etudiant.getNom(), etudiant.getMatricule());
        System.out.println("══════════════════════════════════════════");

        double somme = 0; int count = 0;
        for (Inscription ins : inscs) {
            double moy = ins.calculerMoyenne();
            System.out.printf("  %-30s : %s%n",
                    ins.getGroupe().getCours().getIntitule(),
                    moy < 0 ? "Pas de notes" : String.format("%.2f / 20", moy));
            if (moy >= 0) { somme += moy; count++; }
        }

        System.out.println("──────────────────────────────────────────");
        System.out.printf("  Moyenne générale : %s%n",
                count == 0 ? "N/A" : String.format("%.2f / 20", somme / count));
        System.out.println("══════════════════════════════════════════\n");
    }
}
