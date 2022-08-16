package com.example.WebAppProject.services;

import com.example.WebAppProject.models.Answer;
import com.example.WebAppProject.models.Question;
import com.example.WebAppProject.repositories.AnswersRepository;
import com.example.WebAppProject.repositories.QuestionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AnswersServiceTest {

    @Mock
    private AnswersRepository answersRepository;

    @InjectMocks
    private AnswersService answersService;

    @Test
    void findAnswerById() {
        Answer answer = new Answer();
        int id = 1;
        answer.setId(id);
        when(answersService.findAnswerById(id)).thenReturn(answer);
    }

    @Test
    void getAnswerByContent() {
        Answer answer = new Answer();
        String content = "Content";
        answer.setContent(content);
        when(answersService.getAnswerByContent(content)).thenReturn(answer);
    }

    @Test
    void save() {
        Answer answer = new Answer();
        answersService.save(answer);
        Mockito.verify(answersRepository, times(1)).save(any());
    }

    @Test
    void delete() {
        Answer answer = new Answer();
        answersService.delete(answer);
        Mockito.verify(answersRepository).delete(answer);
    }
}