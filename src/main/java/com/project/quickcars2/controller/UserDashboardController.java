package com.project.quickcars2.controller;

import com.project.quickcars2.dto.RentalDto;
import com.project.quickcars2.dto.ReviewDto;
import com.project.quickcars2.dto.UserDashboardRentalDto;
import com.project.quickcars2.entity.Rental;
import com.project.quickcars2.entity.Review;
import com.project.quickcars2.entity.Vehicle;
import com.project.quickcars2.repository.RentalRepository;
import com.project.quickcars2.repository.VehicleRepository;
import com.project.quickcars2.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/userdashboard")
@RequiredArgsConstructor
@CrossOrigin
public class UserDashboardController {

    private  final RentalRepository rentalRepository;
    private  final ReviewService reviewService;
    private final VehicleRepository vehicleRepository;

    @GetMapping("/inquiries")
    public List<UserDashboardRentalDto> getUserInquiries(OAuth2AuthenticationToken auth) {
        String email = auth.getPrincipal().getAttribute("email");
        List<Rental> rentals = rentalRepository.findByCustomerEmail(email);

        return rentals.stream().map(r -> {
            UserDashboardRentalDto dto = new UserDashboardRentalDto();
            Vehicle vehicle = vehicleRepository.findById((long) r.getVehicleId()).orElse(null);
            dto.setName(vehicle != null ? vehicle.getName() : "Unknown Vehicle");
            dto.setPickupDate(r.getPickupDate());
            dto.setPickupLocation(r.getPickupLocation());
            dto.setDropoffDate(r.getDropoffDate());
            dto.setDropoffLocation(r.getDropoffLocation());
            dto.setTotalPrice(r.getTotalPrice());
            dto.setStatus(r.getStatus());
            return dto;
        }).collect(Collectors.toList());
    }


    @PostMapping("/review")
    public ResponseEntity<Void> submitReview(@RequestBody ReviewDto dto, OAuth2AuthenticationToken auth) {

        if (auth == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Review review = new Review();
        review.setName(auth.getPrincipal().getAttribute("name"));
        review.setReviewerEmail(auth.getPrincipal().getAttribute("email"));
        review.setImageurl(auth.getPrincipal().getAttribute("picture"));
        review.setDescription(dto.getDescription());
        review.setRating(dto.getRating());



        reviewService.saveReview(review);
        return ResponseEntity.ok().build();
    }



}
