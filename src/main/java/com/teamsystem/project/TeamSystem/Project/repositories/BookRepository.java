package com.teamsystem.project.TeamSystem.Project.repositories;

import com.teamsystem.project.TeamSystem.Project.entities.Book;
import com.teamsystem.project.TeamSystem.Project.models.GetBookResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {

    @Query("SELECT book FROM Book book WHERE " +
            "book.title LIKE CONCAT('%',:query, '%')" +
            "Or book.author.name LIKE CONCAT('%', :query, '%')" +
            "Or book.author.surname LIKE CONCAT('%', :query, '%')")
    List<Book> searchBooks(String query);
    List<Book> findByAuthorId(String authorId);

}
