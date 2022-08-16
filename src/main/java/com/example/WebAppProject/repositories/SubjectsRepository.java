package com.example.WebAppProject.repositories;

import com.example.WebAppProject.models.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Encapsulates the mechanism for search and retrieval operations on objects of Subject class
 *
 * @author Daryna Frolova
 */

@Repository
public interface SubjectsRepository extends JpaRepository<Subject, Integer> {
    Optional<Subject> findByName(String name);
    Optional<Subject> findById(int id);
}
