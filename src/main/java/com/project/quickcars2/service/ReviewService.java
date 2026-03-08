package com.project.quickcars2.service;

import com.project.quickcars2.dto.ReviewDto;
import com.project.quickcars2.entity.Review;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReviewService {
    List<ReviewDto> getallReviews();
    void syncReviewerDetails(String email, String newName, String newImageUrl);


    void saveReview(Review review);
}
