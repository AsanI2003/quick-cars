package com.project.quickcars2.dto;

import com.project.quickcars2.entity.DriveType;
import com.project.quickcars2.entity.FuelType;
import com.project.quickcars2.entity.VehicleType;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AdminVehiclesaveDto {

    @NotBlank(message = "Image URL is required")
    @Pattern(regexp = "^/images/.*", message = "Image URL must start with '/images/'")
    private String imageurl;

    @NotBlank(message = "Name is required")
    @Pattern(regexp = "^[A-Za-z0-9 ]+$", message = "Name must be alphanumeric only")
    private String name;

    @NotBlank(message = "Capacity is required")
    @Pattern(regexp = "^\\d+ Seater$", message = "Capacity must end with 'Seater' (e.g. '5 Seater')")
    private String capacity;

    @NotBlank(message = "Fuel economy is required")
    @Pattern(regexp = "^\\d+(\\.\\d+)? km/l$", message = "Fuel economy must end with 'km/l' (e.g. '16 km/l')")
    private String fuelEconomy;

    @NotNull(message = "Day price is required")
    @Min(value = 1000, message = "Minimum price is 1000")
    @Max(value = 999999, message = "Day price must be a reasonable integer")
    @Digits(integer = 6, fraction = 0, message = "Day price must be an integer")
    private Integer dayPrice;

    @NotNull(message = "Availability is required")
    @Min(value = 0, message = "Availability must be 0 or 1")
    @Max(value = 1, message = "Availability must be 0 or 1")
    private Integer availability;

    @NotNull(message = "Vehicle type is required")
    private VehicleType vehicleType;

    @NotNull(message = "Fuel type is required")
    private FuelType fuelType;

    @NotNull(message = "Drive type is required")
    private DriveType driveType;

    @NotNull(message = "Fuel capacity is required")
    @Min(value = 30, message = "Minimum fuel capacity is 30L")
    @Max(value = 80, message = "Maximum fuel capacity is 80L")
    private Integer fuelCapacityL;
}
