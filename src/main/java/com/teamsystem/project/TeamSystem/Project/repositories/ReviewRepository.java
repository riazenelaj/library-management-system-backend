package com.teamsystem.project.TeamSystem.Project.repositories;

import com.teamsystem.project.TeamSystem.Project.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review , String> {
    List<Review> findByBookId(String book_id);
}
