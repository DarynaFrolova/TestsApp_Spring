package com.example.WebAppProject.util;

import com.example.WebAppProject.models.Subject;
import com.example.WebAppProject.services.SubjectsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validates the subject to be added to DB (checks whether a subject with the same title already exists)
 *
 * @author Daryna Frolova
 */

@Component
public class SubjectValidator implements Validator {

    private final SubjectsService subjectsService;

    @Autowired
    public SubjectValidator(SubjectsService subjectsService) {
        this.subjectsService = subjectsService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Subject.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Subject subject = (Subject) o;

        if (subjectsService.getSubjectByName(subject.getName()).isPresent())
            errors.rejectValue("name", "", "Subject with this title already exists. Choose another one.");
    }
}
