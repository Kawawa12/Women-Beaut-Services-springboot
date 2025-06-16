package com.example.BeautServices.services;

import com.example.BeautServices.apiresponse.ApiResponse;
import com.example.BeautServices.dto.RooomsResponseDto;
import com.example.BeautServices.dto.ServiceRoomDto;
import com.example.BeautServices.entity.ServiceRoom;

import java.util.List;

public interface ServiceRoomService {
    ApiResponse<?> createRoom(ServiceRoomDto roomDto);
    ApiResponse<List<RooomsResponseDto>> getAllRooms();

    ServiceRoom getRoomById(Long id);
    ApiResponse<?> assignBookingToRoom(Long bookingId, Long roomId);

}
