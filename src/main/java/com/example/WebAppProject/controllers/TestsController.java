package com.example.WebAppProject.controllers;

import com.example.WebAppProject.dto.QuestionsDTO;
import com.example.WebAppProject.dto.TestsDTO;
import com.example.WebAppProject.models.*;
import com.example.WebAppProject.security.UserDetailsImpl;
import com.example.WebAppProject.services.*;
import com.example.WebAppProject.util.QuestionsDtoValidator;
import com.example.WebAppProject.util.TestValidator;
import com.example.WebAppProject.util.TestsDtoValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Processes all tests-related requests and returns corresponding views.
 *
 * @author Daryna Frolova
 */

@Controller
public class TestsController {

    private static final Logger logger = LogManager.getLogger(TestsController.class);

    private final TestsService testsService;
    private final AccountsService accountsService;
    private final TestCaseService testCaseService;
    private final QuestionService questionService;
    private final AnswersService answersService;
    private final SubjectsService subjectsService;
    private final DifficultiesService difficultiesService;
    private final TestsDtoValidator testsDtoValidator;
    private final TestValidator testValidator;
    private final QuestionsDtoValidator questionsDtoValidator;

    private final static SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    @Autowired
    public TestsController
            (TestsService testsService, AccountsService accountsService, TestCaseService testCaseService,
             QuestionService questionService, AnswersService answersService, SubjectsService subjectsService,
             DifficultiesService difficultiesService, TestsDtoValidator testsDtoValidator, TestValidator testValidator,
             QuestionsDtoValidator questionsDtoValidator) {
        this.testsService = testsService;
        this.accountsService = accountsService;
        this.testCaseService = testCaseService;
        this.questionService = questionService;
        this.answersService = answersService;
        this.subjectsService = subjectsService;
        this.difficultiesService = difficultiesService;
        this.testsDtoValidator = testsDtoValidator;
        this.testValidator = testValidator;
        this.questionsDtoValidator = questionsDtoValidator;
    }

    /**
     * Creates an instance of TestCase and initializes its 3 fields:
     * 1) testC - the test itself;
     * 2) account - account of the user that passes this test;
     * 3) startTime - time, at which the user started the test.
     * The logic of timer is implemented using jQuery (see templates/student/tests/run_test.html)
     */
    @GetMapping("/student/tests/{id}")
    public String startTesting(Model model, @PathVariable("id") int id) {
        try {
            model.addAttribute("user", ContextController.getUser());

            TestCase testCase = new TestCase();
            Test test = testsService.findOneById(id);
            testCase.setTestC(test);

            logger.trace("Student with id {} started test {}", ContextController.getUser().getId(), test.getName());

            testCase.setAccount(accountsService.findAccountByUserId(ContextController.getUser().getId()));

            long start = System.currentTimeMillis();
            Date resultDate = new Date(start);
            String startTimeString = sdf.format(resultDate);
            testCase.setStartTime(startTimeString);

            ContextController.setTest(test);

            model.addAttribute("test", test);
            model.addAttribute("testCase", testCase);
            model.addAttribute("questions", test.getQuestions());

            logger.trace("Student with id {} finished test {}", ContextController.getUser().getId(), test.getName());
        } catch (NoSuchElementException e) {
            logger.error("Student {} tried to access a page with id of a test that does not exist in database: {}",
                    ContextController.getUser().getUsername(), "/student/tests/" + id);
        }

        return "student/tests/run_test";
    }

    /**
     * Calculates result of the test based on number of correctly answered questions.
     * Initializes 3 remaining fields of TestCase instance:
     * 1) endTime - time, at which the user started the test;
     * 2) score - number of correctly answered questions;
     * 3) result - result of the test is the percentage of questions that the student answered correctly in relation to
     * the total number of questions (it is considered that the student answered the question correctly if his answer
     * coincides exactly with the correct answers).
     */
    @PostMapping("/tests/{id}")
    public String saveResults(Model model, @PathVariable("id") int id,
                              @ModelAttribute TestCase testCase) {

        long end = System.currentTimeMillis();
        Date resultDate = new Date(end);
        String endTimeString = sdf.format(resultDate);

        Integer numberOfQueries = testsService.findOneById(id).getNumberOfQueries();
        Integer newNumber = numberOfQueries + 1;
        testsService.updateNumberOfQueries(id, newNumber);

        testCase.setEndTime(endTimeString);

        List<Answer> submittedAnswers = testCase.getAnswers();

        List<Question> questions = ContextController.getTest().getQuestions();

        int score = 0;
        int countOfAnswersToOneQuestion = 0;
        boolean correctAnswerPresent = false;

        for (Question question : questions) {
            if (question.getQuestionType().getId() == 1) {
                for (Answer submittedAnswer : submittedAnswers) {
                    if (submittedAnswer.getQuestion().getId() == question.getId()) {
                        countOfAnswersToOneQuestion++;
                        if (submittedAnswer.getIsCorrect() == 1) {
                            correctAnswerPresent = true;
                        }
                    }
                }
                if (countOfAnswersToOneQuestion == 1 && correctAnswerPresent)
                    score++;
                countOfAnswersToOneQuestion = 0;
                correctAnswerPresent = false;
            } else {
                List<Answer> correctAnswersInMultipleChoiceQuestions = new ArrayList<>();
                for (Answer answer : question.getAnswers()) {
                    if (answer.getIsCorrect() == 1)
                        correctAnswersInMultipleChoiceQuestions.add(answer);
                }
                for (Answer correctAnswer : correctAnswersInMultipleChoiceQuestions) {
                    for (Answer submittedAnswer : submittedAnswers) {
                        if (correctAnswer.getId() == submittedAnswer.getId())
                            countOfAnswersToOneQuestion++;
                    }
                }
                if (correctAnswersInMultipleChoiceQuestions.size() == countOfAnswersToOneQuestion)
                    score++;
                countOfAnswersToOneQuestion = 0;
                submittedAnswers.removeAll(correctAnswersInMultipleChoiceQuestions);
            }
        }

        testCase.setScore(score);
        testCase.setTestC(testsService.findOneById(id));

        double result = (double) score / questions.size() * 100;

        testCase.setResult(result);

        testCaseService.saveTestCase(testCase);

        ContextController.setTestCase(testCase);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        model.addAttribute("user", user);

        logger.trace("Results of test {} have been shown to student {}: {}",
                ContextController.getUser().getUsername(),
                ContextController.getTestCase().getTestC().getName(),
                ContextController.getTestCase().getResult());

        return "redirect:/student/tests/results";
    }

    @GetMapping("student/tests/results")
    public String showResults(Model model) {
        model.addAttribute("user", ContextController.getUser());
        model.addAttribute("testCase", ContextController.getTestCase());
        return "student/tests/results";
    }

    /**
     * Redirects user to the page saying that time assigned to the test has run out.
     * Creates an instance of TestCase and initializes all its fields.
     */
    @GetMapping("/student/tests/time_ran_out")
    public String saveTimeRanOut(Model model) {
        model.addAttribute("user", ContextController.getUser());
        logger.trace("Time ran out. Student did not finish the test in time.");
        TestCase testCase = new TestCase();

        long start = System.currentTimeMillis();
        int timeToPassInMinutes = ContextController.getTest().getTimeToPassInMinutes();
        Date resultDate = new Date(start - (long) timeToPassInMinutes * 60 * 1000);
        String startTimeString = sdf.format(resultDate);
        testCase.setAccount(accountsService.findAccountByUserId(ContextController.getUser().getId()));
        testCase.setTestC(ContextController.getTest());
        testCase.setStartTime(startTimeString);
        testCase.setEndTime("Test ended automatically when time ran out.");
        testCase.setResult(0);
        testCase.setScore(0);
        testCaseService.saveTestCase(testCase);

        return "redirect:/student/tests/time_ran_out/page";
    }

    @GetMapping("/student/tests/time_ran_out/page")
    public String showTimeRanOut(Model model) {
        model.addAttribute("user", ContextController.getUser());
        return "student/tests/time_ran_out";
    }

    @GetMapping("/admin/tests/archived/{id}")
    public String showOneArchivedTest(Model model, @PathVariable("id") int id) {
        try {
            model.addAttribute("user", ContextController.getUser());
            model.addAttribute("questions", testsService.findOneById(id).getQuestions());
            model.addAttribute("test", testsService.findOneById(id));
        } catch (NoSuchElementException e) {
            logger.error("Admin {} tried to access a page with id of a test that does not exist in database: {}",
                    ContextController.getUser().getUsername(), "/admin/tests/archived/" + id);
        }
        return "admin/tests/test_archived";
    }

    @GetMapping("/admin/tests/{id}")
    public String showOneTest(Model model, @PathVariable("id") int id) {
        try {
            model.addAttribute("user", ContextController.getUser());
            model.addAttribute("questions", testsService.findOneById(id).getQuestions());
            model.addAttribute("test", testsService.findOneById(id));
            ContextController.setTest(testsService.findOneById(id));
        } catch (NoSuchElementException e) {
            logger.error("Admin {} tried to access a page with id of a test that does not exist in database: {}",
                    ContextController.getUser().getUsername(), "/admin/tests/" + id);
        }
        return "admin/tests/test";
    }

    @GetMapping("/admin/tests/test/{id}/edit/title")
    public String showTestTitleEditForm(@PathVariable Integer id, Model model) {
        try {
            model.addAttribute("user", ContextController.getUser());
            Test test = testsService.findOneById(id);
            model.addAttribute("test", test);
        } catch (NoSuchElementException e) {
            logger.error("Admin {} tried to access a page with id of a test that does not exist in database: {}",
                    ContextController.getUser().getUsername(), "/admin/tests/test/" + id + "/edit/title");
        }
        return "admin/tests/edit_test_title";
    }

    @PostMapping("/admin/tests/test/{id}/edit/title")
    public String editTestTitle(@PathVariable Integer id,
                                @ModelAttribute("test") @Valid Test test, BindingResult bindingResult,
                                Model model) {

        model.addAttribute("user", ContextController.getUser());

        testValidator.validate(test, bindingResult);
        if (bindingResult.hasFieldErrors("name")) {
            logger.trace("Admin {} tried to edit title of the test {}, but failed due to validation restrictions",
                    ContextController.getUser().getUsername(), test.getName());
            return "admin/tests/edit_test_title";
        }

        Test testFromDB = testsService.findOneById(id);
        testFromDB.setName(test.getName());
        testsService.save(testFromDB);

        logger.trace("Admin {} has successfully edited title of the test", ContextController.getUser().getUsername());

        return "redirect:/admin/tests/test-title-edit-success";
    }

    @GetMapping("/admin/tests/test-title-edit-success")
    public String showEditTitleSuccessPage(Model model) {
        model.addAttribute("user", ContextController.getUser());
        model.addAttribute("test", ContextController.getTest());
        return "admin/tests/test-title-edit-success";
    }

    @GetMapping("/admin/tests/test/{id}/edit/time")
    public String showTestTimeEditForm(@PathVariable Integer id, Model model) {
        try {
            model.addAttribute("user", ContextController.getUser());
            Test test = testsService.findOneById(id);
            model.addAttribute("test", test);
        } catch (NoSuchElementException e) {
            logger.error("Admin {} tried to access a page with id of a test that does not exist in database: {}",
                    ContextController.getUser().getUsername(), "/admin/tests/test/" + id + "/edit/time");
        }
        return "admin/tests/edit_test_time";
    }

    @PostMapping("/admin/tests/test/{id}/edit/time")
    public String editTestTime(@PathVariable Integer id,
                               @ModelAttribute("test") @Valid Test test, BindingResult bindingResult,
                               Model model) {
        model.addAttribute("user", ContextController.getUser());

        testValidator.validate(test, bindingResult);
        if (bindingResult.hasFieldErrors("timeToPassInMinutes")) {
            logger.trace("Admin {} tried to edit time assigned to the test {}, but failed due to validation restrictions",
                    ContextController.getUser().getUsername(), test.getName());
            return "admin/tests/edit_test_time";
        }

        Test testFromDB = testsService.findOneById(id);
        testFromDB.setTimeToPassInMinutes(test.getTimeToPassInMinutes());
        testsService.save(testFromDB);

        logger.trace("Admin {} has successfully edited time assigned to the test {}",
                ContextController.getUser().getUsername(), ContextController.getTest().getName());
        return "redirect:/admin/tests/test-time-edit-success";
    }

    @GetMapping("/admin/tests/test-time-edit-success")
    public String showEditTimeSuccessPage(Model model) {
        model.addAttribute("user", ContextController.getUser());
        model.addAttribute("test", ContextController.getTest());
        return "admin/tests/test-time-edit-success";
    }

    /**
     * Shows to admin "Edit question" page.
     * Creates an instance of QuestionsDTO - temporary object used to transfer String values received from Thymeleaf
     * to Answer values of the specific question.
     */
    @GetMapping("/admin/tests/question/edit/{id}")
    public String showEditQuestionForm(@PathVariable Integer id, Model model) {
        try {
            model.addAttribute("user", ContextController.getUser());
            Question existingQuestion = questionService.findQuestionById(id);

            QuestionsDTO questionsDto = new QuestionsDTO();
            questionsDto.setQuestionTitle(existingQuestion.getContent());

            questionsDto.setQuestionType(existingQuestion.getQuestionType().getName());

            List<Answer> options = existingQuestion.getAnswers();
            questionsDto.setOptionOne(options.get(0).getContent());
            questionsDto.setOptionTwo(options.get(1).getContent());
            questionsDto.setOptionThree(options.get(2).getContent());
            if (existingQuestion.getAnswers().size() == 4) {
                questionsDto.setOptionFour(options.get(3).getContent());
            }

            if (existingQuestion.getQuestionType().getId() == 1) {
                String content = answersService.findAnswerById(Integer.parseInt(existingQuestion.getCorrectAnswerId())).getContent();
                questionsDto.setCorrectAnswerOne(content);
            } else {
                String[] ids = existingQuestion.getCorrectAnswerId().split(",");
                Answer answer1 = answersService.findAnswerById(Integer.parseInt(ids[0]));
                questionsDto.setCorrectAnswerOne(answer1.getContent());
                Answer answer2 = answersService.findAnswerById(Integer.parseInt(ids[1]));
                questionsDto.setCorrectAnswerTwo(answer2.getContent());
                if (ids.length == 3) {
                    Answer answer3 = answersService.findAnswerById(Integer.parseInt(ids[2]));
                    questionsDto.setCorrectAnswerThree(answer3.getContent());
                }
                if (ids.length == 4) {
                    Answer answer4 = answersService.findAnswerById(Integer.parseInt(ids[3]));
                    questionsDto.setCorrectAnswerFour(answer4.getContent());
                }
            }

            questionsDto.setQuestionId(existingQuestion.getId());

            model.addAttribute("questionsDto", questionsDto);
            model.addAttribute("question", existingQuestion);
            model.addAttribute("numberOfAnswers", existingQuestion.getAnswers().size());
        } catch (NoSuchElementException e) {
            logger.error("Admin {} tried to access a page with id of a question that does not exist in database: {}",
                    ContextController.getUser().getUsername(), "/admin/tests/question/edit/" + id);
        }
        return "admin/tests/edit_question";
    }

    /**
     * Updates question based on values entered by user and saved as fields of a temporary QuestionsDTO object.
     */
    @PostMapping("/admin/tests/question/edit/{id}")
    public String updateQuestion(@PathVariable Integer id,
                                 @ModelAttribute("questionsDto") @Valid QuestionsDTO questionsDto, BindingResult bindingResult,
                                 Model model) {
        model.addAttribute("user", ContextController.getUser());

        Question existingQuestion = questionService.findQuestionById(id);

        if (bindingResult.hasErrors()) {
            model.addAttribute("question", existingQuestion);
            model.addAttribute("numberOfAnswers", existingQuestion.getAnswers().size());
            logger.trace("Admin {} tried to update question with id {}, but failed due to validation restrictions",
                    ContextController.getUser().getUsername(), existingQuestion.getId());
            return "admin/tests/edit_question";
        }

        List<Answer> existingOptions = existingQuestion.getAnswers();

        existingQuestion.setContent(questionsDto.getQuestionTitle());

        existingOptions.get(0).setContent(questionsDto.getOptionOne());
        existingOptions.get(1).setContent(questionsDto.getOptionTwo());
        existingOptions.get(2).setContent(questionsDto.getOptionThree());
        if (existingOptions.size() == 4) {
            existingOptions.get(3).setContent(questionsDto.getOptionFour());
        }

        Answer correctAnswer1 = answersService.getAnswerByContent(questionsDto.getOptionOne());
        Answer correctAnswer2 = answersService.getAnswerByContent(questionsDto.getOptionTwo());
        Answer correctAnswer3 = answersService.getAnswerByContent(questionsDto.getOptionThree());
        Answer correctAnswer4 = answersService.getAnswerByContent(questionsDto.getOptionFour());

        for (Answer existingOption : existingOptions) {
            existingOption.setIsCorrect(0);
        }

        setCorrectAnswersOfUpdatedQuestion(questionsDto.getCorrectAnswerOne(),
                correctAnswer1, correctAnswer2, correctAnswer3, correctAnswer4, existingQuestion);

        existingQuestion.setQuestionType(new QuestionType(1));

        setCorrectAnswersOfUpdatedQuestion(questionsDto.getCorrectAnswerTwo(),
                correctAnswer1, correctAnswer2, correctAnswer3, correctAnswer4, existingQuestion);

        setCorrectAnswersOfUpdatedQuestion(questionsDto.getCorrectAnswerThree(),
                correctAnswer1, correctAnswer2, correctAnswer3, correctAnswer4, existingQuestion);

        if (existingQuestion.getAnswers().size() == 4) {
            setCorrectAnswersOfUpdatedQuestion(questionsDto.getCorrectAnswerFour(),
                    correctAnswer1, correctAnswer2, correctAnswer3, correctAnswer4, existingQuestion);
        }

        questionService.saveQuestion(existingQuestion);

        for (Answer answer : existingQuestion.getAnswers()) {
            if (answer.getContent().isEmpty() || answer.getContent().isBlank()) {
                answersService.delete(answer);
            }
        }

        logger.trace("Admin {} has successfully updated question with id {}",
                ContextController.getUser().getUsername(), existingQuestion.getId());
        return "redirect:/admin/tests/question-edit-success";
    }

    private void setCorrectAnswersOfUpdatedQuestion(String correctAnswer, Answer correctAnswer1, Answer correctAnswer2,
                                                    Answer correctAnswer3, Answer correctAnswer4, Question existingQuestion) {
        switch (correctAnswer) {
            case "1" -> {
                assert correctAnswer1 != null;
                existingQuestion.setCorrectAnswerId(String.valueOf(correctAnswer1.getId()));
                Answer answer1 = answersService.findAnswerById(correctAnswer1.getId());
                answer1.setIsCorrect(1);
                answersService.save(answer1);
            }
            case "2" -> {
                assert correctAnswer2 != null;
                existingQuestion.setCorrectAnswerId(String.valueOf(correctAnswer2.getId()));
                Answer answer2 = answersService.findAnswerById(correctAnswer2.getId());
                answer2.setIsCorrect(1);
                answersService.save(answer2);
            }
            case "3" -> {
                assert correctAnswer3 != null;
                existingQuestion.setCorrectAnswerId(String.valueOf(correctAnswer3.getId()));
                Answer answer3 = answersService.findAnswerById(correctAnswer3.getId());
                answer3.setIsCorrect(1);
                answersService.save(answer3);
            }
            case "4" -> {
                assert correctAnswer4 != null;
                existingQuestion.setCorrectAnswerId(String.valueOf(correctAnswer4.getId()));
                Answer answer4 = answersService.findAnswerById(correctAnswer4.getId());
                answer4.setIsCorrect(1);
                answersService.save(answer4);
            }
        }
    }

    @GetMapping("/admin/tests/question-edit-success")
    public String showEditQuestionSuccessPage(Model model) {
        model.addAttribute("user", ContextController.getUser());
        model.addAttribute("test", ContextController.getTest());
        return "admin/tests/question-edit-success";
    }

    /**
     * Shows to admin "Add question" page.
     * Creates an instance of QuestionsDTO - temporary object used to transfer String values received from Thymeleaf
     * to Answer values of the new question.
     */
    @GetMapping("/admin/tests/test/{id}/question/add")
    public String showAddQuestionForm(@PathVariable Integer id, Model model) {
        try {
            model.addAttribute("user", ContextController.getUser());

            QuestionsDTO questionsDTO = new QuestionsDTO();
            questionsDTO.setTestTitle(testsService.findOneById(id).getName());
            model.addAttribute("test", testsService.findOneById(id));
            model.addAttribute("questionsDto", questionsDTO);
        } catch (NoSuchElementException e) {
            logger.error("Admin {} tried to access a page with id of a test that does not exist in database: {}",
                    ContextController.getUser().getUsername(), "/admin/tests/test/" + id + "/question/add");
        }
        return "admin/tests/add_question";
    }

    /**
     * Creates new question and initializes all its fields based on values entered by user and saved
     * as fields of a temporary QuestionsDTO object.
     */
    @PostMapping("/admin/tests/test/{id}/question/add")
    public String addQuestion(@PathVariable Integer id,
                              @ModelAttribute("questionsDto") @Valid QuestionsDTO questionsDto,
                              BindingResult bindingResult, Model model) {
        model.addAttribute("user", ContextController.getUser());

        questionsDtoValidator.validate(questionsDto, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("test", testsService.findOneById(id));
            logger.trace("Admin {} tried to add new question to test with id {}, but failed due to validation restrictions",
                    ContextController.getUser().getUsername(), testsService.findOneById(id).getId());
            return "admin/tests/add_question";
        }

        Question question = new Question();

        Test test = testsService.findOneByName(questionsDto.getTestTitle());
        question.setTest(test);

        List<Answer> options = new ArrayList<>();
        Answer optionOne = new Answer(questionsDto.getOptionOne());
        Answer optionTwo = new Answer(questionsDto.getOptionTwo());
        Answer optionThree = new Answer(questionsDto.getOptionThree());
        Answer optionFour = new Answer(questionsDto.getOptionFour());
        options.add(optionOne);
        options.add(optionTwo);
        options.add(optionThree);
        options.add(optionFour);

        question.setQuestionType(new QuestionType(1));
        question.setContent(questionsDto.getQuestionTitle());
        questionService.saveQuestion(question);

        for (Answer option : options) {
            if (!option.getContent().equals("")) {
                option.setQuestion(question);
                answersService.save(option);
            }
        }

        Answer correctAnswer1 = answersService.getAnswerByContent(questionsDto.getOptionOne());
        Answer correctAnswer2 = answersService.getAnswerByContent(questionsDto.getOptionTwo());
        Answer correctAnswer3 = answersService.getAnswerByContent(questionsDto.getOptionThree());
        Answer correctAnswer4 = answersService.getAnswerByContent(questionsDto.getOptionFour());

        setCorrectAnswersOfNewQuestion(questionsDto.getCorrectAnswerOne(),
                correctAnswer1, correctAnswer2, correctAnswer3, correctAnswer4, question);

        setCorrectAnswersOfNewQuestion(questionsDto.getCorrectAnswerTwo(),
                correctAnswer1, correctAnswer2, correctAnswer3, correctAnswer4, question);

        setCorrectAnswersOfNewQuestion(questionsDto.getCorrectAnswerThree(),
                correctAnswer1, correctAnswer2, correctAnswer3, correctAnswer4, question);

        setCorrectAnswersOfNewQuestion(questionsDto.getCorrectAnswerFour(),
                correctAnswer1, correctAnswer2, correctAnswer3, correctAnswer4, question);

        question.setAnswers(options);

        questionService.saveQuestion(question);

        for (Answer option : options) {
            if (option.getContent() != null) {
                option.setQuestion(question);
                answersService.save(option);
            }
        }

        int numberOfQuestions = test.getNumberOfQuestions();
        test.setNumberOfQuestions(++numberOfQuestions);
        testsService.save(test);

        logger.trace("Admin {} has successfully added new question to test with id {}",
                ContextController.getUser().getUsername(), testsService.findOneById(id).getId());
        return "redirect:/admin/tests/question-add-success";
    }

    private void setCorrectAnswersOfNewQuestion(String correctAnswer, Answer correctAnswer1, Answer correctAnswer2,
                                                Answer correctAnswer3, Answer correctAnswer4, Question question) {
        switch (correctAnswer) {
            case "1" -> {
                assert correctAnswer1 != null;
                question.setCorrectAnswerId(String.valueOf(correctAnswer1.getId()));
                correctAnswer1.setIsCorrect(1);
            }
            case "2" -> {
                assert correctAnswer2 != null;
                question.setCorrectAnswerId(String.valueOf(correctAnswer2.getId()));
                correctAnswer2.setIsCorrect(1);
            }
            case "3" -> {
                assert correctAnswer3 != null;
                question.setCorrectAnswerId(String.valueOf(correctAnswer3.getId()));
                correctAnswer3.setIsCorrect(1);
            }
            case "4" -> {
                assert correctAnswer4 != null;
                question.setCorrectAnswerId(String.valueOf(correctAnswer4.getId()));
                correctAnswer4.setIsCorrect(1);
            }
        }
    }

    @GetMapping("/admin/tests/question-add-success")
    public String showAddQuestionSuccessPage(Model model) {
        model.addAttribute("user", ContextController.getUser());
        model.addAttribute("test", ContextController.getTest());
        return "admin/tests/question-add-success";
    }

    /**
     * Shows to admin "Create new test" page.
     * Creates an instance of TestsDTO - temporary object used to transfer String values received from Thymeleaf
     * to corresponding values of the specific test.
     */
    @GetMapping("/admin/subjects/tests/new")
    public String showCreateNewTestForm(Model model) {
        model.addAttribute("user", ContextController.getUser());
        TestsDTO testsDTO = new TestsDTO();
        List<Subject> allSubjects = subjectsService.findAll();
        List<Difficulty> allDifficultyLevels = difficultiesService.findAll();
        model.addAttribute("testsDto", testsDTO);
        model.addAttribute("subjects", allSubjects);
        model.addAttribute("allDifficultyLevels", allDifficultyLevels);
        return "admin/tests/new_test";
    }

    /**
     * Creates new test and initializes all its fields based on values entered by user and saved
     * as fields of a temporary TestsDTO object.
     */
    @PostMapping("/admin/subjects/tests/new")
    public String createNewTest(Model model,
                                @ModelAttribute("testsDto") @Valid TestsDTO testsDTO,
                                BindingResult bindingResult) {
        Test test = new Test();
        test.setName(testsDTO.getName());
        if (subjectsService.getSubjectByName(testsDTO.getSubjectName()).isPresent())
            test.setSubject(subjectsService.getSubjectByName(testsDTO.getSubjectName()).get());
        test.setDifficulty(difficultiesService.findDifficultyLevelByName(testsDTO.getDifficultyLevel()));
        test.setNumberOfQuestions(testsDTO.getNumberOfQuestions());
        test.setTimeToPassInMinutes(testsDTO.getTimeToPassInMinutes());

        testsDtoValidator.validate(testsDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("user", ContextController.getUser());
            model.addAttribute("subjects", subjectsService.findAll());
            model.addAttribute("allDifficultyLevels", difficultiesService.findAll());
            logger.trace("Admin {} tried to create new test, but failed due to validation restrictions",
                    ContextController.getUser().getUsername());
            return "admin/tests/new_test";
        }

        test.setStatus("active");
        test.setNumberOfQueries(0);
        testsService.save(test);

        Subject subject = subjectsService.getSubjectByName(testsDTO.getSubjectName()).get();
        if (subject.getTests() == null) {
            subject.setTests(new ArrayList<>(Collections.singletonList(test)));
        } else {
            subject.getTests().add(test);
        }

        subjectsService.save(subject);

        testsService.save(test);

        ContextController.setTest(test);

        logger.trace("Admin {} created new test: {}",
                ContextController.getUser().getUsername(), test.getName());

        return "redirect:/admin/tests/test-new-success";
    }

    @GetMapping("/admin/tests/test-new-success")
    public String showNewTestSuccessPage(Model model) {
        model.addAttribute("user", ContextController.getUser());
        model.addAttribute("test", ContextController.getTest());
        return "admin/tests/test-new-success";
    }


    @GetMapping("/admin/tests/question/delete/{id}")
    public String showDeleteQuestionForm(@PathVariable Integer id, Model model) {
        try {
            model.addAttribute("user", ContextController.getUser());
            Test test = questionService.findQuestionById(id).getTest();
            model.addAttribute("question", questionService.findQuestionById(id));
            model.addAttribute("test", test);
        } catch (NoSuchElementException e) {
            logger.error("Admin {} tried to access a page with id of a question that does not exist in database: {}",
                    ContextController.getUser().getUsername(), "/admin/tests/question/delete/" + id);
        }
        return "admin/tests/delete_question";
    }

    @PostMapping("/admin/tests/question/delete/{id}")
    public String deleteQuestion(@PathVariable Integer id,
                                 @ModelAttribute QuestionsDTO questionsDto) {

        Test test = questionService.findQuestionById(id).getTest();
        int numberOfQuestions = test.getNumberOfQuestions();
        test.setNumberOfQuestions(--numberOfQuestions);
        testsService.save(test);

        questionService.deleteQuestionById(id);

        logger.trace("Admin {} deleted question with id {}",
                ContextController.getUser().getUsername(), id);

        return "redirect:/admin/tests/question-delete-success";
    }

    @GetMapping("/admin/tests/question-delete-success")
    public String showDeleteQuestionSuccessPage(Model model) {
        model.addAttribute("user", ContextController.getUser());
        model.addAttribute("test", ContextController.getTest());
        return "admin/tests/question-delete-success";
    }

    @GetMapping("/admin/tests/test/{id}/delete")
    public String showDeleteTestForm(@PathVariable Integer id, Model model) {
        try {
            model.addAttribute("user", ContextController.getUser());
            Test test = testsService.findOneById(id);
            model.addAttribute("test", test);
        } catch (NoSuchElementException e) {
            logger.error("Admin {} tried to access a page with id of a test that does not exist in database: {}",
                    ContextController.getUser().getUsername(), "/admin/tests/test/" + id + "/delete");
        }
        return "admin/tests/delete_test";
    }

    @PostMapping("/admin/tests/test/{id}/delete")
    public String deleteTest(@PathVariable Integer id) {

        List<TestCase> allTestCases = testCaseService.findAllTestCases();
        for (TestCase testCase : allTestCases) {
            if (testCase.getTestC().getId() == id) {
                logger.trace("Admin {} tried to delete test with id {}, but was redirected to \"Archive test\" page",
                        ContextController.getUser().getUsername(), id);
                return "redirect:/admin/tests/test-delete-failure";
            }
        }

        List<Question> allQuestionsByTestId = questionService.findAllQuestionsByTestId(id);
        for (Question question : allQuestionsByTestId) {
            questionService.deleteQuestion(question);
        }

        testsService.deleteTestById(id);

        logger.trace("Admin {} successfully deleted test with id {}",
                ContextController.getUser().getUsername(), id);

        return "redirect:/admin/tests/test-delete-success";
    }

    @GetMapping("/admin/tests/test-delete-success")
    public String showDeleteTestSuccessPage(Model model) {
        model.addAttribute("user", ContextController.getUser());
        model.addAttribute("subject", ContextController.getSubject());
        return "admin/tests/test-delete-success";
    }

    @GetMapping("/admin/tests/test-delete-failure")
    public String showDeleteTestFailurePage(Model model) {
        model.addAttribute("user", ContextController.getUser());
        model.addAttribute("test", ContextController.getTest());
        return "admin/tests/test-delete-failure";
    }


    @PostMapping("/admin/tests/test/{id}/archive")
    public String archiveTest(@PathVariable Integer id) {
        Test test = testsService.findOneById(id);
        test.setStatus("archived");
        testsService.save(test);
        logger.trace("Admin {} successfully changed status of test with id {} to \"archived\"",
                ContextController.getUser().getUsername(), id);
        return "redirect:/admin/tests/test-archive-success";
    }

    @GetMapping("/admin/tests/test-archive-success")
    public String showArchiveTestSuccessPage(Model model) {
        model.addAttribute("user", ContextController.getUser());
        return "admin/tests/test-archive-success";
    }

    @PostMapping("/admin/tests/archived/test/{id}/activate")
    public String activateTest(@PathVariable Integer id) {
        Test test = testsService.findOneById(id);
        test.setStatus("active");
        testsService.save(test);
        logger.trace("Admin {} successfully changed status of test with id {} to \"active\"",
                ContextController.getUser().getUsername(), id);
        return "redirect:/admin/tests/test-activate-success";
    }

    @GetMapping("/admin/tests/test-activate-success")
    public String showActivateTestSuccessPage(Model model) {
        model.addAttribute("user", ContextController.getUser());
        return "admin/tests/test-activate-success";
    }
}