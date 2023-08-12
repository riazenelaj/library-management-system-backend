package com.teamsystem.project.TeamSystem.Project.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ReviewRequest {
    private int score;
    private  String fullName;
    private String creationDate;
    private String title;
    public String comment;
    public String bookId;
}
