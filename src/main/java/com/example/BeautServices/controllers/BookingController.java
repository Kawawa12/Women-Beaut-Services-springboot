package com.example.BeautServices.controllers;

import com.example.BeautServices.apiresponse.ApiResponse;
import com.example.BeautServices.dto.BookingDto;
import com.example.BeautServices.dto.BookingResponseDto;
import com.example.BeautServices.services.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/create")
    public ResponseEntity<?> createBooking(@RequestBody BookingDto dto) {
        ApiResponse<?> response = bookingService.createBooking(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<?>> getAllBookings() {
        log.info("Fetching all bookings");
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @PostMapping("/confirm")
    public ResponseEntity<ApiResponse<?>> confirmBooking(
            @RequestParam("id") Long bookingId,
            @RequestParam("pin") String pin
    ) {
        ApiResponse<?> response = bookingService.confirmBookingAtReception(bookingId, pin);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getBooking(@PathVariable("id") Long bookingId) {
        ApiResponse<?> response = bookingService.getBookingById(bookingId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/mark-complete")
    public ResponseEntity<ApiResponse<?>> markAsComplete(
            @RequestParam("id") Long bookingId
    ) {
        log.info("Marking booking {} as complete", bookingId);
        ApiResponse<?> response = bookingService.markAsComplete(bookingId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/mark-in-service")
    public ResponseEntity<ApiResponse<?>> markAsInService(
            @RequestParam("id") Long bookingId
    ) {
        log.info("Marking booking {} as in service", bookingId);
        ApiResponse<?> response = bookingService.markAsInService(bookingId);
        return ResponseEntity.ok(response);
    }

    // === New endpoint to assign a room to a booking ===
    @PostMapping("/assign-room")
    public ResponseEntity<ApiResponse<?>> assignRoom(
            @RequestParam("bookingId") Long bookingId,
            @RequestParam("roomId") Long roomId
    ) {
        log.info("Assigning room {} to booking {}", roomId, bookingId);
        ApiResponse<?> response = bookingService.assignRoomToBooking(bookingId, roomId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/confirmed")
    public ResponseEntity<ApiResponse<List<BookingResponseDto>>> confirmed() {
        return ResponseEntity.ok(bookingService.getConfirmedBookings());
    }

    @GetMapping("/reserved")
    public ResponseEntity<ApiResponse<List<BookingResponseDto>>> reserved() {
        return ResponseEntity.ok(bookingService.getReservedServiceBookings());
    }

    @GetMapping("/in-service")
    public ResponseEntity<ApiResponse<List<BookingResponseDto>>> inService() {
        return ResponseEntity.ok(bookingService.getInServiceBookings());
    }

    @GetMapping("/completed")
    public ResponseEntity<ApiResponse<List<BookingResponseDto>>> completed() {
        return ResponseEntity.ok(bookingService.getCompletedBookings());
    }

}