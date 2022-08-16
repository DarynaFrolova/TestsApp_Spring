package com.example.WebAppProject.util;

import com.example.WebAppProject.dto.TestsDTO;
import com.example.WebAppProject.services.TestsDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validates the TestsDto instance used to create/update a test to be added to DB (checks whether a test with the same title already exists)
 *
 * @author Daryna Frolova
 */

@Component
public class TestsDtoValidator implements Validator {

    private final TestsDtoService testsDtoService;

    @Autowired
    public TestsDtoValidator(TestsDtoService testsDtoService) {
        this.testsDtoService = testsDtoService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return TestsDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        TestsDTO test = (TestsDTO) o;

        if (testsDtoService.findByName(test.getName()) != null)
            errors.rejectValue("name", "", "Test with this title already exists. Choose another one.");
    }
}
