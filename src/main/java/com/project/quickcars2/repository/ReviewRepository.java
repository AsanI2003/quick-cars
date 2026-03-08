package com.project.quickcars2.repository;

import com.project.quickcars2.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {
    List<Review> findByReviewerEmail(String reviewerEmail);


}
