package com.teamsystem.project.TeamSystem.Project.models;

import com.teamsystem.project.TeamSystem.Project.entities.Author;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetBooksResponse {
    private String id;
    private String title;
    private String description;
    private String imageData;
    private Author author;
}
