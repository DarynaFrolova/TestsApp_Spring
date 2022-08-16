package com.example.WebAppProject.services;

import com.example.WebAppProject.models.Role;
import com.example.WebAppProject.models.User;
import com.example.WebAppProject.repositories.UsersRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Contains business logic to register a user
 *
 * @author Daryna Frolova
 */

@Service
@Transactional
public class RegistrationService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public RegistrationService(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Session session = entityManager.unwrap(Session.class);
        user.setRole(session.get(Role.class, 1));
        user.setStatus("active");
        usersRepository.save(user);
    }
}
