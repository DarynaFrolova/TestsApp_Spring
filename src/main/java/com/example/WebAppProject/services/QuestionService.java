package com.example.WebAppProject.services;

import com.example.WebAppProject.models.Question;
import com.example.WebAppProject.repositories.QuestionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Contains business logic to retrieve, store and delete an object of QuestionsDto class
 *
 * @author Daryna Frolova
 */

@Service
@Transactional
public class QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        super();
        this.questionRepository = questionRepository;
    }

    public void deleteQuestion(Question question) {
        questionRepository.delete(question);
    }

    public List<Question> findAllQuestionsByTestId(Integer testId) {
        return questionRepository.findByTestId(testId);
    }

    public void saveQuestion(Question question) {
        questionRepository.save(question);
    }

    public Question findQuestionById(Integer id) {
        return questionRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    public void deleteQuestionById(Integer id) {
        questionRepository.deleteById(id);
    }
}
