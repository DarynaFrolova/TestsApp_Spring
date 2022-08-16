package com.example.WebAppProject.controllers;

import com.example.WebAppProject.models.Subject;
import com.example.WebAppProject.models.Test;
import com.example.WebAppProject.models.User;
import com.example.WebAppProject.security.UserDetailsImpl;
import com.example.WebAppProject.services.SubjectsService;
import com.example.WebAppProject.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Redirects users to main page, which differs depending on their role.
 *
 * @author Daryna Frolova
 */

@Controller
public class DefaultController {

    private final SubjectsService subjectsService;
    private final UserService userService;
    private static int countOfMainPageCalls = 0;

    private static final Logger logger = LogManager.getLogger(DefaultController.class);

    @Autowired
    public DefaultController(SubjectsService subjectsService, UserService userService) {
        this.subjectsService = subjectsService;
        this.userService = userService;
    }

    @RequestMapping("/main_page")
    public String defaultAfterLogin(Model model, HttpServletRequest request) {
        countOfMainPageCalls++;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        model.addAttribute("user", user);
        ContextController.setUser(user);

        if (countOfMainPageCalls == 1)
            logger.trace("Login successful: {}, {}", user.getUsername(), user.getRole());

        List<Subject> subjects = subjectsService.findAll();
        List<Subject> subjectsWithActiveTests = new ArrayList<>();

        for (Subject subject : subjects) {
            List<Test> tests = subject.getTests();
            if (tests != null) {
                for (Test test : tests) {
                    if (test.getStatus().equals("active")) {
                        subjectsWithActiveTests.add(subject);
                        break;
                    }
                }
            }
        }

        model.addAttribute("subjects", subjects);

        model.addAttribute("students", userService.findAllStudents());

        if (request.isUserInRole("ROLE_ADMIN")) {
            return "admin/main";
        }

        model.addAttribute("activeSubjects", subjectsWithActiveTests);
        return "student/main";
    }
}
