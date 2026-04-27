package com.enspd.campusconnect.service;

import com.enspd.campusconnect.model.Seance;
import com.enspd.campusconnect.repository.SeanceRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Service for managing schedules and conflict detection.
 */
public class ScheduleService {

    private final SeanceRepository repository;

    public ScheduleService(SeanceRepository repository) {
        this.repository = repository;
    }

    public List<Seance> listAll() {
        return repository.findAll();
    }

    /**
     * Attempts to add a session after conflict verification.
     * 
     * @return Message indicating success or conflict.
     */
    public String addSession(Seance session) {
        List<String> conflicts = detectConflicts(session);
        if (!conflicts.isEmpty()) {
            return "CONFLICT DETECTED:\n" + String.join("\n", conflicts);
        }
        repository.save(session);
        return "Session added successfully.";
    }

    /**
     * Detects all potential conflicts for a given session.
     */
    public List<String> detectConflicts(Seance newSession) {
        List<String> conflicts = new ArrayList<>();
        for (Seance existing : repository.findAll()) {
            if (!existing.getId().equals(newSession.getId()) && existing.chevauche(newSession)) {
                // Simplified conflict detection
                if (existing.getSalle().getCode().equals(newSession.getSalle().getCode())) {
                    conflicts.add("Room conflict: " + newSession.getSalle().getCode());
                }
                if (existing.getEnseignant().getId().equals(newSession.getEnseignant().getId())) {
                    conflicts.add("Teacher conflict: " + newSession.getEnseignant().getNom());
                }
            }
        }
        return conflicts;
    }

    public void displaySchedule() {
        List<Seance> sessions = repository.findAll();
        if (sessions.isEmpty()) {
            System.out.println("No sessions scheduled.");
            return;
        }
        sessions.stream()
                .sorted((a, b) -> a.getDebut().compareTo(b.getDebut()))
                .forEach(System.out::println);
    }
}
