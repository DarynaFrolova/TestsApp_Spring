package com.example.WebAppProject.controllers;

import com.example.WebAppProject.models.Account;
import com.example.WebAppProject.models.Question;
import com.example.WebAppProject.models.User;
import com.example.WebAppProject.services.*;
import com.example.WebAppProject.util.QuestionsDtoValidator;
import com.example.WebAppProject.util.TestValidator;
import com.example.WebAppProject.util.TestsDtoValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TestsController.class)
@ContextConfiguration(classes = TestsController.class)
class TestsControllerTest {

    @Autowired
    public MockMvc mockMvc;

    @MockBean
    private TestsService testsService;
    @MockBean
    private AccountsService accountsService;

    @Test
    @WithMockUser(username = "Bob", roles = {"STUDENT"})
    void test_showTimeRanOut() {
        String url = "/student/tests/time_ran_out";
        try {
            mockMvc.perform(get(url)).andExpect(status().isOk());
        } catch (Exception ignored) {
        }
    }

    @Test
    @WithMockUser(username = "Bob", roles = {"STUDENT"})
    void test_startTesting() {
        int id = 1;
        User user = new User();
        Mockito.when(testsService.findOneById(id)).thenReturn(new com.example.WebAppProject.models.Test());
        Mockito.when(accountsService.findAccountByUserId(user.getId())).thenReturn(new Account());

        String url = "/student/tests/time_ran_out";
        try {
            mockMvc.perform(get(url)).andExpect(status().isOk());
        } catch (Exception ignored) {
        }
    }
}