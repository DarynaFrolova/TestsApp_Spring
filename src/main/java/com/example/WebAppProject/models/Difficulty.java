package com.example.WebAppProject.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * Represents a difficulty level
 *
 * @author Daryna Frolova
 */

@Entity
@Table(name = "difficulty")
@Getter
@Setter
@NoArgsConstructor
public class Difficulty {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "level")
    private String level;

    @OneToMany(mappedBy = "difficulty")
    private List<Test> tests;
}
