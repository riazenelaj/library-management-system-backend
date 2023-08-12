package com.teamsystem.project.TeamSystem.Project.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private  String fullName;
    private String creationDate;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String comment;
    private int score;
    @ManyToOne
    private Book book;
}
