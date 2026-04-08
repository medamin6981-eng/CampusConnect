package com.enspd.campusconnect.service;

import com.enspd.campusconnect.model.Etudiant;
import com.enspd.campusconnect.repository.EtudiantRepository;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing students.
 */
public class StudentService {

    private final EtudiantRepository repository;

    public StudentService(EtudiantRepository repository) {
        this.repository = repository;
    }

    public void addStudent(Etudiant student) {
        repository.save(student);
    }

    public List<Etudiant> listAll() {
        return repository.findAll();
    }

    public Optional<Etudiant> findById(String id) {
        return repository.findById(id);
    }

    public void deleteStudent(String id) {
        repository.delete(id);
    }
    
    // Additional methods can be added here
}
