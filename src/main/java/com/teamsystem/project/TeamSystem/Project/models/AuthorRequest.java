package com.teamsystem.project.TeamSystem.Project.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthorRequest {
    private String name;
    private String surname;
}
