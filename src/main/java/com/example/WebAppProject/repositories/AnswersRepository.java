package com.example.WebAppProject.repositories;

import com.example.WebAppProject.models.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Encapsulates the mechanism for search and retrieval operations on objects of Answer class
 *
 * @author Daryna Frolova
 */

public interface AnswersRepository extends JpaRepository<Answer, Integer> {
    Optional<Answer> findById(Integer id);

    Optional<Answer> findByContent(String content);
}
