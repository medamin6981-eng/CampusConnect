package com.enspd.campusconnect.service;

import com.enspd.campusconnect.model.Cours;
import com.enspd.campusconnect.repository.CoursRepository;

import java.util.List;

/**
 * Service for managing courses.
 */
public class CourseService {

    private final CoursRepository repository;

    public CourseService(CoursRepository repository) {
        this.repository = repository;
    }

    public List<Cours> listAll() {
        return repository.findAll();
    }

    public void save(Cours cours) {
        repository.save(cours);
    }

    public Cours getByCode(String code) {
        return repository.findById(code).orElse(null);
    }
}
