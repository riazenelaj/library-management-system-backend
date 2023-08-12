package com.teamsystem.project.TeamSystem.Project.repositories;

import com.teamsystem.project.TeamSystem.Project.entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, String> {
    Optional<Author> findByName(String name);

    @Query("SELECT author FROM Author  author WHERE " +
            "author.name LIKE CONCAT('%',:query, '%')" +
            "Or author.surname LIKE CONCAT('%', :query, '%')")
    List<Author> searchAuthor(String query);
}
