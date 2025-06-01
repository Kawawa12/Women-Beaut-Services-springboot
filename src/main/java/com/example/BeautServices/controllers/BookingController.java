package com.example.BeautServices.controllers;

import com.example.BeautServices.apiresponse.ApiResponse;
import com.example.BeautServices.dto.BookingDto;
import com.example.BeautServices.services.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/create")
    public ResponseEntity<?> createBooking(@RequestBody BookingDto dto) {
        log.info("Received booking request: {}", dto);
        log.info("serviceId = {}", dto.getServiceId());
        log.info("timeSlotId = {}", dto.getSlotId());
        log.info("date = {}", dto.getDate());
        log.info("email = {}", dto.getEmail());
        log.info("paymentMethod = {}", dto.getPaymentMethod());
        log.info("amount = {}", dto.getAmount());

        ApiResponse<?> response = bookingService.createBooking(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<?>> getAllBookings() {
        log.info("Fetching all bookings");
        return ResponseEntity.ok(bookingService.getAllBookings());
    }
}
