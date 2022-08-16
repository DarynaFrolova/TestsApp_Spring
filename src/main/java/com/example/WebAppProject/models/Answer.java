package com.example.WebAppProject.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Represents an answer
 *
 * @author Daryna Frolova
 */

@Entity
@Table(name = "answer")
@Getter
@Setter
@NoArgsConstructor
public class Answer {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "question_id", referencedColumnName = "id")
    private Question question;

    @Column(name = "is_correct")
    private int isCorrect;
    // 0 - false; 1 - true

    public Answer(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return id + ", " + content;
    }
}
