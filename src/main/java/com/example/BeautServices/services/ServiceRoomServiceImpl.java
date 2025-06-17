package com.example.BeautServices.services;

import com.example.BeautServices.apiresponse.ApiResponse;
import com.example.BeautServices.dto.RooomsResponseDto;
import com.example.BeautServices.dto.ServiceRoomDto;
import com.example.BeautServices.entity.Booking;
import com.example.BeautServices.entity.BookingStatus;
import com.example.BeautServices.entity.ServiceRoom;
import com.example.BeautServices.exceptions.ResourceNotFoundException;
import com.example.BeautServices.repository.BookingRepository;
import com.example.BeautServices.repository.ServiceRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceRoomServiceImpl implements ServiceRoomService {

    private final ServiceRoomRepository serviceRoomRepository;
    private final BookingRepository bookingRepository;

    @Override
    public ApiResponse<?> createRoom(ServiceRoomDto roomDto) {
        if (serviceRoomRepository.existsByRoomNo(roomDto.getRoomNo())) {
            return new ApiResponse<>(400, "Room number already exists.", null);
        }

        ServiceRoom room = new ServiceRoom();
        room.setRoomNo(roomDto.getRoomNo());
        room.setServiceName(roomDto.getServiceName());
        room.setCapacity(roomDto.getCapacity());
        room.setCurrentClientInService(0);

        ServiceRoom saved = serviceRoomRepository.save(room);
        return new ApiResponse<>(201, "Room created successfully.", saved);
    }

    /**
     * Returns a filtered list of rooms without booking details
     */
    @Override
    public ApiResponse<List<RooomsResponseDto>> getAllRooms() {
        List<RooomsResponseDto> dtos = serviceRoomRepository.findAll().stream()
                .map(r -> {
                    RooomsResponseDto dto = new RooomsResponseDto();
                    dto.setRoomNo(r.getRoomNo());
                    dto.setServiceName(r.getServiceName());
                    dto.setCapacity(r.getCapacity());
                    dto.setCurrentClientInService(r.getCurrentClientInService());
                    return dto;
                })
                .collect(Collectors.toList());

        return new ApiResponse<>(200, "Rooms fetched successfully.", dtos);
    }

    @Override
    public ServiceRoom getRoomById(Long id) {
        return serviceRoomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));
    }

    @Override
    @Transactional
    public ApiResponse<?> assignBookingToRoom(Long bookingId, Long roomId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found for ID: " + bookingId));

        ServiceRoom room = serviceRoomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found for ID: " + roomId));

        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(),
                    "Booking must be confirmed before assigning to a room.", null);
        }

        if (room.getCurrentClientInService() >= room.getCapacity()) {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(),
                    "Selected room is at full capacity.", null);
        }

        room.setCurrentClientInService(room.getCurrentClientInService() + 1);
        serviceRoomRepository.save(room);

        booking.setServiceRoom(room);
        bookingRepository.save(booking);

        return new ApiResponse<>(HttpStatus.OK.value(),
                "Booking assigned to room ID " + roomId + " successfully.", null);
    }


}