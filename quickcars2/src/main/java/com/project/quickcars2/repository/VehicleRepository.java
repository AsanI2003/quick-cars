package com.project.quickcars2.repository;


import com.project.quickcars2.entity.DriveType;
import com.project.quickcars2.entity.FuelType;
import com.project.quickcars2.entity.Vehicle;

import com.project.quickcars2.entity.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface VehicleRepository extends JpaRepository<Vehicle,Long> {
    @Query(value = "SELECT * FROM vehicle WHERE availability = 1 LIMIT 8", nativeQuery = true)
    List<Vehicle> gettop8Vehicles();

    @Query(value = "SELECT * FROM vehicle WHERE availability = 1", nativeQuery = true)
    List<Vehicle> findAllvehicles();

    @Query("SELECT v FROM Vehicle v WHERE " +
            "(:type IS NULL OR v.vehicleType = :type) AND " +
            "(:fuelType IS NULL OR v.fuelType = :fuelType) AND " +
            "(:driveType IS NULL OR v.driveType = :driveType) AND " +
            "v.availability = 1")
    List<Vehicle> filterVehicles(VehicleType type, FuelType fuelType, DriveType driveType);





}
