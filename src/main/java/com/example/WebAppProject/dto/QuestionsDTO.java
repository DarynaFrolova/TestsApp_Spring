package com.example.WebAppProject.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Transfers data from client to server in one shot when admin creates/updates a question
 *
 * @author Daryna Frolova
 */

@Getter
@Setter
@NoArgsConstructor
public class QuestionsDTO {

    private int questionId;

    private String testTitle;

    @Size(min = 2, max = 150, message = "Question length should be between 2 and 150 characters")
    private String questionTitle;

    private String questionType;

    @NotBlank(message = "This field cannot be empty. Every question must have at least 3 options")
    @Size(max = 100, message = "Answer should be max 100 characters long")
    private String optionOne;

    @NotBlank(message = "This field cannot be empty. Every question must have at least 3 options")
    @Size(max = 100, message = "Answer should be max 100 characters long")
    private String optionTwo;

    @NotBlank(message = "This field cannot be empty. Every question must have at least 3 options")
    @Size(max = 100, message = "Answer should be max 100 characters long")
    private String optionThree;

    private String optionFour;

    private String correctAnswerOne;
    private String correctAnswerTwo;
    private String correctAnswerThree;
    private String correctAnswerFour;
}
