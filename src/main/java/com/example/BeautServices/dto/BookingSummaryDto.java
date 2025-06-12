package com.example.BeautServices.dto;


import com.example.BeautServices.entity.BookingStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record BookingSummaryDto(
        Long id,
        String serviceName,
        String timeRange,
        LocalDate bookingDate,
        String paymentMethod,
        Double paidAmount,
        String confirmationPin,
        LocalDateTime pinExpiry,
        boolean isExpired,
        BookingStatus status
) {}