package com.example.WebAppProject.util;

import com.example.WebAppProject.models.User;
import com.example.WebAppProject.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validates the user to be added to DB (checks whether a user with the same username already exists)
 *
 * @author Daryna Frolova
 */

@Component
public class UserValidator implements Validator {

    private final UserService userService;

    @Autowired
    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;

        if (userService.getUserByUsername(user.getUsername()).isPresent())
            errors.rejectValue("username", "", "This username already exists. Choose another one.");
    }
}
