package com.project.quickcars2.controller;

import com.project.quickcars2.dto.*;
import com.project.quickcars2.entity.Admin;
import com.project.quickcars2.entity.Rental;
import com.project.quickcars2.entity.Vehicle;
import com.project.quickcars2.repository.RentalRepository;
import com.project.quickcars2.repository.VehicleRepository;
import com.project.quickcars2.service.impl.AdminServiceImpl;
import com.project.quickcars2.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AdminController {

    private final AdminServiceImpl adminService;
    private final JwtUtil jwtUtil;
    private final VehicleRepository vehicleRepository;
    private final RentalRepository rentalRepository;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        if (adminService.findByUsername(request.getUsername()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }

        Admin admin = new Admin();
        admin.setUsername(request.getUsername());
        admin.setPassword(request.getPassword());
        adminService.save(admin);

        return ResponseEntity.ok("Admin registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Admin admin = adminService.findByUsername(request.getUsername());
        if (admin != null && adminService.checkPassword(request.getPassword(), admin.getPassword())) {
            String accessToken = jwtUtil.generateAccessToken(admin.getUsername());
            String refreshToken = jwtUtil.generateRefreshToken(admin.getUsername());

            return ResponseEntity.ok(Map.of(
                    "accessToken", accessToken,
                    "refreshToken", refreshToken
            ));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> payload) {
        String refreshToken = payload.get("refreshToken");
        if (jwtUtil.isTokenValid(refreshToken)) {
            String username = jwtUtil.extractUsername(refreshToken);
            String newAccessToken = jwtUtil.generateAccessToken(username);
            String newRefreshToken = jwtUtil.generateRefreshToken(username); // rotate
            return ResponseEntity.ok(Map.of(
                    "accessToken", newAccessToken,
                    "refreshToken", newRefreshToken
            ));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
    }


    @GetMapping("/dashboard/data")
    public ResponseEntity<?> getSecureData(Authentication auth) {
        String username = auth.getName();
        return ResponseEntity.ok("Hello " + username + ", this is secure admin data.");
    }

    @GetMapping("/dashboard/inquiries")
    public ResponseEntity<?> getAllInquiries() {
        List<Rental> rentals = rentalRepository.findAll();

        List<AdminRentalInquiryDto> result = rentals.stream().map(r -> {
            AdminRentalInquiryDto dto = new AdminRentalInquiryDto();
            dto.setId(r.getId());
            dto.setVehicleId(r.getVehicleId());
            dto.setContactNumber(String.valueOf(r.getContactNumber()));
            dto.setCreatedAt(r.getCreatedAt());
            dto.setPickupDate(r.getPickupDate());
            dto.setDropoffDate(r.getDropoffDate());
            dto.setPickupLocation(r.getPickupLocation());
            dto.setDropoffLocation(r.getDropoffLocation());
            dto.setTotalPrice(r.getTotalPrice());
            dto.setStatus(r.getStatus());


            return dto;
        }).toList();

        return ResponseEntity.ok(result);
    }

    @PutMapping("/dashboard/inquiries/{id}/status")
    public ResponseEntity<?> updateInquiryStatus(@PathVariable int id, @RequestBody Map<String, String> payload) {
        String newStatus = payload.get("status");
        Rental rental = rentalRepository.findById(id).orElse(null);

        if (rental == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Inquiry not found");
        }

        if (rental.getStatus().equalsIgnoreCase(newStatus)) {
            return ResponseEntity.ok("Status unchanged");
        }

        try {
            rental.setStatus(newStatus);
            rentalRepository.save(rental);
            return ResponseEntity.ok("Status updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update status");
        }
    }

    @PostMapping("/dashboard/vehicles")
    public ResponseEntity<?> saveVehicle(@Valid @RequestBody AdminVehiclesaveDto dto) {
        try {
            Vehicle vehicle = new Vehicle();
            vehicle.setImageurl(dto.getImageurl());
            vehicle.setName(dto.getName());
            vehicle.setCapacity(dto.getCapacity());
            vehicle.setFuelEconomy(dto.getFuelEconomy());
            vehicle.setDayPrice(Double.valueOf(dto.getDayPrice()));
            vehicle.setAvailability(dto.getAvailability());
            vehicle.setVehicleType(dto.getVehicleType());
            vehicle.setFuelType(dto.getFuelType());
            vehicle.setDriveType(dto.getDriveType());
            vehicle.setFuelCapacityL(dto.getFuelCapacityL());

            vehicleRepository.save(vehicle);
            return ResponseEntity.ok("Vehicle saved successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save vehicle");
        }
    }

    @GetMapping("/dashboard/vehicles")
    public ResponseEntity<?> getAllVehicles() {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        List<VehicleDto> result = vehicles.stream().map(v -> {
            VehicleDto dto = new VehicleDto();
            dto.setId(v.getId());
            dto.setImageurl(v.getImageurl());
            dto.setName(v.getName());
            dto.setCapacity(v.getCapacity());
            dto.setFuelEconomy(v.getFuelEconomy());
            dto.setDayPrice(v.getDayPrice());
            dto.setAvailability(v.getAvailability());
            dto.setVehicleType(v.getVehicleType());
            dto.setFuelType(v.getFuelType());
            dto.setDriveType(v.getDriveType());
            dto.setFuelCapacityL(v.getFuelCapacityL());
            return dto;
        }).toList();

        return ResponseEntity.ok(result);
    }

    @PutMapping("/dashboard/vehicles/{id}/price")
    public ResponseEntity<?> updateDayPrice(@PathVariable int id, @RequestBody Map<String, Integer> payload) {
        Integer newPrice = payload.get("dayPrice");
        if (newPrice == null || newPrice < 1000 || newPrice > 999999) {
            return ResponseEntity.badRequest().body("Invalid day price");
        }

        Vehicle vehicle = vehicleRepository.findById((long) id).orElse(null);
        if (vehicle == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vehicle not found");

        vehicle.setDayPrice(Double.valueOf(newPrice));
        vehicleRepository.save(vehicle);
        return ResponseEntity.ok("Day price updated");
    }

    @PutMapping("/dashboard/vehicles/{id}/availability")
    public ResponseEntity<?> updateAvailability(@PathVariable int id, @RequestBody Map<String, Integer> payload) {
        Integer availability = payload.get("availability");
        if (availability == null || (availability != 0 && availability != 1)) {
            return ResponseEntity.badRequest().body("Invalid availability value");
        }

        Vehicle vehicle = vehicleRepository.findById((long) id).orElse(null);
        if (vehicle == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vehicle not found");

        vehicle.setAvailability(availability);
        vehicleRepository.save(vehicle);
        return ResponseEntity.ok("Availability updated");
    }

    @GetMapping("/dashboard/inquiries/report/download")
    public ResponseEntity<byte[]> downloadMonthlyReport(@RequestParam int year, @RequestParam int month) {
        List<Rental> rentals = rentalRepository.findByStatusAndMonth("rental completed", year, month);

        StringBuilder csv = new StringBuilder("ID,Vehicle ID,Contact,Pickup,Dropoff,Locations,Price (LKR),Created At\n");
        for (Rental r : rentals) {
            csv.append(r.getId()).append(",")
                    .append(r.getVehicleId()).append(",")
                    .append(r.getContactNumber()).append(",")
                    .append(r.getPickupDate()).append(",")
                    .append(r.getDropoffDate()).append(",")
                    .append(r.getPickupLocation()).append(" → ").append(r.getDropoffLocation()).append(",")
                    .append(r.getTotalPrice()).append(",")
                    .append(r.getCreatedAt()).append("\n");
        }

        byte[] bytes = csv.toString().getBytes();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename("Completed_Inquiries_" + year + "_" + month + ".csv")
                .build());

        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }





}
