package com.example.BeautServices.controllers;

import com.example.BeautServices.apiresponse.ApiResponse;
import com.example.BeautServices.dto.ServiceRoomDto;
import com.example.BeautServices.services.ServiceRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.example.BeautServices.dto.RooomsResponseDto;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class ServiceRoomController {

    private final ServiceRoomService serviceRoomService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createRoom(@RequestBody ServiceRoomDto dto) {
        return ResponseEntity.ok(serviceRoomService.createRoom(dto));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RooomsResponseDto>>> findAll() {
        return ResponseEntity.ok(serviceRoomService.getAllRooms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getRoomById(@PathVariable Long id) {
        // returning full entity or you can map to RooomsResponseDto similarly
        return ResponseEntity.ok(new ApiResponse<>(200, "Room fetched successfully", serviceRoomService.getRoomById(id)));
    }

    @PostMapping("/assign-booking")
    public ResponseEntity<ApiResponse<?>> assignBookingToRoom(
            @RequestParam("bookingId") Long bookingId,
            @RequestParam("roomId") Long roomId
    ) {
        return ResponseEntity.ok(serviceRoomService.assignBookingToRoom(bookingId, roomId));
    }
}