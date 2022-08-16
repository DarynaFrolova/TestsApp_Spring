package com.example.WebAppProject.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * Represents a question type
 *
 * @author Daryna Frolova
 */

@Entity
@Table(name = "question_type")
@Getter
@Setter
@NoArgsConstructor
public class QuestionType {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "questionType")
    private List<Question> questions;

    public QuestionType(int id) {
        this.id = id;
    }
}
