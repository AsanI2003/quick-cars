package com.project.quickcars2.repository;

import com.project.quickcars2.entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Integer> {
    @Query("SELECT COUNT(r) > 0 FROM Rental r WHERE r.vehicleId = :vehicleId AND " +
            "(:pickupDate <= r.dropoffDate AND :dropoffDate >= r.pickupDate)")
    boolean isVehicleBookedBetween(Long vehicleId, LocalDate pickupDate, LocalDate dropoffDate);

    List<Rental> findByVehicleId(int vehicleId);

    List<Rental> findByCustomerEmail(String customerEmail);

    @Query("SELECT r FROM Rental r WHERE r.status = :status AND YEAR(r.createdAt) = :year AND MONTH(r.createdAt) = :month")
    List<Rental> findByStatusAndMonth(@Param("status") String status, @Param("year") int year, @Param("month") int month);




}
