package com.example.WebAppProject.util;

import com.example.WebAppProject.models.Test;
import com.example.WebAppProject.services.TestsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validates the test to be added to DB (checks whether a test with the same title already exists)
 *
 * @author Daryna Frolova
 */

@Component
public class TestValidator implements Validator {

    private final TestsService testsService;

    @Autowired
    public TestValidator(TestsService testsService) {
        this.testsService = testsService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Test.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Test test = (Test) o;

        if (testsService.findOneByName(test.getName()) != null)
            errors.rejectValue("name", "", "Test with this title already exists. Choose another one.");
    }
}
