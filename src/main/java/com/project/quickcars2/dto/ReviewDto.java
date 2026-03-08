package com.project.quickcars2.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReviewDto {

    private String description;
    private String name;
    private LocalDate createdAt;
    private String imageurl;
    private int rating;
    private String reviewerEmail;
}
