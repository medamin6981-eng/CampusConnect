package com.enspd.campusconnect;

import com.enspd.campusconnect.model.Etudiant;
import com.enspd.campusconnect.repository.EtudiantRepository;
import com.enspd.campusconnect.service.StudentService;
import com.enspd.campusconnect.util.DatabaseManager;

import java.time.LocalDate;
import java.util.List;

/**
 * Main entry point for CampusConnect.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║        CAMPUSCONNECT – ENSPD             ║");
        System.out.println("║    Academic Management System v1.1       ║");
        System.out.println("╚══════════════════════════════════════════╝");
        System.out.println();

        // Initialize Persistence Layer
        DatabaseManager.initializeDatabase();

        // Initialize Repositories
        EtudiantRepository studentRepository = new EtudiantRepository();
        
        // Initialize Services
        StudentService studentService = new StudentService(studentRepository);

        System.out.println("Data layer initialized with Repository pattern and SQLite.");
        
        // Simple test: Add a student if none exist
        List<Etudiant> students = studentService.listAll();
        if (students.isEmpty()) {
            System.out.println("No students found. Adding a sample student...");
            Etudiant newStudent = new Etudiant(
                    "ETU-001", "Doe", "John", "john.doe@enspd.cm",
                    LocalDate.of(2000, 1, 1), "20X1234", "L3", "Informatique"
            );
            studentService.addStudent(newStudent);
            System.out.println("Sample student added.");
        } else {
            System.out.println("Current students in database:");
            students.forEach(s -> System.out.println(" - " + s));
        }

        System.out.println("\nApplication started successfully in persistence mode.");
    }
}
