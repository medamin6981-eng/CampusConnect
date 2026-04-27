package com.enspd.campusconnect.service;

import com.enspd.campusconnect.model.Etudiant;
import com.enspd.campusconnect.model.Groupe;
import com.enspd.campusconnect.model.Inscription;
import com.enspd.campusconnect.repository.InscriptionRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for managing enrollments.
 */
public class EnrollmentService {

    private final InscriptionRepository repository;

    public EnrollmentService(InscriptionRepository repository) {
        this.repository = repository;
    }

    /**
     * Enrolls a student in a group.
     * @return The created inscription.
     */
    public Inscription enroll(Etudiant student, Groupe group) {
        // TODO: Validate capacity
        String id = "INS-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
        Inscription inscription = new Inscription(id, student, group);
        repository.save(inscription);
        group.incrementerInscrits();
        return inscription;
    }

    public List<Inscription> listAll() {
        return repository.findAll();
    }

    public List<Inscription> getEnrollmentsByStudent(String studentId) {
        return repository.findAll().stream()
                .filter(i -> i.getEtudiant().getId().equals(studentId))
                .collect(Collectors.toList());
    }
}
