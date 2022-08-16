package com.example.WebAppProject.services;

import com.example.WebAppProject.models.Answer;
import com.example.WebAppProject.repositories.AnswersRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Contains business logic to retrieve, store and delete an object of Answer class
 *
 * @author Daryna Frolova
 */

@Service
@Transactional
public class AnswersService {

    private final AnswersRepository answersRepository;

    public AnswersService(AnswersRepository answersRepository) {
        this.answersRepository = answersRepository;
    }

    public Answer findAnswerById(Integer id) {
        return answersRepository.findById(id).orElse(null);
    }

    public Answer getAnswerByContent(String content) {
        return answersRepository.findByContent(content).orElse(null);
    }

    public void save(Answer answer) {
        answersRepository.save(answer);
    }

    public void delete(Answer answer){
        answersRepository.delete(answer);
    }
}
