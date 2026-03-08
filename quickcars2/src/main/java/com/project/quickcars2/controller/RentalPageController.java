package com.project.quickcars2.controller;

import com.project.quickcars2.dto.RentalDto;
import com.project.quickcars2.entity.Rental;
import com.project.quickcars2.entity.Vehicle;
import com.project.quickcars2.repository.RentalRepository;
import com.project.quickcars2.repository.VehicleRepository;
import com.project.quickcars2.service.NotificationService;
import com.project.quickcars2.service.RentalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/rental")
@RequiredArgsConstructor
@CrossOrigin
public class RentalPageController {
    private final RentalService rentalService;
    private final NotificationService notificationService;
    private final VehicleRepository vehicleRepository;

    @Value("${api.key}")
    private String apiKey;


    @GetMapping("/check-availability")
    public ResponseEntity<Boolean> checkAvailability(
            @RequestHeader("X-API-KEY") String key,
            @RequestParam Long vehicleId,
            @RequestParam LocalDate pickupDate,
            @RequestParam LocalDate dropoffDate) {

        if (!apiKey.equals(key)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        boolean conflict = rentalService.isVehicleBookedBetween(vehicleId, pickupDate, dropoffDate);
        return ResponseEntity.ok(!conflict); // true = available
    }


    @PostMapping("/save")
    public ResponseEntity<Void> saveRental(@Valid @RequestBody RentalDto dto, OAuth2AuthenticationToken auth) {
        if (auth == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        rentalService.saveRental(dto, auth);

        String userEmail = auth.getPrincipal().getAttribute("email");


        Vehicle vehicle = vehicleRepository.findById((long) dto.getVehicleId()).orElse(null);
        if (vehicle == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // or custom error
        }

        String vehicleName = vehicle.getName();


        notificationService.sendRentalEmail(userEmail, vehicleName, dto.getPickupDate().toString(), dto.getDropoffDate().toString());
//        notificationService.sendRentalSms(dto.getContactNumber(), vehicleName, dto.getPickupDate().toString());

        return ResponseEntity.ok().build();
    }


    @GetMapping("/available-dates")
    public ResponseEntity<List<LocalDate>> getAvailableDates(
            @RequestHeader("X-API-KEY") String key,
            @RequestParam int vehicleId) {

        if (!apiKey.equals(key)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<LocalDate> availableDates = rentalService.getAvailableDates(vehicleId);
        return ResponseEntity.ok(availableDates);
    }






}
