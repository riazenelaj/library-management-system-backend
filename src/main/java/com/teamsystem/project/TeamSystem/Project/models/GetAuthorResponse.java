package com.teamsystem.project.TeamSystem.Project.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAuthorResponse {
    private String name;
    private String surname;
}
