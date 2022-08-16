package com.example.WebAppProject.repositories;

import com.example.WebAppProject.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Encapsulates the mechanism for search and retrieval operations on objects of Role class
 *
 * @author Daryna Frolova
 */

@Repository
public interface RolesRepository extends JpaRepository<Role, Integer> {
    Role findRoleById(Integer id);
}
