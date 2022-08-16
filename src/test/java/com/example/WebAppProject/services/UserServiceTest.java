package com.example.WebAppProject.services;

import com.example.WebAppProject.models.User;
import com.example.WebAppProject.repositories.RolesRepository;
import com.example.WebAppProject.repositories.UsersRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserServiceTest {

    @Mock
    private UsersRepository usersRepository;
    @Mock
    private RolesRepository rolesRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void testDelete(){
        final int id = 1;
        userService.delete(id);
        Mockito.verify(usersRepository).deleteById(id);
    }

    @Test
    void testSave(){
        User user = new User("Bred");
        userService.save(user);
        Mockito.verify(usersRepository, times(1)).save(any());
    }

    @Test
    void testFindAll() {
        User user = new User("Bred");
        when(usersRepository.findAll()).thenReturn(Collections.singletonList(user));
    }

    @Test
    void testFindAllStudents() {
        User admin = new User("Mary");
        admin.setRole(rolesRepository.findRoleById(1));
        User student = new User("Bob");
        student.setRole(rolesRepository.findRoleById(2));
        when(userService.findAllStudents()).thenReturn(Collections.singletonList(student));
    }

    @Test
    void testFindOneById() {
        User user = new User("Bred");
        int id = 1;
        user.setId(id);
        when(usersRepository.findById(id)).thenReturn(Optional.of(user));
    }

    @Test
    void testGetUserByUsername() {
        String username = "Bred";
        User user = new User(username);
        when(usersRepository.findByUsername(username)).thenReturn(Optional.of(user));
    }
}