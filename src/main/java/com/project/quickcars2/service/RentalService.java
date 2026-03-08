package com.project.quickcars2.service;

import com.project.quickcars2.dto.RentalDto;
import com.project.quickcars2.entity.Rental;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface RentalService {

    boolean isVehicleBookedBetween(Long vehicleId, LocalDate pickupDate, LocalDate dropoffDate);

    void saveRental(RentalDto dto, OAuth2AuthenticationToken auth);

    List<LocalDate> getAvailableDates(int vehicleId);


}
