package com.example.WebAppProject.services;

import com.example.WebAppProject.repositories.TestsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TestsDtoServiceTest {

    @Mock
    private TestsRepository testsRepository;

    @InjectMocks
    private TestsDtoService testsDtoService;

    @Test
    void findByName() {
        com.example.WebAppProject.models.Test test = new com.example.WebAppProject.models.Test();
        String name = "Grammar";
        test.setName(name);
        when(testsRepository.findByName(name)).thenReturn(test);
    }
}