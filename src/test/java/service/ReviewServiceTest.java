package service;

import com.teamsystem.project.TeamSystem.Project.entities.Author;
import com.teamsystem.project.TeamSystem.Project.entities.Book;
import com.teamsystem.project.TeamSystem.Project.entities.Review;
import com.teamsystem.project.TeamSystem.Project.models.GetReviewResponse;
import com.teamsystem.project.TeamSystem.Project.models.ReviewRequest;
import com.teamsystem.project.TeamSystem.Project.models.ReviewResponse;
import com.teamsystem.project.TeamSystem.Project.repositories.BookRepository;
import com.teamsystem.project.TeamSystem.Project.repositories.ReviewRepository;
import com.teamsystem.project.TeamSystem.Project.service.ReviewService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private ReviewService reviewService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateReview() throws Exception {
        String id = "111";
        Book foundBook = new Book();
        foundBook.setId("222");
        ReviewRequest reviewRequest = new ReviewRequest(3, "Test test", null, "test", "test", "222");
        when(bookRepository.findById(reviewRequest.getBookId())).thenReturn(Optional.of(foundBook));

        Review savedReview = new Review();
        savedReview.setId(id);
        when(reviewRepository.save(any(Review.class))).thenReturn(savedReview);
        ReviewResponse reviewResponse = reviewService.createReview(reviewRequest);
        Assertions.assertEquals(id, savedReview.getId());
        verify(bookRepository, times(1)).findById("222");
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    public void throwsExceptionsWhenBookIdNotFoundOnCreate() throws Exception {
        String id = "222";
        ReviewRequest reviewRequest = new ReviewRequest(3, "Test test", null, "test", "test", null);
        when(bookRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> reviewService.createReview(reviewRequest));
    }

    @Test
    public void testCalculateRating() throws Exception {
        Book foundBook = new Book();
        foundBook.setId("222");
        when(bookRepository.findById(foundBook.getId())).thenReturn(Optional.of(foundBook));
        List<Review> reviewList = new ArrayList<>();
        reviewList.add(new Review("1", "test", null, "test", "test", 3, foundBook));
        reviewList.add(new Review("2", "test", null, "test", "test", 2, foundBook));
        reviewList.add(new Review("3", "test", null, "test", "test", 4, foundBook));

        when(reviewRepository.findByBookId(foundBook.getId())).thenReturn(reviewList);
        double avrScore = reviewService.calculateRating(foundBook.getId());
        Assertions.assertEquals(3, avrScore);
        verify(bookRepository, times(1)).findById(foundBook.getId());
        verify(reviewRepository, times(1)).findByBookId(foundBook.getId());
    }

    @Test
    public void throwsExceptionWhenBookNotFoundOnCalculateRating() throws Exception {
        String id = "222";
        when(bookRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> reviewService.calculateRating(id));
    }
   //Should I check if calculateRating returns 0 when book has no reviews????

    @Test
    public void testDisplayAllReviews() throws Exception {
        String id = "333";
        Book book = new Book();
        book.setId(id);
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));

        Review review1 = new Review("1", "test", null, "test", "test", 3, book);
        Review review2 = new Review("2", "test", null, "test", "test", 2, book);
        List<Review> reviewList = new ArrayList<>();
        reviewList.add(review1);
        reviewList.add(review2);

        when(reviewRepository.findByBookId(id)).thenReturn(reviewList);
        List<GetReviewResponse> reviewResponseList = reviewService.displayReviews(id);

        Assertions.assertEquals(2, reviewResponseList.size());
        GetReviewResponse response1 = reviewResponseList.get(0);
        Assertions.assertEquals(review1.getScore(), response1.getScore());
        Assertions.assertEquals(review1.getFullName(), response1.getFullName());
        Assertions.assertEquals(review1.getTitle(), response1.getTitle());
        Assertions.assertEquals(review1.getComment(), response1.getComment());
        Assertions.assertEquals(review1.getCreationDate(), response1.getCreationDate());

        GetReviewResponse response2 = reviewResponseList.get(1);
        Assertions.assertEquals(review2.getScore(), response2.getScore());
        Assertions.assertEquals(review2.getFullName(), response2.getFullName());
        Assertions.assertEquals(review2.getTitle(), response2.getTitle());
        Assertions.assertEquals(review2.getComment(), response2.getComment());
        Assertions.assertEquals(review2.getCreationDate(), response2.getCreationDate());
        verify(bookRepository,times(1)).findById(id);
        verify(reviewRepository, times(1)).findByBookId(id);
    }
    @Test
    public void throwsExceptionsWhenBookNotFoundOnDisplayReviews() throws Exception {
        String id = "222";
        when(bookRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> reviewService.displayReviews(id));
    }
    @Test
    public void throwExceptionWhenBookHasNoReviewsOnDisplayReview() throws Exception {
        String bookId = "333";

        Book book = new Book();
        book.setId(bookId);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        List<Review> reviewList = new ArrayList<>();
        when(reviewRepository.findByBookId(bookId)).thenReturn(reviewList);
        Exception exception = assertThrows(Exception.class, () -> reviewService.displayReviews(bookId));

        Assertions.assertEquals("This book has no reviews", exception.getMessage());
        verify(bookRepository, times(1)).findById(bookId);
        verify(reviewRepository, times(1)).findByBookId(bookId);
    }
}

