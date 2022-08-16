package com.example.WebAppProject.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * Represents a question
 *
 * @author Daryna Frolova
 */

@Entity
@Table(name = "question")
@Getter
@Setter
@NoArgsConstructor
public class Question {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "test_id", referencedColumnName = "id")
    private Test test;

    @ManyToOne
    @JoinColumn(name = "question_type_id", referencedColumnName = "id")
    private QuestionType questionType;

    @OneToMany(mappedBy = "question")
    private List<Answer> answers;

    @Column(name = "correct_answer_id")
    private String correctAnswerId;
}
