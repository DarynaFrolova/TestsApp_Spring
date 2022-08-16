package com.example.WebAppProject.services;

import com.example.WebAppProject.models.Subject;
import com.example.WebAppProject.repositories.SubjectsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Contains business logic to retrieve and store an object of Subject class
 *
 * @author Daryna Frolova
 */

@Service
@Transactional
public class SubjectsService {

    private final SubjectsRepository subjectsRepository;

    @Autowired
    public SubjectsService(SubjectsRepository subjectsRepository) {
        this.subjectsRepository = subjectsRepository;
    }

    public List<Subject> findAll() {
        return subjectsRepository.findAll();
    }

    public void save(Subject subject) {
        subjectsRepository.save(subject);
    }

    public Optional<Subject> getSubjectByName(String name) {
        return subjectsRepository.findByName(name);
    }

    public Subject getSubjectById(int id) {
        return subjectsRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }
}
