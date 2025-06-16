package com.example.BeautServices.services;

import com.example.BeautServices.apiresponse.ApiResponse;
import com.example.BeautServices.dto.BookingDto;
import com.example.BeautServices.dto.BookingResponseDto;
import com.example.BeautServices.dto.BookingSummaryDto;

import java.util.List;

public interface BookingService {

    ApiResponse<?> createBooking(BookingDto bookingDto);
    ApiResponse<?> getAllBookings();
    List<BookingSummaryDto> getMyBookings(String clientEmail);
    ApiResponse<?> confirmBookingAtReception(Long bookingId, String pin);
    ApiResponse<?> getBookingById(Long bookingId);
    ApiResponse<?> assignRoomToBooking(Long bookingId, Long roomId);
    ApiResponse<?> markAsComplete(Long bookingId);
    ApiResponse<?> markAsInService(Long bookingId);

    ApiResponse<List<BookingResponseDto>> getConfirmedBookings();

    ApiResponse<List<BookingResponseDto>> getInServiceBookings();

    ApiResponse<List<BookingResponseDto>> getCompletedBookings();
    ApiResponse<List<BookingResponseDto>> getReservedServiceBookings();

}
