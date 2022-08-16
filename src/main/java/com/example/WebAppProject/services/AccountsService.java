package com.example.WebAppProject.services;

import com.example.WebAppProject.models.Account;
import com.example.WebAppProject.models.User;
import com.example.WebAppProject.repositories.AccountsRepository;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Contains business logic to retrieve an object of Account class
 *
 * @author Daryna Frolova
 */

@Service
@Transactional
public class AccountsService {

    @PersistenceContext
    private EntityManager entityManager;

    private final AccountsRepository accountsRepository;

    public AccountsService(AccountsRepository accountsRepository) {
        this.accountsRepository = accountsRepository;
    }

    public Account findAccountByUserId(int id) {
        Session session = entityManager.unwrap(Session.class);
        User user = session.get(User.class, id);
        return accountsRepository.findAccountByUser(user);
    }

    public void save(Account account) {
        accountsRepository.save(account);
    }
}
