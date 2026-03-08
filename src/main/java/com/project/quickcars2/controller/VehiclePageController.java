package com.project.quickcars2.controller;

import com.project.quickcars2.entity.DriveType;
import com.project.quickcars2.entity.FuelType;
import com.project.quickcars2.entity.VehicleType;
import com.project.quickcars2.dto.VehicleDto;
import com.project.quickcars2.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/vehicle")
@CrossOrigin
@RequiredArgsConstructor
public class VehiclePageController {
    private final VehicleService vehicleService;


    @Value("${api.key}")
    private String apiKey;





    @GetMapping("/getallvehicle")
    public ResponseEntity<List<VehicleDto>> getAllVehicles(@RequestHeader("X-API-KEY") String key) {
        if (!apiKey.equals(key)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(vehicleService.getallVehicles());
    }


    @GetMapping("/filtervehicles")
    public ResponseEntity<List<VehicleDto>> filterVehicles(
            @RequestHeader("X-API-KEY") String key,
            @RequestParam(required = false) VehicleType vehicletype,
            @RequestParam(required = false) FuelType fuelType,
            @RequestParam(required = false) DriveType driveType) {

        if (!apiKey.equals(key)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(vehicleService.filterVehicles(vehicletype, fuelType, driveType));
    }


    @GetMapping("/check/{vehicleId}")
    public ResponseEntity<Boolean> checkAvailability(
            @RequestHeader("X-API-KEY") String key,
            @PathVariable Long vehicleId) {

        if (!apiKey.equals(key)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        boolean available = vehicleService.isVehicleAvailable(vehicleId);
        return ResponseEntity.ok(available);
    }









}
