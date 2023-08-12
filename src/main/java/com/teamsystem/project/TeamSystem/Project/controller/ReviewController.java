package com.teamsystem.project.TeamSystem.Project.controller;

import com.teamsystem.project.TeamSystem.Project.models.GetReviewResponse;
import com.teamsystem.project.TeamSystem.Project.models.ReviewRequest;
import com.teamsystem.project.TeamSystem.Project.models.ReviewResponse;
import com.teamsystem.project.TeamSystem.Project.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/review")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/create")
    public ResponseEntity<ReviewResponse> createReview(
            @RequestBody ReviewRequest reviewRequest
    ) throws Exception {
        return new ResponseEntity<>(reviewService.createReview(reviewRequest), HttpStatus.CREATED);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<List<GetReviewResponse>> displayReviews(
            @PathVariable(name = "id") String id
    ) throws Exception {
        return new ResponseEntity<>(reviewService.displayReviews(id), HttpStatus.OK);
    }
}
