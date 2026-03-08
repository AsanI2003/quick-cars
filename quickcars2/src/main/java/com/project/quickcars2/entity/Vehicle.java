package com.project.quickcars2.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id ;

    @Column(nullable = false)
    private String imageurl;

    @Column(nullable = false)
    private String name ;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleType vehicleType;

    @Column(nullable = false)
    private String capacity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FuelType fuelType;

    @Column(nullable = false)
    private String fuelEconomy;

    @Column(nullable = false)
    private int fuelCapacityL;

    @Column(nullable = false)
    private Double dayPrice;

    @Column(nullable = false)
    private int availability;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DriveType driveType;

}
