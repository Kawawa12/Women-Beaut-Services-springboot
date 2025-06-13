package com.example.BeautServices.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponseDto {
    private Long id;
    private String clientName;
    private String serviceName;
    private String timeSlot;         // e.g., "10:00 AM - 10:30 AM"
    private String bookingDate;      // formatted like "2025-06-01"
    private String paymentMethod;
    private double amount;
    private String status;           // PENDING, RESERVED, CONFIRMED
    private String confirmationPin;
    private boolean pinExpired;
}