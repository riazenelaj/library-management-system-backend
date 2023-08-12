package com.teamsystem.project.TeamSystem.Project.service;

import com.teamsystem.project.TeamSystem.Project.entities.Author;
import com.teamsystem.project.TeamSystem.Project.entities.Book;
import com.teamsystem.project.TeamSystem.Project.entities.Review;
import com.teamsystem.project.TeamSystem.Project.models.*;
import com.teamsystem.project.TeamSystem.Project.repositories.AuthorRepository;
import com.teamsystem.project.TeamSystem.Project.repositories.BookRepository;
import com.teamsystem.project.TeamSystem.Project.utils.ImageUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final ReviewService reviewService;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, ReviewService reviewService) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.reviewService = reviewService;
    }

public List<GetBooksResponse> displayAllBooks() throws Exception {
    List<Book> books = bookRepository.findAll();

    return books.stream().map(book -> {
        byte[] imageData = book.getImageData();
       String decompressedImageData = null;

        if (imageData != null) {
            decompressedImageData = ImageUtil.decompressImage(imageData);
        }

        return new GetBooksResponse(
                book.getId(),
                book.getTitle(),
                book.getDescription(),
                decompressedImageData,
                book.getAuthor()
        );
    }).toList();
}

    public BookResponse createBook(BookRequest bookRequest) throws Exception {
        Optional<Author> foundAuthor = authorRepository.findById(bookRequest.getAuthorId());
        if (foundAuthor.isEmpty()) {
            throw new Exception("Author does not exist");
        }
        Author author = foundAuthor.get();
        Book book = new Book();
        book.setAuthor(author);
        book.setTitle(bookRequest.getTitle());
        book.setDescription(bookRequest.getDescription());
        String imageData = bookRequest.getImage();
        if (imageData != null) {
            book.setImageData(ImageUtil.compressImage(bookRequest.getImage().getBytes()));
        }
        Book savedBook = bookRepository.save(book);
        return new BookResponse(savedBook.getId());
    }

    public void deleteBook(String id) throws Exception {
        if (bookRepository.findById(id).isEmpty()) {
            throw new Exception("Id does not exist .");
        }
        bookRepository.deleteById(id);
    }

    public BookResponse updateBook(UpdateBookRequest request, String id) throws Exception {
        Optional<Book> foundBook = bookRepository.findById(id);
        if (foundBook.isEmpty()) {
            throw new Exception("Book with id " + id + " not found.");
        }

        Book book = foundBook.get();
        if (request.getTitle() != null) {
            book.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            book.setDescription(request.getDescription());
        }

        Book savedBook = bookRepository.save(book);
        return new BookResponse(savedBook.getId());
    }


    public GetBookResponse bookInfo(String id) throws Exception {
        Optional<Book> foundBook = bookRepository.findById(id);
        if (foundBook.isEmpty()) {
            throw new Exception("Book does not exist");
        }
        Book book = foundBook.get();
        double averageScore = reviewService.calculateRating(id);
        GetBookResponse bookResponse = new GetBookResponse();
        bookResponse.setTitle(book.getTitle());
        bookResponse.setDescription(book.getDescription());
        bookResponse.setRates(averageScore);
        byte[] imageData = book.getImageData();
        if (imageData != null) {
            bookResponse.setImageData(ImageUtil.decompressImage(imageData));
        }
        return bookResponse;
    }

    public List<GetBooksResponse> searchBooks(String query) throws Exception {
        List<Book> books = bookRepository.searchBooks(query);
        List<GetBooksResponse> responses = new ArrayList<>();
        for (Book book : books) {
            GetBooksResponse response = new GetBooksResponse();
            response.setId(book.getId());
            response.setAuthor(book.getAuthor());
            response.setTitle(book.getTitle());
            response.setDescription(book.getDescription());

            byte[] imageData = book.getImageData();
            if (imageData != null) {
                response.setImageData(ImageUtil.decompressImage(imageData));
            }
            responses.add(response);
        }
        return responses;
    }
}
