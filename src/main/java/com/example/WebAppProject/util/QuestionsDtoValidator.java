package com.example.WebAppProject.util;

import com.example.WebAppProject.dto.QuestionsDTO;
import com.example.WebAppProject.services.QuestionsDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validates the question to be added to DB (checks whether a question with the same title already exists)
 *
 * @author Daryna Frolova
 */

@Component
public class QuestionsDtoValidator implements Validator {

    private final QuestionsDtoService questionsDtoService;

    @Autowired
    public QuestionsDtoValidator( QuestionsDtoService questionsDtoService) {
        this.questionsDtoService = questionsDtoService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return QuestionsDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        QuestionsDTO questionsDTO = (QuestionsDTO) o;

        if (questionsDtoService.findByContent(questionsDTO.getQuestionTitle()) != null)
            errors.rejectValue("questionTitle", "", "Question with this title already exists. Choose another one.");
    }
}
