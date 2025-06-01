package com.example.BeautServices.services;

import com.example.BeautServices.apiresponse.ApiResponse;
import com.example.BeautServices.dto.BookingDto;

public interface BookingService {

    ApiResponse<?> createBooking(BookingDto bookingDto);
    ApiResponse<?> getAllBookings();
}
