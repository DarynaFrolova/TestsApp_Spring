package com.example.WebAppProject.services;

import com.example.WebAppProject.models.Question;
import com.example.WebAppProject.repositories.QuestionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QuestionService questionService;

    @Test
    void deleteQuestion() {
        Question question = new Question();
        questionService.deleteQuestion(question);
        Mockito.verify(questionRepository).delete(question);
    }

    @Test
    void findAllQuestionsByTestId() {
        com.example.WebAppProject.models.Test test = new com.example.WebAppProject.models.Test();
        test.setId(1);
        Question question1 = new Question();
        question1.setTest(test);
        Question question2 = new Question();
        when(questionService.findAllQuestionsByTestId(1)).thenReturn(Collections.singletonList(question1));
    }

    @Test
    void saveQuestion() {
        Question question = new Question();
        questionService.saveQuestion(question);
        Mockito.verify(questionRepository, times(1)).save(any());
    }

    @Test
    void findQuestionById() {
        Question question = new Question();
        int id = 1;
        question.setId(id);
        when(questionService.findQuestionById(id)).thenReturn(question);
    }

    @Test
    void deleteQuestionById() {
        final int id = 1;
        questionService.deleteQuestionById(id);
        Mockito.verify(questionRepository).deleteById(id);
    }
}