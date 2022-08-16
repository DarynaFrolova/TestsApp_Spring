package com.example.WebAppProject.controllers;

import com.example.WebAppProject.models.Account;
import com.example.WebAppProject.models.User;
import com.example.WebAppProject.services.AccountsService;
import com.example.WebAppProject.services.RegistrationService;
import com.example.WebAppProject.util.UserValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

/**
 * Authenticates users via login and registration pages.
 *
 * @author Daryna Frolova
 */

@Controller
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LogManager.getLogger(AuthController.class);

    private final RegistrationService registrationService;
    private final AccountsService accountsService;
    private final UserValidator userValidator;

    @Autowired
    public AuthController(RegistrationService registrationService, AccountsService accountsService, UserValidator userValidator) {
        this.registrationService = registrationService;
        this.accountsService = accountsService;
        this.userValidator = userValidator;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("user") User user) {
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("user") @Valid User user,
                                      BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors())
            return "/auth/registration";

        registrationService.register(user);

        Account account = new Account();
        account.setUser(user);
        accountsService.save(account);

        logger.trace("Registration successful: " + user.getUsername());

        return "redirect:/auth/login";
    }
}
