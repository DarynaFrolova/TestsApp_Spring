package com.example.WebAppProject.repositories;

import com.example.WebAppProject.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Encapsulates the mechanism for search, retrieval and delete operations on objects of Difficulty class
 *
 * @author Daryna Frolova
 */

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    List<Question> findByTestId(Integer id);
    Optional<Question> findById(Integer id);
    Question findByContent(String content);
    void deleteById(Integer id);
}
