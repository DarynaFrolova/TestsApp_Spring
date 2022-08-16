package com.example.WebAppProject.services;

import com.example.WebAppProject.models.Subject;
import com.example.WebAppProject.models.Test;
import com.example.WebAppProject.repositories.TestsRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Contains business logic to store, retrieve, delete and update an object of Test class
 *
 * @author Daryna Frolova
 */

@Service
@Transactional
public class TestsService {

    @PersistenceContext
    private EntityManager entityManager;

    private final TestsRepository testsRepository;

    @Autowired
    public TestsService(TestsRepository testsRepository) {
        this.testsRepository = testsRepository;
    }

    public void updateNumberOfQueries(Integer testId, Integer newNumber) {
        Session session = entityManager.unwrap(Session.class);
        Test test = session.get(Test.class, testId);
        test.setNumberOfQueries(newNumber);
        testsRepository.save(test);
    }

    public Page<Test> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.testsRepository.findAll(pageable);
    }

    public List<Test> findAllTestsBySubjectId(int id) {
        Session session = entityManager.unwrap(Session.class);
        Subject subject = session.get(Subject.class, id);
        return testsRepository.findAllBySubject(subject);
    }

    public Test findOneById(int id) {
        Optional<Test> foundTest = testsRepository.findById(id);
        return foundTest.orElseThrow(NoSuchElementException::new);
    }

    public Test findOneByName(String name) {
        return testsRepository.findByName(name);
    }

    public void save(Test test) {
        testsRepository.save(test);
    }

    public void deleteTestById(int id) {
        testsRepository.deleteById(id);
    }

}
