package com.example.WebAppProject.services;

import com.example.WebAppProject.repositories.TestsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TestsServiceTest {

    @Mock
    private TestsRepository testsRepository;

    @InjectMocks
    private TestsService testsService;

    @Test
    void findAll() {
        com.example.WebAppProject.models.Test test = new com.example.WebAppProject.models.Test();
        when(testsRepository.findAll()).thenReturn(Collections.singletonList(test));
    }

    @Test
    void findOneById() {
        com.example.WebAppProject.models.Test test = new com.example.WebAppProject.models.Test();
        int id = 1;
        test.setId(id);
        when(testsService.findOneById(id)).thenReturn(test);
    }

    @Test
    void findOneByName() {
        com.example.WebAppProject.models.Test test = new com.example.WebAppProject.models.Test();
        String name = "Grammar";
        test.setName(name);
        when(testsService.findOneByName(name)).thenReturn(test);
    }

    @Test
    void testDelete(){
        final int id = 1;
        testsService.deleteTestById(id);
        Mockito.verify(testsRepository).deleteById(id);
    }

    @Test
    void testSave(){
        com.example.WebAppProject.models.Test test = new com.example.WebAppProject.models.Test();
        testsService.save(test);
        Mockito.verify(testsRepository, times(1)).save(any());
    }
}