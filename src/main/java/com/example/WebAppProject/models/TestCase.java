package com.example.WebAppProject.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.List;

/**
 * Represents a specific test passed by a specific student in a specific period of time
 *
 * @author Daryna Frolova
 */

@Entity
@Table(name = "account_has_test")
@Getter
@Setter
@NoArgsConstructor
public class TestCase {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private Account account;

    @ManyToOne
    @JoinColumn(name = "test_id", referencedColumnName = "id")
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private Test testC;

    @Column(name = "correct_answers")
    private int score;

    @Column(name = "start_time")
    private String startTime;

    @Column(name = "end_time")
    private String endTime;

    @Column(name = "result")
    private double result;

    @Transient
    private List<Answer> answers;
}