package com.project.quickcars2.dto;

import lombok.Data;

import java.time.LocalDate;
@Data
public class UserDashboardRentalDto {
    private String name;
    private LocalDate pickupDate;
    private String pickupLocation;
    private LocalDate dropoffDate;
    private String dropoffLocation;
    private Double totalPrice;
    private String status;
}
