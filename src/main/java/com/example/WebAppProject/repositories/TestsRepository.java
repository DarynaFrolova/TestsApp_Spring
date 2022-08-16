package com.example.WebAppProject.repositories;

import com.example.WebAppProject.models.Subject;
import com.example.WebAppProject.models.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Encapsulates the mechanism for search and retrieval operations on objects of Test class
 *
 * @author Daryna Frolova
 */

@Repository
public interface TestsRepository extends JpaRepository<Test, Integer> {
    List<Test> findAllBySubject(Subject subject);
    Test findByName(String name);
}
