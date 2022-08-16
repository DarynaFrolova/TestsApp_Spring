package com.example.WebAppProject.services;

import com.example.WebAppProject.models.Question;
import com.example.WebAppProject.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Contains business logic to retrieve an object of QuestionsDto class
 *
 * @author Daryna Frolova
 */

@Service
@Transactional
public class QuestionsDtoService {

    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionsDtoService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public Question findByContent(String content) {
        return questionRepository.findByContent(content);
    }
}
