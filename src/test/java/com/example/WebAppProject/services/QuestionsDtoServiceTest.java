package com.example.WebAppProject.services;

import com.example.WebAppProject.models.Question;
import com.example.WebAppProject.repositories.QuestionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class QuestionsDtoServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QuestionsDtoService questionsDtoService;

    @Test
    void findByContent() {
        Question question = new Question();
        question.setContent("Content");
        when(questionsDtoService.findByContent("Content")).thenReturn(question);
    }
}