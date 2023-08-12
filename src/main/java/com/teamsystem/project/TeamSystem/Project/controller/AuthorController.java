package com.teamsystem.project.TeamSystem.Project.controller;

import com.teamsystem.project.TeamSystem.Project.entities.Author;
import com.teamsystem.project.TeamSystem.Project.models.AuthorRequest;
import com.teamsystem.project.TeamSystem.Project.models.AuthorResponse;
import com.teamsystem.project.TeamSystem.Project.models.GetAuthorResponse;
import com.teamsystem.project.TeamSystem.Project.models.UpdateAuthorRequest;
import com.teamsystem.project.TeamSystem.Project.service.AuthorService;
import com.teamsystem.project.TeamSystem.Project.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/authors")
public class AuthorController {
    private final AuthorService authorService;
    private final ReviewService reviewService;

    public AuthorController(AuthorService authorService, ReviewService reviewService) {
        this.authorService = authorService;
        this.reviewService = reviewService;
    }

    @GetMapping
    public ResponseEntity<List<Author>> displayAllAuthors(
    ) throws Exception {
        return new ResponseEntity<>(authorService.displayAllAuthors(), HttpStatus.OK);
    }


    @PostMapping("/create")
    public ResponseEntity<AuthorResponse> createAuthor(
            @RequestBody AuthorRequest authorRequest
    ) throws Exception {
        return new ResponseEntity<>(authorService.createAuthor(authorRequest), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteAuthor(
            @PathVariable(name = "id") String id
    ) throws Exception {
        authorService.deleteAuthor(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<AuthorResponse> updateAuthor(
            @RequestBody UpdateAuthorRequest updateAuthorRequest,
            @PathVariable (name="id") String id
    ) throws Exception {
        return new ResponseEntity<>(authorService.updateAuthor(updateAuthorRequest,id), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Author>> searchAuthor(
            @RequestParam("query") String query
    )throws Exception{
        return new ResponseEntity<>(authorService.searchAuthor(query),HttpStatus.OK);

    }
}
