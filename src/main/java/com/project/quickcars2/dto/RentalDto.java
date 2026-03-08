package com.project.quickcars2.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;
@Data
public class RentalDto {

    private int id;
    private String customerEmail;

    @NotBlank(message = "Contact number is required")
    @Pattern(regexp = "^07[0-9]{8}$", message = "Invalid Sri Lankan mobile number format")
    private String contactNumber;
    private int vehicleId;
    private LocalDate pickupDate;
    private String pickupLocation;
    private LocalDate dropoffDate;
    private String dropoffLocation;
    private Double totalPrice;
    private LocalDate createdAt;
    private String status;
}
