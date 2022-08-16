package com.example.WebAppProject.services;

import com.example.WebAppProject.models.Account;
import com.example.WebAppProject.models.TestCase;
import com.example.WebAppProject.repositories.TestCaseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Contains business logic to retrieve and store an object of TestCase class
 *
 * @author Daryna Frolova
 */

@Service
@Transactional
public class TestCaseService {

    private final TestCaseRepository testCaseRepository;

    public TestCaseService(TestCaseRepository testCaseRepository) {
        this.testCaseRepository = testCaseRepository;
    }

    public List<TestCase> findAllTestCases() {
        return testCaseRepository.findAll();
    }

    public void saveTestCase(TestCase testCase) {
        testCaseRepository.save(testCase);
    }

    public List<TestCase> findAllTestCasesByAccount(Account account) {
        return testCaseRepository.findAllByAccount(account);
    }
}
