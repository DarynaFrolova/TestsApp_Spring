package com.example.WebAppProject.services;

import com.example.WebAppProject.models.Difficulty;
import com.example.WebAppProject.repositories.AnswersRepository;
import com.example.WebAppProject.repositories.DifficultiesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DifficultiesServiceTest {

    @Mock
    private DifficultiesRepository difficultiesRepository;

    @InjectMocks
    private DifficultiesService difficultiesService;

    @Test
    void findAll() {
        Difficulty difficulty = new Difficulty();
        when(difficultiesService.findAll()).thenReturn(Collections.singletonList(difficulty));
    }

    @Test
    void findDifficultyLevelByName() {
        Difficulty difficulty = new Difficulty();
        String name = "Easy";
        difficulty.setLevel(name);
        when(difficultiesService.findDifficultyLevelByName(name)).thenReturn(difficulty);
    }
}