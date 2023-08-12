package com.teamsystem.project.TeamSystem.Project.service;

import com.teamsystem.project.TeamSystem.Project.entities.Book;
import com.teamsystem.project.TeamSystem.Project.entities.Review;
import com.teamsystem.project.TeamSystem.Project.models.GetReviewResponse;
import com.teamsystem.project.TeamSystem.Project.models.ReviewRequest;
import com.teamsystem.project.TeamSystem.Project.models.ReviewResponse;
import com.teamsystem.project.TeamSystem.Project.repositories.BookRepository;
import com.teamsystem.project.TeamSystem.Project.repositories.ReviewRepository;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;

    public ReviewService(ReviewRepository reviewRepository, BookRepository bookRepository) {
        this.reviewRepository = reviewRepository;
        this.bookRepository = bookRepository;
    }

    public List<GetReviewResponse> displayReviews(String bookId) throws Exception {
        Optional<Book> foundBook = bookRepository.findById(bookId);
        if (foundBook.isEmpty()) {
            throw new Exception("Book does not exist");
        }
        List<Review> reviewList = reviewRepository.findByBookId(bookId);
        if (reviewList.isEmpty()) {
            throw new Exception("This book has no reviews");
        }
        List<GetReviewResponse> responseList = new ArrayList<>();
        for (Review review : reviewList) {
            GetReviewResponse reviewResponse = new GetReviewResponse();
            reviewResponse.setFullName(review.getFullName());
            reviewResponse.setTitle(review.getTitle());
            reviewResponse.setCreationDate(review.getCreationDate());
            reviewResponse.setScore(review.getScore());
            reviewResponse.setComment(review.getComment());
            responseList.add(reviewResponse);
        }
        return responseList;
    }

    public ReviewResponse createReview(ReviewRequest reviewRequest) throws Exception {
        Optional<Book> foundBook = bookRepository.findById(reviewRequest.getBookId());
        if (foundBook.isEmpty()) {
            throw new Exception("Book does not exist");
        }
        Book book = foundBook.get();
        Review review = new Review();
        review.setFullName(reviewRequest.getFullName());
        review.setTitle(reviewRequest.getTitle());
         String creationDate= LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM uuuu"));
        review.setCreationDate(creationDate);
        review.setComment(reviewRequest.getComment());
        review.setScore(reviewRequest.getScore());
        review.setBook(book);
        Review savedReview = reviewRepository.save(review);
        return new ReviewResponse(
                savedReview.getId()
        );
    }

    public double calculateRating(String bookId) throws Exception {
        Optional<Book> foundBook = bookRepository.findById(bookId);
        if (foundBook.isEmpty()) {
            throw new Exception("Book does not exist");
        }
        int totalScore = 0;
        List<Review> reviews = reviewRepository.findByBookId(bookId);
        if (!reviews.isEmpty()) {
            for (Review review : reviews) {
                totalScore += review.getScore();
            }
            DecimalFormat df = new DecimalFormat("#.#");
            double average= (double) totalScore / reviews.size();
            return Double.parseDouble(df.format(average));
        }
        return totalScore;
    }
}
