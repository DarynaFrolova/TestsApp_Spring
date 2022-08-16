package com.example.WebAppProject.repositories;

import com.example.WebAppProject.models.Difficulty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Encapsulates the mechanism for search and retrieval operations on objects of Difficulty class
 *
 * @author Daryna Frolova
 */

@Repository
public interface DifficultiesRepository extends JpaRepository<Difficulty, Integer> {
    Difficulty findByLevel(String level);
}
