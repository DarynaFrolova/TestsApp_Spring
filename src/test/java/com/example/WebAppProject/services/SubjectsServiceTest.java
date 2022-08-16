package com.example.WebAppProject.services;

import com.example.WebAppProject.models.Subject;
import com.example.WebAppProject.repositories.SubjectsRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SubjectsServiceTest {

    @Mock
    private SubjectsRepository subjectsRepository;

    @InjectMocks
    private SubjectsService subjectsService;

    @Test
    void findAll() {
        Subject subject = new Subject();
        when(subjectsRepository.findAll()).thenReturn(Collections.singletonList(subject));
    }

    @Test
    void save() {
        Subject subject = new Subject();
        subjectsService.save(subject);
        Mockito.verify(subjectsRepository, times(1)).save(any());
    }

    @Test
    void getSubjectByName() {
        Subject subject = new Subject();
        String name = "Grammar";
        subject.setName(name);
        when(subjectsRepository.findByName(name)).thenReturn(Optional.of(subject));
    }

    @Test
    void getSubjectById() {
        Subject subject = new Subject();
        int id = 1;
        subject.setId(id);
        when(subjectsRepository.findById(id)).thenReturn(Optional.of(subject));
    }
}