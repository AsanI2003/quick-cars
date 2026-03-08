package com.project.quickcars2.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String customerEmail;

    @Column(nullable = false)
    private int contactNumber;

    @Column(nullable = false)
    private int vehicleId;

    @Column(nullable = false)
    private LocalDate pickupDate;

    @Column(nullable = false)
    private String pickupLocation;

    @Column(nullable = false)
    private LocalDate dropoffDate;

    @Column(nullable = false)
    private String dropoffLocation;

    @Column(nullable = false)
    private Double totalPrice;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private String status = "pending";

}
