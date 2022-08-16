package com.example.WebAppProject.controllers;

import com.example.WebAppProject.models.Subject;
import com.example.WebAppProject.models.Test;
import com.example.WebAppProject.services.SubjectsService;
import com.example.WebAppProject.services.TestsService;
import com.example.WebAppProject.util.SubjectValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
public class SubjectController {

    private static final Logger logger = LogManager.getLogger(SubjectController.class);

    private final TestsService testsService;
    private final SubjectsService subjectsService;
    private final SubjectValidator subjectValidator;

    @Autowired
    public SubjectController(TestsService testsService, SubjectsService subjectsService, SubjectValidator subjectValidator) {
        this.testsService = testsService;
        this.subjectsService = subjectsService;
        this.subjectValidator = subjectValidator;
    }

    @GetMapping("/subjects/{id}")
    public String showAllTestsOfSubjectToStudent(Model model, @PathVariable("id") int id) {
        try {
            subjectsService.getSubjectById(id); // needed to catch NoSuchElementException

            List<Test> tests = testsService.findAllTestsBySubjectId(id);
            List<Test> activeTests = new ArrayList<>();
            for (Test test : tests) {
                if (test.getStatus().equals("active")) {
                    activeTests.add(test);
                }
            }
            tests = activeTests;

            ContextController.setTests(tests);
            ContextController.setSubject(subjectsService.getSubjectById(id));

            int size = 0;
            if (tests.size() != 0) {
                size = tests.size();
            }
            model.addAttribute("user", ContextController.getUser());
            model.addAttribute("size", size);
            model.addAttribute("tests", tests);
            model.addAttribute("subjectId", id);
        } catch (NoSuchElementException e) {
            logger.error("User with id {} tried to access a page with id of a user that does not exist in database: {}",
                    ContextController.getUser().getId(), "/subjects/" + id);
        }
        return findPaginated(1, "name", "asc", model);
    }


    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortDir,
                                Model model) {

        model.addAttribute("user", ContextController.getUser());
        model.addAttribute("size", ContextController.getTests().size());

        int pageSize = 10;

        Page<Test> page = testsService.findPaginated(pageNo, pageSize, sortField, sortDir);
        List<Test> tests = page.getContent();
        List<Test> activeTests = new ArrayList<>();
        for (Test test : tests) {
            if (test.getSubject().getId() == ContextController.getSubject().getId()) {
                activeTests.add(test);
            }
        }

        System.out.println(tests.size());
        System.out.println(activeTests.size());

        model.addAttribute("currentPage", pageNo);

        model.addAttribute("totalItems", ContextController.getTests().size());
        model.addAttribute("totalPages", (int) Math.ceil((double) ContextController.getTests().size() / pageSize));

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("tests", activeTests);
        model.addAttribute("subjectId", ContextController.getSubject().getId());

        return "student/tests/tests";
    }

    @GetMapping("admin/subjects/{id}")
    public String showAllTestsOfSubjectToAdmin(Model model, @PathVariable("id") int id) {
        try {
            subjectsService.getSubjectById(id); // needed to catch NoSuchElementException

            List<Test> tests = testsService.findAllTestsBySubjectId(id);
            List<Test> activeTests = new ArrayList<>();
            List<Test> archivedTests = new ArrayList<>();
            for (Test test : tests) {
                if (test.getStatus().equals("active")) {
                    activeTests.add(test);
                } else {
                    archivedTests.add(test);
                }
            }

            int activeSize = 0;
            if (activeTests.size() != 0) {
                activeSize = activeTests.size();
            }

            int archivedSize = 0;
            if (archivedTests.size() != 0) {
                archivedSize = archivedTests.size();
            }

            model.addAttribute("activeTests", activeTests);
            model.addAttribute("archivedTests", archivedTests);
            model.addAttribute("activeSize", activeSize);
            model.addAttribute("archivedSize", archivedSize);
            model.addAttribute("user", ContextController.getUser());
        } catch (NoSuchElementException e) {
            logger.error("Admin {} tried to access a page with id of a user that does not exist in database: {}",
                    ContextController.getUser().getUsername(), "/admin/subjects/" + id);
        }
        return "admin/tests/tests";
    }

    @GetMapping("/subjects/new")
    public String createNewSubject(Model model) {
        Subject subject = new Subject();
        model.addAttribute("subject", subject);
        model.addAttribute("user", ContextController.getUser());
        return "admin/tests/new_subject";
    }

    @PostMapping("/subjects/new_post")
    public String create(@ModelAttribute("subject") @Valid Subject subject,
                         BindingResult bindingResult, Model model) {

        model.addAttribute("user", ContextController.getUser());

        subjectValidator.validate(subject, bindingResult);

        if (bindingResult.hasErrors())
            return "admin/tests/new_subject";

        subjectsService.save(subject);

        logger.trace("New subject crated: {}", subject.getName());

        return "redirect:/main_page";
    }

}
