package com.example.BeautServices.controllers;

import com.example.BeautServices.apiresponse.ApiResponse;
import com.example.BeautServices.dto.BookingSummaryDto;
import com.example.BeautServices.dto.ProfileDto;
import com.example.BeautServices.services.BookingService;
import com.example.BeautServices.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    private final CustomerService customerService;
    private final BookingService bookingService; // ✅ Inject bookingService

    @Autowired
    public CustomerController(CustomerService customerService, BookingService bookingService) {
        this.customerService = customerService;
        this.bookingService = bookingService;
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<ProfileDto>> getCustomerProfile(Principal principal) {
        String email = principal.getName(); // ✅ Authenticated user's email
        ApiResponse<ProfileDto> response = customerService.getProfile(email);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-bookings")
    public ResponseEntity<List<BookingSummaryDto>> getMyBookings(Principal principal) {
        String email = principal.getName(); // ✅ Use the same Principal
        return ResponseEntity.ok(bookingService.getMyBookings(email));
    }


    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<ProfileDto>> updateProfile(@RequestBody ProfileDto profileDto, Principal principal) {
        String email = principal.getName(); // Authenticated email
        ApiResponse<ProfileDto> response = customerService.updateProfile(email, profileDto);
        return ResponseEntity.ok(response);
    }
}
