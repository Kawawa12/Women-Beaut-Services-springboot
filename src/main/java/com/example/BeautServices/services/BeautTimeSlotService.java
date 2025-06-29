package com.example.BeautServices.services;

import com.example.BeautServices.apiresponse.ApiResponse;
import com.example.BeautServices.dto.BeautTimeSlotDto;
import com.example.BeautServices.dto.BeautTimeSlotResponseDto;

import java.util.List;

public interface BeautTimeSlotService {

    ApiResponse<?> createTimeSlot(BeautTimeSlotDto dto);
    List<BeautTimeSlotResponseDto> getAllTimeSlots();
    ApiResponse<?> toggleTimeSlotStatus(Long id);
    ApiResponse<?> deleteTimeSlot(Long id);
    ApiResponse<?> updateTimeSlot(Long id, BeautTimeSlotDto dto);
}
