package com.project.quickcars2.controller;


import com.project.quickcars2.dto.ReviewDto;
import com.project.quickcars2.dto.VehicleDto;
import com.project.quickcars2.service.ReviewService;
import com.project.quickcars2.service.UserService;
import com.project.quickcars2.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/home")
@RequiredArgsConstructor
@CrossOrigin


public class HomePageController {

    private final VehicleService vehicleService;
    private final ReviewService reviewService;
    private final UserService userService;

    @Value("${api.key}")
    private String apiKey;





    @GetMapping("/gettop8")
    public ResponseEntity<List<VehicleDto>> gettop8Vehicles(@RequestHeader("X-API-KEY") String key) {
        if (!apiKey.equals(key)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(vehicleService.gettop8Vehicles());
    }

    @GetMapping("/getallreviews")
    public ResponseEntity<List<ReviewDto>> getallReviews(@RequestHeader("X-API-KEY") String key) {
        if (!apiKey.equals(key)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(reviewService.getallReviews());
    }


    @GetMapping("/api/user/profile")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> saveUserProfile(OAuth2AuthenticationToken authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }


        Map<String, Object> response = new HashMap<>();
        Map<String, Object> attributes = authentication.getPrincipal().getAttributes();
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        String picture = (String) attributes.get("picture");

        userService.saveOrUpdateUser(email,name);
        reviewService.syncReviewerDetails(email, name, picture);

        response.put("email", email);
        response.put("name", name);
        response.put("picture", picture);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/auth/check")
    @ResponseBody
    public ResponseEntity<Void> checkAuthentication(OAuth2AuthenticationToken authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/vehicle/check/{id}")
    @ResponseBody
    public ResponseEntity<Void> checkVehicleAvailability(@PathVariable Long id, @RequestHeader("X-API-KEY") String key) {
        if (!apiKey.equals(key)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        boolean available = vehicleService.isVehicleAvailable(id);
        return available ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.GONE).build();
    }


}

