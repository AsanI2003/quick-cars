package com.project.quickcars2.service.impl;


import com.project.quickcars2.dto.VehicleDto;
import com.project.quickcars2.entity.DriveType;
import com.project.quickcars2.entity.FuelType;
import com.project.quickcars2.entity.VehicleType;
import com.project.quickcars2.repository.VehicleRepository;
import com.project.quickcars2.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final ModelMapper modelMapper;



    @Override
    public List<VehicleDto> gettop8Vehicles() {
        return vehicleRepository.gettop8Vehicles().stream()
                .map(vehicle -> modelMapper.map(vehicle, VehicleDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<VehicleDto> getallVehicles() {
        return vehicleRepository.findAllvehicles().stream()
                .map(vehicle -> modelMapper.map(vehicle, VehicleDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<VehicleDto> filterVehicles(VehicleType vehicleType, FuelType fuelType, DriveType driveType) {
        return vehicleRepository.filterVehicles(vehicleType, fuelType,driveType).stream()
                .map(vehicle -> modelMapper.map(vehicle, VehicleDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isVehicleAvailable(Long vehicleId) {
        return vehicleRepository.findById(vehicleId)
                .map(vehicle -> vehicle.getAvailability() == 1)
                .orElse(false);
    }


}
