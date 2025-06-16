package com.example.BeautServices.services;

import com.example.BeautServices.apiresponse.ApiResponse;
import com.example.BeautServices.dto.BookingDto;
import com.example.BeautServices.dto.BookingResponseDto;
import com.example.BeautServices.dto.BookingSummaryDto;
import com.example.BeautServices.dto.ServiceRoomDto;
import com.example.BeautServices.entity.*;
import com.example.BeautServices.exceptions.ResourceNotFoundException;
import com.example.BeautServices.exceptions.UserNotFoundException;
import com.example.BeautServices.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BeautServiceRepository beautServiceRepository;
    private final BeautTimeSlotRepository beautTimeSlotRepository;
    private final ClientRepository clientRepository;
    private final ServiceRoomRepository serviceRoomRepository;

    private static final int MAX_BOOKINGS_PER_SLOT = 5;

    @Override
    @Transactional
    public ApiResponse<?> createBooking(BookingDto bookingDto) {
        long currentCount = bookingRepository
                .countByServiceIdAndTimeSlotIdAndBookingDate(
                        bookingDto.getServiceId(),
                        bookingDto.getSlotId(),
                        bookingDto.getDate()
                );

        if (currentCount >= MAX_BOOKINGS_PER_SLOT) {
            return new ApiResponse<>(
                    HttpStatus.BAD_REQUEST.value(),
                    "This time slot is fully booked for this service.",
                    null
            );
        }

        Client client = clientRepository.findByEmail(bookingDto.getEmail())
                .orElseThrow(() -> new UserNotFoundException("Client not found"));

        boolean alreadyBooked = bookingRepository
                .existsByClientIdAndServiceIdAndTimeSlotIdAndBookingDate(
                        client.getId(),
                        bookingDto.getServiceId(),
                        bookingDto.getSlotId(),
                        bookingDto.getDate()
                );

        if (alreadyBooked) {
            return new ApiResponse<>(
                    HttpStatus.BAD_REQUEST.value(),
                    "You have already booked this service at this time slot on this date.",
                    null
            );
        }

        BeautService service = beautServiceRepository.findById(bookingDto.getServiceId())
                .orElseThrow(() -> new ResourceNotFoundException("No service found"));

        TimeSlot slot = beautTimeSlotRepository.findById(bookingDto.getSlotId())
                .orElseThrow(() -> new ResourceNotFoundException("No time slot found"));

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

        Booking saved = bookingRepository.save(booking);

        return new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "Booking successfully created",
                "Confirmation PIN: " + saved.getConfirmationPin()
        );
    }

    private String generatePin() {
        return String.format("%06d", new Random().nextInt(1_000_000));
    }

    @Override
    public ApiResponse<?> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        List<BookingResponseDto> dtos = bookings.stream()
                .map(this::mapToDto)
                .toList();

        return new ApiResponse<>(
                HttpStatus.OK.value(),
                "All bookings fetched successfully",
                dtos
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingSummaryDto> getMyBookings(String clientEmail) {
        List<Booking> bookings = bookingRepository.findAllByClientEmail(clientEmail);
        return bookings.stream().map(b -> new BookingSummaryDto(
                b.getId(),
                b.getService().getName(),
                b.getTimeSlot().getTimeRange(),
                b.getBookingDate(),
                b.getPaymentMethod(),
                b.getPaidAmount(),
                b.getConfirmationPin(),
                b.getPinExpiry(),
                b.getPinExpiry().isBefore(LocalDateTime.now()),
                b.getStatus()
        )).toList();
    }

    @Override
    @Transactional
    public ApiResponse<?> assignRoomToBooking(Long bookingId, Long roomId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found for ID: " + bookingId));

        ServiceRoom room = serviceRoomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found for ID: " + roomId));

        if (room.getCurrentClientInService() >= room.getCapacity()) {
            return new ApiResponse<>(
                    HttpStatus.BAD_REQUEST.value(),
                    "Selected room is at full capacity.",
                    null
            );
        }

        if (!room.getServiceName().equals(booking.getService().getName())) {
            return new ApiResponse<>(
                    HttpStatus.BAD_REQUEST.value(),
                    "Room service does not match booking service.",
                    null
            );
        }

        room.setCurrentClientInService(room.getCurrentClientInService() + 1);
        serviceRoomRepository.save(room);

        booking.setServiceRoom(room);
        Booking updated = bookingRepository.save(booking);

        return new ApiResponse<>(
                HttpStatus.OK.value(),
                "Room assigned successfully.",
                mapToDto(updated)
        );
    }

    @Override
    @Transactional
    public ApiResponse<?> confirmBookingAtReception(Long bookingId, String pin) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found for ID: " + bookingId));

        if (!booking.getConfirmationPin().equals(pin)) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Invalid PIN.", null);
        }
        if (booking.getPinExpiry().isBefore(LocalDateTime.now())) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "PIN has expired.", null);
        }
        if (booking.getStatus() == BookingStatus.CONFIRMED) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Already confirmed.", null);
        }

        booking.setStatus(BookingStatus.CONFIRMED);
        Booking saved = bookingRepository.save(booking);

        return new ApiResponse<>(
                HttpStatus.OK.value(),
                "Booking confirmed successfully.",
                mapToDto(saved)
        );
    }

    @Transactional(readOnly = true)
    public ApiResponse<?> getBookingById(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found for ID: " + bookingId));
        return new ApiResponse<>(HttpStatus.OK.value(), "Booking fetched successfully", mapToDto(booking));
    }

    @Override
    @Transactional
    public ApiResponse<?> markAsInService(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found for ID: " + bookingId));

        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            return new ApiResponse<>(
                    HttpStatus.BAD_REQUEST.value(),
                    "Booking must be CONFIRMED before marking as IN_SERVICE.",
                    null
            );
        }

        booking.setStatus(BookingStatus.IN_SERVICE);
        Booking saved = bookingRepository.save(booking);
        return new ApiResponse<>(HttpStatus.OK.value(), "Marked as in service.", mapToDto(saved));
    }

    @Override
    @Transactional
    public ApiResponse<?> markAsComplete(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found for ID: " + bookingId));

        if (booking.getStatus() != BookingStatus.CONFIRMED && booking.getStatus() != BookingStatus.IN_SERVICE) {
            return new ApiResponse<>(
                    HttpStatus.BAD_REQUEST.value(),
                    "Booking must be CONFIRMED or IN_SERVICE before marking as COMPLETED.",
                    null
            );
        }

        booking.setStatus(BookingStatus.COMPLETED);

        ServiceRoom room = booking.getServiceRoom();
        if (room != null) {
            room.setCurrentClientInService(room.getCurrentClientInService() - 1);
            serviceRoomRepository.save(room);
            booking.setServiceRoom(null);
        }

        Booking saved = bookingRepository.save(booking);
        return new ApiResponse<>(HttpStatus.OK.value(), "Marked as complete.", mapToDto(saved));
    }

    private BookingResponseDto mapToDto(Booking b) {
        BookingResponseDto dto = new BookingResponseDto();
        dto.setId(b.getId());
        dto.setClientName(b.getClient().getFullName());
        dto.setServiceName(b.getService().getName());
        dto.setTimeSlot(b.getTimeSlot().getTimeRange());
        dto.setBookingDate(b.getBookingDate().toString());
        dto.setPaymentMethod(b.getPaymentMethod());
        dto.setAmount(b.getPaidAmount());
        dto.setStatus(b.getStatus().name());
        dto.setConfirmationPin(b.getConfirmationPin());
        dto.setPinExpired(b.getPinExpiry().isBefore(LocalDateTime.now()));

        if (b.getServiceRoom() != null) {
            ServiceRoom sr = b.getServiceRoom();
            ServiceRoomDto roomDto = new ServiceRoomDto();
            roomDto.setRoomNo(sr.getRoomNo());
            roomDto.setServiceName(sr.getServiceName());
            roomDto.setCapacity(sr.getCapacity());
            dto.setServiceRoom(roomDto);
        }

        return dto;
    }

    private ApiResponse<List<BookingResponseDto>> getBookingsByStatus(BookingStatus status) {
        List<BookingResponseDto> dtos = bookingRepository.findAll().stream()
                .filter(b -> b.getStatus() == status)
                .map(this::mapToDto)
                .toList();

        return new ApiResponse<>(
                HttpStatus.OK.value(),
                status.name() + " bookings fetched successfully.",
                dtos
        );
    }

    @Override
    public ApiResponse<List<BookingResponseDto>> getConfirmedBookings() {
        return getBookingsByStatus(BookingStatus.CONFIRMED);
    }

    @Override
    public ApiResponse<List<BookingResponseDto>> getInServiceBookings() {
        return getBookingsByStatus(BookingStatus.IN_SERVICE);
    }

    @Override
    public ApiResponse<List<BookingResponseDto>> getReservedServiceBookings() {
        return getBookingsByStatus(BookingStatus.RESERVED);
    }

    @Override
    public ApiResponse<List<BookingResponseDto>> getCompletedBookings() {
        return getBookingsByStatus(BookingStatus.COMPLETED);
    }
}