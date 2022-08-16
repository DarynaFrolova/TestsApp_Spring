package com.example.WebAppProject.services;

import com.example.WebAppProject.models.Difficulty;
import com.example.WebAppProject.repositories.DifficultiesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Contains business logic to retrieve an object of Difficulty class
 *
 * @author Daryna Frolova
 */

@Service
@Transactional
public class DifficultiesService {

    private final DifficultiesRepository difficultiesRepository;

    public DifficultiesService(DifficultiesRepository difficultiesRepository) {
        this.difficultiesRepository = difficultiesRepository;
    }

    public List<Difficulty> findAll() {
        return difficultiesRepository.findAll();
    }

    public Difficulty findDifficultyLevelByName(String level) {
        return difficultiesRepository.findByLevel(level);
    }
}
