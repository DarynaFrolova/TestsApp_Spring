package com.example.WebAppProject.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Represents a subject
 *
 * @author Daryna Frolova
 */

@Entity
@Table(name = "subject")
@Getter
@Setter
@NoArgsConstructor
public class Subject {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    @Size(min = 2, max = 100, message = "Subject title length should be between 2 and 45 characters")
    private String name;

    @OneToMany(mappedBy = "subject")
    private List<Test> tests;

    public Subject(String name) {
        this.name = name;
    }
}
