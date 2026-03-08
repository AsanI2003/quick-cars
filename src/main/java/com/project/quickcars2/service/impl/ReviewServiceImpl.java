package com.project.quickcars2.service.impl;


import com.project.quickcars2.dto.ReviewDto;
import com.project.quickcars2.entity.Review;
import com.project.quickcars2.repository.ReviewRepository;
import com.project.quickcars2.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final  ModelMapper modelMapper;



    @Override
    public List<ReviewDto> getallReviews() {
        return reviewRepository.findAll().stream().map(review -> modelMapper.map(review, ReviewDto.class))
                .collect(Collectors.toList());
    }

    public void syncReviewerDetails(String email, String newName, String newImageUrl) {
        List<Review> reviews = reviewRepository.findByReviewerEmail(email);
        for (Review review : reviews) {
            review.setName(newName);
            review.setImageurl(newImageUrl);
        }
        reviewRepository.saveAll(reviews);
    }

    @Override
    public void saveReview(Review review) {
        reviewRepository.save(review);
    }


}
