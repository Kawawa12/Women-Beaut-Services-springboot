package com.example.BeautServices.repository;

import com.example.BeautServices.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    long countByServiceIdAndTimeSlotIdAndBookingDate(Long serviceId, Long timeSlotId, LocalDate bookingDate);

    boolean existsByClientIdAndServiceIdAndTimeSlotIdAndBookingDate(
            Long clientId, Long serviceId, Long timeSlotId, LocalDate bookingDate
    );
    Optional<Booking> findByConfirmationPin(String pin);
    List<Booking> findByClientId(Long clientId);

    List<Booking> findAllByClientEmail(String email);


}
