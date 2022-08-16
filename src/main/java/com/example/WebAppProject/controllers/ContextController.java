package com.example.WebAppProject.controllers;

import com.example.WebAppProject.models.*;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Keeps in memory all fields that must be referenced in other classes of the project.
 *
 * @author Daryna Frolova
 */

@NoArgsConstructor
public class ContextController {
    public static User user;
    public static TestCase testCase;
    public static Test test;
    public static List<Test> tests;
    public static Subject subject;
    public static User student;

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        ContextController.user = user;
    }

    public static TestCase getTestCase() {
        return testCase;
    }

    public static void setTestCase(TestCase testCase) {
        ContextController.testCase = testCase;
    }

    public static Test getTest() {
        return test;
    }

    public static void setTest(Test test) {
        ContextController.test = test;
    }

    public static List<Test> getTests() {
        return tests;
    }

    public static void setTests(List<Test> tests) {
        ContextController.tests = tests;
    }

    public static Subject getSubject() {
        return subject;
    }

    public static void setSubject(Subject subject) {
        ContextController.subject = subject;
    }

    public static User getStudent() {
        return student;
    }

    public static void setStudent(User student) {
        ContextController.student = student;
    }
}
