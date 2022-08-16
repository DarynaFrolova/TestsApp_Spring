package com.example.WebAppProject.services;

import com.example.WebAppProject.models.Test;
import com.example.WebAppProject.repositories.TestsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Contains business logic to retrieve an object of TestsDto class
 *
 * @author Daryna Frolova
 */

@Service
@Transactional
public class TestsDtoService {

    private final TestsRepository testsRepository;

    @Autowired
    public TestsDtoService(TestsRepository testsRepository) {
        this.testsRepository = testsRepository;
    }

    public Test findByName(String name) {
        return testsRepository.findByName(name);
    }
}
