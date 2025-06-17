package com.example.BeautServices.controllers;

import com.example.BeautServices.apiresponse.ApiResponse;
import com.example.BeautServices.dto.BeautTimeSlotDto;
import com.example.BeautServices.exceptions.UnexpectedException;
import com.example.BeautServices.services.BeautTimeSlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/time-slots")
@RequiredArgsConstructor
public class BeautTimeSlotController {

    private final BeautTimeSlotService beautTimeSlotService;

    @PostMapping
    public ResponseEntity<?> createSlot(@RequestBody BeautTimeSlotDto dto) {
        try {
            return ResponseEntity.ok(beautTimeSlotService.createTimeSlot(dto));
        } catch (IllegalArgumentException e) {
             throw new UnexpectedException("Error, " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllSlots() {
        return ResponseEntity.ok(beautTimeSlotService.getAllTimeSlots());
    }

    @PostMapping("/{id}/toggle")
    public ResponseEntity<ApiResponse<?>> toggleTimeSlot(@PathVariable Long id){
        return ResponseEntity.ok(beautTimeSlotService.toggleTimeSlotStatus(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateSlot(
            @PathVariable Long id,
            @RequestBody BeautTimeSlotDto dto
    ) {
        try {
            return ResponseEntity.ok(beautTimeSlotService.updateTimeSlot(id, dto));
        } catch (IllegalArgumentException e) {
            throw new UnexpectedException("Error, " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteSlot(@PathVariable Long id) {
        return ResponseEntity.ok(beautTimeSlotService.deleteTimeSlot(id));
    }


}

