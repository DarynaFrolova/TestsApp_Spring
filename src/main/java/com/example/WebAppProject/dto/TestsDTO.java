package com.example.WebAppProject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

/**
 * Transfers data from client to server in one shot when admin creates/updates a test
 *
 * @author Daryna Frolova
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestsDTO {
    private int id;

    @Size(min = 2, max = 45, message = "Title length should be between 2 and 45 characters")
    @Pattern(regexp = "^[a-zA-Z\s]*$", message = "This field can contain only letters")
    private String name;
    private String subjectName;
    private String difficultyLevel;

    private int numberOfQuestions;

    @Digits(message = "This field cannot contain any letters, it must be an integer in the range 1 - 120", integer = 3, fraction = 0)
    @Min(message = "Minimum time assigned to test is 1 minute", value = 1)
    @Max(message = "Minimum time assigned to test is 120 minutes", value = 120)
    private int timeToPassInMinutes;
}
