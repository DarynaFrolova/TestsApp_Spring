package com.example.WebAppProject.services;

import com.example.WebAppProject.models.User;
import com.example.WebAppProject.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Contains business logic to store, retrieve, delete and update an object of User class
 *
 * @author Daryna Frolova
 */

@Service
@Transactional
public class UserService {

    private final UsersRepository usersRepository;

    @Autowired
    public UserService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public List<User> findAll() {
        return usersRepository.findAll();
    }

    public List<User> findAllStudents() {
        List<User> allUsers = usersRepository.findAll();
        List<User> students = new ArrayList<>();
        for (User user : allUsers) {
            if (user.getRole().getId() == 1) {
                students.add(user);
            }
        }
        return students;
    }

    public User findOneById(int id) {
        Optional<User> foundUser = usersRepository.findById(id);
        return foundUser.orElseThrow(NoSuchElementException::new);
    }

    public void save(User user) {
        usersRepository.save(user);
    }

    public void delete(int id) {
        usersRepository.deleteById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        return usersRepository.findByUsername(username);
    }
}
