package com.example.BeautServices.services;

import com.example.BeautServices.apiresponse.ApiResponse;
import com.example.BeautServices.dto.BookingDto;
import com.example.BeautServices.dto.BookingSummaryDto;

import java.util.List;

public interface BookingService {

    ApiResponse<?> createBooking(BookingDto bookingDto);
    ApiResponse<?> getAllBookings();
    List<BookingSummaryDto> getMyBookings(String clientEmail);
}
