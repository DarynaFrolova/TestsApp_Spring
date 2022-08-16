package com.example.WebAppProject.repositories;

import com.example.WebAppProject.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Encapsulates the mechanism for search and retrieval operations on objects of User class
 *
 * @author Daryna Frolova
 */

@Repository
public interface UsersRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
}
