package com.example.WebAppProject.services;

import com.example.WebAppProject.models.User;
import com.example.WebAppProject.repositories.UsersRepository;
import com.example.WebAppProject.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Contains business logic to authenticate a user
 *
 * @author Daryna Frolova
 */

@Service
public class UserWebDetailsService implements UserDetailsService {

    private final UsersRepository usersRepository;

    @Autowired
    public UserWebDetailsService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDetailsImpl loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<User> user = usersRepository.findByUsername(s);

        if (user.isEmpty())
            throw new UsernameNotFoundException("User not found");

        return new UserDetailsImpl(user.get());
    }
}
