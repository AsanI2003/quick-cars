package com.project.quickcars2.dto;



import com.project.quickcars2.entity.DriveType;
import com.project.quickcars2.entity.FuelType;
import com.project.quickcars2.entity.VehicleType;
import lombok.Data;

@Data
public class VehicleDto {
    private int id;
    private String imageurl;
    private String name;
    private String capacity;
    private String fuelEconomy;
    private Double dayPrice;
    private int availability;
    private VehicleType vehicleType;
    private FuelType fuelType;
    private DriveType driveType;
    private int fuelCapacityL;
}
