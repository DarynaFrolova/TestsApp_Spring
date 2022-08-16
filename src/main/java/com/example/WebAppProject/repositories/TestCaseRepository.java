package com.example.WebAppProject.repositories;

import com.example.WebAppProject.models.Account;
import com.example.WebAppProject.models.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Encapsulates the mechanism for search and retrieval operations on objects of TestCase class
 *
 * @author Daryna Frolova
 */

@Repository
public interface TestCaseRepository extends JpaRepository<TestCase, Long>{
    TestCase findById(Integer id);
    List<TestCase> findAllByAccount(Account account);
}

