package com.teamsystem.project.TeamSystem.Project.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateBookRequest {
    private String title;
    private String description;
}
