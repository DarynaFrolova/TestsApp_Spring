package com.example.WebAppProject.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

/**
 * Represents a test
 *
 * @author Daryna Frolova
 */

@Entity
@Table(name = "test")
@Getter
@Setter
@NoArgsConstructor
public class Test {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Size(min = 2, max = 45, message = "Title length should be between 2 and 45 characters")
    @Pattern(regexp = "^[a-zA-Z\s]*$", message = "This field can contain only letters")
    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "subject_id", referencedColumnName = "id")
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "difficulty_id", referencedColumnName = "id")
    private Difficulty difficulty;

    @Column(name = "number_of_questions")
    private int numberOfQuestions;

    @Digits(message = "This field cannot contain any letters, it must be an integer in the range 1 - 120", integer = 3, fraction = 0)
    @Min(message = "Minimum time assigned to test is 1 minute", value = 1)
    @Max(message = "Minimum time assigned to test is 120 minutes", value = 120)
    @Column(name = "time_to_pass_in_minutes")
    private int timeToPassInMinutes;

    @Column(name = "number_of_queries")
    private Integer numberOfQueries;

    @OneToMany(mappedBy = "testC")
    private List<TestCase> testCases;

    @OneToMany(mappedBy = "test")
    private List<Question> questions;

    @Column(name = "status")
    private String status;

    public Test(String name, Subject subject, Difficulty difficulty, int numberOfQuestions, int timeToPassInMinutes) {
        this.name = name;
        this.subject = subject;
        this.difficulty = difficulty;
        this.numberOfQuestions = numberOfQuestions;
        this.timeToPassInMinutes = timeToPassInMinutes;
    }

    @Transient
    private List<Answer> answers;

    @Transient
    private List<Question> newQuestions;

    @Transient
    private List<Integer> correctOptions;
}