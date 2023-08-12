package com.teamsystem.project.TeamSystem.Project.models;

import lombok.Data;

import java.time.LocalDate;

@Data
public class GetReviewResponse {
     private int score;
    private  String fullName;
    private String creationDate;
    private String title;
    public String comment;
}
