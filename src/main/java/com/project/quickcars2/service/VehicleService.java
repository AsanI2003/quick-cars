package com.project.quickcars2.service;


import com.project.quickcars2.dto.VehicleDto;
import com.project.quickcars2.entity.DriveType;
import com.project.quickcars2.entity.FuelType;
import com.project.quickcars2.entity.VehicleType;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface VehicleService {
    List<VehicleDto> gettop8Vehicles();

    List<VehicleDto> getallVehicles();

    List<VehicleDto> filterVehicles(VehicleType vehicletype, FuelType fuelType, DriveType driveType);

    boolean isVehicleAvailable(Long vehicleId);



}
