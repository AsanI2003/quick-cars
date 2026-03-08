package com.project.quickcars2.service.impl;

import com.project.quickcars2.dto.RentalDto;
import com.project.quickcars2.entity.Rental;
import com.project.quickcars2.repository.RentalRepository;
import com.project.quickcars2.service.RentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService {

    private final RentalRepository rentalRepository;

    @Override
    public boolean isVehicleBookedBetween(Long vehicleId, LocalDate pickupDate, LocalDate dropoffDate) {
        return rentalRepository.isVehicleBookedBetween(vehicleId, pickupDate, dropoffDate);
    }

    @Override
    public void saveRental(RentalDto dto, OAuth2AuthenticationToken auth) {
        Rental rental = new Rental();
        rental.setCustomerEmail(auth.getPrincipal().getAttribute("email"));

        try {
            rental.setContactNumber(Integer.parseInt(dto.getContactNumber()));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid contact number format");
        }

        rental.setVehicleId(dto.getVehicleId());
        rental.setPickupDate(dto.getPickupDate());
        rental.setDropoffDate(dto.getDropoffDate());
        rental.setPickupLocation(dto.getPickupLocation());
        rental.setDropoffLocation(dto.getDropoffLocation());
        rental.setTotalPrice(dto.getTotalPrice());

        rentalRepository.save(rental);
    }

    @Override
    public List<LocalDate> getAvailableDates(int vehicleId) {
        List<Rental> rentals = rentalRepository.findByVehicleId(vehicleId);

        Set<LocalDate> booked = new HashSet<>();
        for (Rental r : rentals) {
            LocalDate date = r.getPickupDate();
            while (!date.isAfter(r.getDropoffDate())) {
                booked.add(date);
                date = date.plusDays(1);
            }
        }

        List<LocalDate> all = Stream.iterate(LocalDate.now(), d -> d.plusDays(1))
                .limit(90)
                .collect(Collectors.toList());

        return all.stream()
                .filter(d -> !booked.contains(d))
                .collect(Collectors.toList());
    }

}
