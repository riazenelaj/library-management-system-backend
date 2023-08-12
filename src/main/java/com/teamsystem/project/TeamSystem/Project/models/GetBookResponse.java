package com.teamsystem.project.TeamSystem.Project.models;

import lombok.Data;

@Data
public class GetBookResponse {
    private String title;
    private String description;
    private String imageData;
    private double rates;
}
