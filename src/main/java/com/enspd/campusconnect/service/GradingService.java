package com.enspd.campusconnect.service;

import com.enspd.campusconnect.model.Etudiant;
import com.enspd.campusconnect.model.Inscription;
import com.enspd.campusconnect.model.Note;
import com.enspd.campusconnect.repository.NoteRepository;

import java.util.List;
import java.util.UUID;

/**
 * Service for managing grades and transcripts.
 */
public class GradingService {

    private final NoteRepository repository;

    public GradingService(NoteRepository repository) {
        this.repository = repository;
    }

    public List<Note> listAll() {
        return repository.findAll();
    }

    public void addGrade(Note note) {
        note.getInscription().ajouterNote(note);
        repository.save(note);
    }

    public void addGrade(Inscription enrollment, String label, double value, double coefficient) {
        String id = "NOTE-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
        Note grade = new Note(id, enrollment, label, value, coefficient);
        addGrade(grade);
    }

    public void printTranscript(Etudiant student, List<Inscription> enrollments) {
        System.out.println("\n==========================================");
        System.out.printf("  TRANSCRIPT - %s %s (%s)%n",
                student.getPrenom(), student.getNom(), student.getMatricule());
        System.out.println("==========================================");

        double totalSum = 0;
        int count = 0;
        for (Inscription enrollment : enrollments) {
            double average = enrollment.calculerMoyenne();
            System.out.printf("  %-30s : %s%n",
                    enrollment.getGroupe().getCours().getIntitule(),
                    average < 0 ? "No grades" : String.format("%.2f / 20", average));
            if (average >= 0) {
                totalSum += average;
                count++;
            }
        }

        System.out.println("------------------------------------------");
        System.out.printf("  Overall GPA: %s%n",
                count == 0 ? "N/A" : String.format("%.2f / 20", totalSum / count));
        System.out.println("==========================================\n");
    }
}
