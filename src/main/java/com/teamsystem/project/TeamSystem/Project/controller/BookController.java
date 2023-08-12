package com.teamsystem.project.TeamSystem.Project.controller;

import com.teamsystem.project.TeamSystem.Project.entities.Book;
import com.teamsystem.project.TeamSystem.Project.models.*;
import com.teamsystem.project.TeamSystem.Project.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.apache.tomcat.util.http.fileupload.FileUploadBase.MULTIPART_FORM_DATA;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<GetBooksResponse>> displayAllBooks() throws Exception {
        return new ResponseEntity<>(bookService.displayAllBooks(), HttpStatus.OK);
    }

    @PostMapping(value = "/create")
    public ResponseEntity<BookResponse> createBook(
            @RequestBody BookRequest bookRequest
    ) throws Exception {
        return new ResponseEntity<>(bookService.createBook(bookRequest), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteBook(
            @PathVariable(name = "id") String id
    ) throws Exception {
        bookService.deleteBook(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<BookResponse> updateBook(
            @RequestBody UpdateBookRequest updateBookRequest,
            @PathVariable (name = "id") String id
    ) throws Exception {
        return new ResponseEntity<>(bookService.updateBook(updateBookRequest,id), HttpStatus.OK);
    }

    @GetMapping("/bookInfo/{id}")
    public ResponseEntity<GetBookResponse> bookInfo(
            @PathVariable(name = "id") String id
    ) throws Exception {
        return new ResponseEntity<>(bookService.bookInfo(id), HttpStatus.OK);
    }

    @GetMapping("/search")
    public  ResponseEntity<List<GetBooksResponse>> searchBooks(
            @RequestParam("query")String query
    )throws Exception{
        return new ResponseEntity<>(bookService.searchBooks(query),HttpStatus.OK);
    }

}
