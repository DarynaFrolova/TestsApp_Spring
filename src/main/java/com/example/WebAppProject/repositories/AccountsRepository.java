package com.example.WebAppProject.repositories;

import com.example.WebAppProject.models.Account;
import com.example.WebAppProject.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Encapsulates the mechanism for search and retrieval operations on objects of Account class
 *
 * @author Daryna Frolova
 */

@Repository
public interface AccountsRepository extends JpaRepository<Account, Integer> {
    Account findAccountByUser(User user);
}
