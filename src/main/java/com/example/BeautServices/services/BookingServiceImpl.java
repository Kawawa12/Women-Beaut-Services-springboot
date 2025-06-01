package com.example.BeautServices.services;

import com.example.BeautServices.apiresponse.ApiResponse;
import com.example.BeautServices.dto.BookingDto;
import com.example.BeautServices.dto.BookingResponseDto;
import com.example.BeautServices.entity.*;
import com.example.BeautServices.exceptions.ResourceNotFoundException;
import com.example.BeautServices.exceptions.UserNotFoundException;
import com.example.BeautServices.repository.BeautServiceRepository;
import com.example.BeautServices.repository.BeautTimeSlotRepository;
import com.example.BeautServices.repository.BookingRepository;
import com.example.BeautServices.repository.ClientRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@AllArgsConstructor
public class BookingServiceImpl  implements BookingService{

    private BookingRepository bookingRepository;
    private final BeautServiceRepository beautServiceRepository;
    private final BeautTimeSlotRepository beautTimeSlotRepository;
    private final ClientRepository clientRepository;

    private static final int MAX_BOOKINGS_PER_SLOT = 5;


    @Transactional
    @Override
    public ApiResponse<?> createBooking(BookingDto bookingDto) {
        // 1. Check if the number of bookings for this service, slot, and date is already full
        long currentCount = bookingRepository.countByServiceIdAndTimeSlotIdAndBookingDate(
                bookingDto.getServiceId(),
                bookingDto.getSlotId(),
                bookingDto.getDate()
        );

        if (currentCount >= MAX_BOOKINGS_PER_SLOT) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(),
                    "This time slot is fully booked for this service.", null);
        }

        // 2. Find the client making the booking
        Client client = clientRepository.findByEmail(bookingDto.getEmail())
                .orElseThrow(() -> new UserNotFoundException("Client is not found!"));

        // 3. Check if the client has already booked this exact combination
        boolean alreadyBooked = bookingRepository.existsByClientIdAndServiceIdAndTimeSlotIdAndBookingDate(
                client.getId(),
                bookingDto.getServiceId(),
                bookingDto.getSlotId(),
                bookingDto.getDate()
        );

        if (alreadyBooked) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(),
                    "You have already booked this service at this time slot on this date.", null);
        }

        // 4. Fetch the service and the correct time slot
        BeautService service = beautServiceRepository.findById(bookingDto.getServiceId())
                .orElseThrow(() -> new ResourceNotFoundException("No service is found!"));

        TimeSlot slot = beautTimeSlotRepository.findById(bookingDto.getSlotId())
                .orElseThrow(() -> new ResourceNotFoundException("No time slot is found"));

        // 5. Create and save the new booking
        Booking booking = new Booking();
        booking.setService(service);
        booking.setTimeSlot(slot);
        booking.setClient(client);
        booking.setPaidAmount(bookingDto.getAmount());
        booking.setPaymentMethod(bookingDto.getPaymentMethod());
        booking.setBookingDate(bookingDto.getDate());
        booking.setConfirmationPin(generatePin());
        booking.setPinExpiry(bookingDto.getDate().atTime(23, 59));
        booking.setStatus(BookingStatus.RESERVED);

        bookingRepository.save(booking);

        return new ApiResponse<>(HttpStatus.CREATED.value(),
                "Booking successfully created", "Confirmation PIN: " + booking.getConfirmationPin());
    }


    private String generatePin() {
        return String.format("%06d", new Random().nextInt(1000000)); // 6-digit PIN
    }


    public Booking verifyBookingByPin(String pin) {
        Booking booking = bookingRepository.findByConfirmationPin(pin)
                .orElseThrow(() -> new RuntimeException("Invalid PIN"));

        if (booking.getPinExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("PIN has expired.");
        }

        booking.setStatus(BookingStatus.CONFIRMED);
        return bookingRepository.save(booking);
    }

    @Override
    public ApiResponse<?> getAllBookings() {
        var bookings = bookingRepository.findAll();

        var bookingDtos = bookings.stream()
                .map(this::mapToDto)
                .toList();

        return new ApiResponse<>(HttpStatus.OK.value(), "All bookings fetched successfully", bookingDtos);
    }


    private BookingResponseDto mapToDto(Booking booking) {
        return new BookingResponseDto(
                booking.getClient().getFullName(),
                booking.getService().getName(),
                booking.getTimeSlot().getTimeRange(), // or however you get time slot string
                booking.getBookingDate().toString(),
                booking.getPaymentMethod(),
                booking.getPaidAmount(),
                booking.getStatus().name(),
                booking.getConfirmationPin(),
                booking.getPinExpiry().isBefore(LocalDateTime.now())
        );
    }


}

