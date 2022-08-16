package com.example.WebAppProject.controllers;

import com.example.WebAppProject.models.Account;
import com.example.WebAppProject.models.TestCase;
import com.example.WebAppProject.models.User;
import com.example.WebAppProject.services.AccountsService;
import com.example.WebAppProject.services.TestCaseService;
import com.example.WebAppProject.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;


@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@ContextConfiguration(classes = UserController.class)
class UserControllerTest {

    @Autowired
    public MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private AccountsService accountsService;

    @MockBean
    private TestCaseService testCaseService;

    @InjectMocks
    private UserController userController;

    @Test
    @WithMockUser(username = "Bob", roles = {"STUDENT"})
    void test_showAccountInfo() {
        int id = 1;
        User user = new User();
        Mockito.when(userService.findOneById(id)).thenReturn(user);

        Account account = new Account();
        Mockito.when(accountsService.findAccountByUserId(id)).thenReturn(account);

        List<TestCase> allTestCasesByAccount = new ArrayList<>();
        allTestCasesByAccount.add(new TestCase());
        Mockito.when(testCaseService.findAllTestCasesByAccount(account)).thenReturn(allTestCasesByAccount);

        String url = "/student/account/1";

        try {
            mockMvc.perform(get(url)).andExpect(status().isOk());
        } catch (Exception ignored) {
        }
    }

    @Test
    @WithMockUser(username = "Mary", roles = {"ADMIN"})
    void test_showToAdminStudentInfo() {
        int id = 1;
        User user = new User();
        Mockito.when(userService.findOneById(id)).thenReturn(user);

        Account account = new Account();
        Mockito.when(accountsService.findAccountByUserId(id)).thenReturn(account);

        List<TestCase> allTestCasesByAccount = new ArrayList<>();
        allTestCasesByAccount.add(new TestCase());
        Mockito.when(testCaseService.findAllTestCasesByAccount(account)).thenReturn(allTestCasesByAccount);

        String url = "/admin/student/info/1";

        try {
            mockMvc.perform(get(url)).andExpect(status().isOk());
        } catch (Exception ignored) {
        }
    }
}