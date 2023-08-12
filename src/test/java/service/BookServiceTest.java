package service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.teamsystem.project.TeamSystem.Project.entities.Author;
import com.teamsystem.project.TeamSystem.Project.entities.Book;
import com.teamsystem.project.TeamSystem.Project.models.*;
import com.teamsystem.project.TeamSystem.Project.repositories.AuthorRepository;
import com.teamsystem.project.TeamSystem.Project.repositories.BookRepository;
import com.teamsystem.project.TeamSystem.Project.service.BookService;
import com.teamsystem.project.TeamSystem.Project.service.ReviewService;
import org.junit.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private ReviewService reviewService;
    @Mock
    private GetBookResponse getBookResponse;
    @InjectMocks
    private BookService bookService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    @DisplayName("This method create a book")
    public void testCreateBook() throws Exception {
        BookRequest book = new BookRequest("test", "test", "111", null);

        Author foundAuthor = new Author();
        foundAuthor.setId("111");
        when(authorRepository.findById("111")).thenReturn(Optional.of(foundAuthor));
        Book savedBook = new Book();
        savedBook.setId("222");
        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);
        BookResponse bookResponse = bookService.createBook(book);
        assertNotNull(bookResponse);
        assertEquals("222", bookResponse.getId());
        verify(authorRepository, times(1)).findById("111");
        verify(bookRepository, times(1)).save(any(Book.class));
    }


    @Test
    public void throwsExceptionsWhenAuthorIdNotFoundOnCreate()throws Exception{
        String id="222";
        BookRequest bookRequest = new BookRequest("test", "test", null, null);
        when(authorRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrows(Exception.class,()->bookService.createBook(bookRequest));
    }


    @Test
    @DisplayName("This method deletes a book")
    public void testDeleteBook() throws Exception {
        String existingBookId = "111";
        when(bookRepository.findById(existingBookId)).thenReturn(Optional.of(new Book()));
        bookService.deleteBook(existingBookId);
        verify(bookRepository, times(1)).deleteById(existingBookId);
    }

    @Test(expected = Exception.class)
    public void throwsExceptionWhenBookNotFoundOnDelete() throws Exception {
        String nonExistingBookId = "111";
        when(bookRepository.findById(nonExistingBookId)).thenReturn(Optional.empty());
        bookService.deleteBook(nonExistingBookId);
    }
    @Test
    public void testUpdateBook_Success() throws Exception {
        String id = "111";
        String updatedTitle = "Updated title";
        String updatedDescription = "Updated description";
        Book book = new Book(id, "test", "test", null, null);
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        UpdateBookRequest bookRequest = new UpdateBookRequest(updatedTitle, updatedDescription);
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        BookResponse bookResponse = bookService.updateBook(bookRequest, id);

        verify(bookRepository, times(1)).findById(id);
        verify(bookRepository, times(1)).save(book);
        assertNotNull(bookResponse);
        assertEquals(id, bookResponse.getId());
        assertEquals(updatedTitle, book.getTitle());
        assertEquals(updatedDescription, book.getDescription());
    }
    @Test
    public void testUpdateBookBookNotFound() throws Exception {
        String id = "111";
        String updatedTitle = "Updated title";
        String updatedDescription = "Updated description";
        when(bookRepository.findById(id)).thenReturn(Optional.empty());
        UpdateBookRequest bookRequest = new UpdateBookRequest(updatedTitle, updatedDescription);
        Assertions.assertThrows(Exception.class, () -> bookService.updateBook(bookRequest,id));
    }
    @Test
    public void throwsExceptionWhenBookNotFoundOnUpdate() throws Exception {
        String nonExistingBookId = "111";
        UpdateBookRequest updateBookRequest=new UpdateBookRequest("test","test");
        when(bookRepository.findById(nonExistingBookId)).thenReturn(Optional.empty());
        Assertions.assertThrows(Exception.class, () -> bookService.updateBook(updateBookRequest,nonExistingBookId));
    }
    @Test
    @DisplayName("This method create a book")
    public void testGetBookInfo() throws Exception {
        String id = "111";
        double avrScore = 4.5;
        Book newBook = new Book("111", "test", "test", null, null);
        when(bookRepository.findById(id)).thenReturn(Optional.of(newBook));
        when(reviewService.calculateRating(id)).thenReturn(avrScore);
        GetBookResponse bookResponse = bookService.bookInfo(id);

        assertEquals(newBook.getTitle(), bookResponse.getTitle());
        assertEquals(newBook.getDescription(), bookResponse.getDescription());
        assertEquals(avrScore, bookResponse.getRates());

        verify(bookRepository, times(1)).findById(id);
        verify(reviewService, times(1)).calculateRating(id);
    }

    @Test
    public void throwsExceptionsWhenBookIdNotFound() throws Exception {
        String nonExistingBookId = "111";
        when(bookRepository.findById(nonExistingBookId)).thenReturn(Optional.empty());
        Assertions.assertThrows(Exception.class, () -> bookService.bookInfo(nonExistingBookId));
    }

    @Test
    public void testSearchBooks() throws Exception {
        String query = "test";
        Book book1 = new Book("1", "Test Book 1", "test", null, null);
        Book book2 = new Book("2", "Test Book 2", "test", null, null);
        List<Book> bookList = List.of(book1, book2);
        when(bookRepository.searchBooks(query)).thenReturn(bookList);
        List<GetBooksResponse> bookResponses = bookService.searchBooks(query);

        assertEquals(2, bookResponses.size());
        GetBooksResponse response1 = bookResponses.get(0);
        assertEquals(book1.getTitle(), response1.getTitle());
        assertEquals(book1.getDescription(), response1.getDescription());
        assertNull(response1.getImageData());
        GetBooksResponse response2 = bookResponses.get(1);
        assertEquals(book2.getTitle(), response2.getTitle());
        assertEquals(book2.getDescription(), response2.getDescription());
        assertNull(response2.getImageData());
        verify(bookRepository, times(1)).searchBooks(query);

    }
    @Test
    public void testDisplayAllBooks() throws Exception {
        List<Book> booksList = new ArrayList<>();
        Book book1 = new Book("1", "Book 1", "test", null, null);
        Book book2 = new Book("2", "Book 2", "test", null, null);
        booksList.add(book1);
        booksList.add(book2);
        when(bookRepository.findAll()).thenReturn(booksList);
        List<GetBooksResponse> booksResponseList = bookService.displayAllBooks();

        verify(bookRepository, times(1)).findAll();

        assertNotNull(booksResponseList);
        assertEquals(2, booksResponseList.size());

        GetBooksResponse response1 = booksResponseList.get(0);
        assertEquals(book1.getId(), response1.getId());
        assertEquals(book1.getTitle(), response1.getTitle());
        assertEquals(book1.getDescription(), response1.getDescription());
        GetBooksResponse response2=booksResponseList.get(1);
        assertEquals(book2.getId(), response2.getId());
        assertEquals(book2.getTitle(), response2.getTitle());
        assertEquals(book2.getDescription(), response2.getDescription());
    }
}