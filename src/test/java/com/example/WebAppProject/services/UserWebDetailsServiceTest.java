package com.example.WebAppProject.services;

import com.example.WebAppProject.repositories.UsersRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserWebDetailsServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @InjectMocks
    private UserWebDetailsService userWebDetailsService;

    @Test
    void loadUserByUsername_throwsException_whenNoUser() {
        Assertions.assertThrows(UsernameNotFoundException.class, () -> userWebDetailsService.loadUserByUsername("Empty"));
    }
}