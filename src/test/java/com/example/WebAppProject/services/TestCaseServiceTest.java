package com.example.WebAppProject.services;

import com.example.WebAppProject.models.Account;
import com.example.WebAppProject.models.TestCase;
import com.example.WebAppProject.repositories.TestCaseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TestCaseServiceTest {

    @Mock
    private TestCaseRepository testCaseRepository;

    @InjectMocks
    private TestCaseService testCaseService;

    @Test
    void findAllTestCases() {
        TestCase testCase = new TestCase();
        when(testCaseRepository.findAll()).thenReturn(Collections.singletonList(testCase));
    }

    @Test
    void saveTestCase() {
        TestCase testCase = new TestCase();
        testCaseService.saveTestCase(testCase);
        Mockito.verify(testCaseRepository, times(1)).save(any());
    }

    @Test
    void findAllTestCasesByAccount() {
        Account account1 = new Account();
        Account account2 = new Account();
        TestCase testCase1 = new TestCase();
        testCase1.setAccount(account1);
        TestCase testCase2 = new TestCase();
        testCase2.setAccount(account2);
        when(testCaseRepository.findAllByAccount(account1)).thenReturn(Collections.singletonList(testCase1));
    }
}