package com.example.WebAppProject.controllers;

import com.example.WebAppProject.models.Account;
import com.example.WebAppProject.models.TestCase;
import com.example.WebAppProject.models.User;
import com.example.WebAppProject.services.AccountsService;
import com.example.WebAppProject.services.TestCaseService;
import com.example.WebAppProject.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Processes all users-related requests sent by admins and returns corresponding views.
 *
 * @author Daryna Frolova
 */

@Controller
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class);

    private final UserService userService;
    private final AccountsService accountsService;
    private final TestCaseService testCaseService;

    @Autowired
    public UserController(UserService userService, AccountsService accountsService, TestCaseService testCaseService) {
        this.userService = userService;
        this.accountsService = accountsService;
        this.testCaseService = testCaseService;
    }

    @GetMapping("/student/account/{id}")
    public String showToStudentAccountInfo(Model model, @PathVariable("id") int id) {
        try {
            showStudentInfo(model, id);
        } catch (NoSuchElementException e) {
            logger.error("Student {} tried to access a page with id of a user that does not exist in database: {}",
                    ContextController.getUser().getUsername(), "/student/account/" + id);
        }
        return "student/account";
    }

    @GetMapping("/admin/student/info/{id}")
    public String showToAdminStudentInfo(Model model, @PathVariable("id") int id) {
        try {
            showStudentInfo(model, id);
        } catch (NoSuchElementException e) {
            logger.error("Admin {} tried to access a page with id of a user that does not exist in database: {}",
                    ContextController.getUser().getUsername(), "/admin/student/info/" + id);
        }
        return "admin/students/info";
    }

    private void showStudentInfo(Model model, int id) {
        User user = userService.findOneById(id);
        Account account = accountsService.findAccountByUserId(id);
        List<TestCase> allTestCasesByAccount = testCaseService.findAllTestCasesByAccount(account);
        model.addAttribute("user", user);
        model.addAttribute("testCases", allTestCasesByAccount);
        int size = 0;
        if (allTestCasesByAccount != null) {
            size = allTestCasesByAccount.size();
        }
        model.addAttribute("size", size);
        ContextController.setStudent(user);
    }

    @PostMapping("/admin/student/{id}/block")
    public String blockStudent(@PathVariable("id") int id) {
        try {
            User user = userService.findOneById(id);
            user.setStatus("blocked");
            userService.save(user);
            logger.trace("Admin {} blocked student with id {}", ContextController.getUser().getUsername(), user.getId());
        } catch (NoSuchElementException e) {
            logger.error("Admin {} tried to access a page with id of a user that does not exist in database: {}",
                    ContextController.getUser().getUsername(), "/admin/student/" + id + "block");
        }
        return "redirect:/admin/student/info/" + ContextController.getStudent().getId();
    }

    @PostMapping("/admin/student/{id}/unblock")
    public String unblockStudent(@PathVariable("id") int id) {
        try {
            User user = userService.findOneById(id);
            user.setStatus("active");
            userService.save(user);
            logger.trace("Admin {} unblocked student with id {}", ContextController.getUser().getUsername(), user.getId());
        } catch (NoSuchElementException e) {
            logger.error("Admin {} tried to access a page with id of a user that does not exist in database: {}",
                    ContextController.getUser().getUsername(), "/admin/student/" + id + "unblock");
        }
        return "redirect:/admin/student/info/" + ContextController.getStudent().getId();
    }
}