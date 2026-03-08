package com.project.quickcars2.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AdminRentalInquiryDto {
    private int id;
    private int vehicleId;
    private String contactNumber;
    private LocalDateTime createdAt;
    private LocalDate pickupDate;
    private LocalDate dropoffDate;
    private String pickupLocation;
    private String dropoffLocation;
    private double totalPrice;
    private String status;
}
