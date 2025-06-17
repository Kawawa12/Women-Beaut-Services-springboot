package com.example.BeautServices.services;

import com.example.BeautServices.apiresponse.ApiResponse;
import com.example.BeautServices.dto.BeautTimeSlotDto;
import com.example.BeautServices.dto.BeautTimeSlotResponseDto;
import com.example.BeautServices.entity.TimeSlot;
import com.example.BeautServices.exceptions.ResourceNotFoundException;
import com.example.BeautServices.repository.BeautTimeSlotRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class BeautTimeSlotServiceImpl implements BeautTimeSlotService{

    private final BeautTimeSlotRepository beautTimeSlotRepository;

    @Override
    public ApiResponse<?> createTimeSlot(BeautTimeSlotDto dto) {
        if (dto.getStartTime() == null || dto.getEndTime() == null) {
            throw new IllegalArgumentException("Start time and end time are required.");
        }

        if (!dto.getEndTime().isAfter(dto.getStartTime())) {
            throw new IllegalArgumentException("End time must be after start time.");
        }

        // 🔐 Prevent duplicates
        if (beautTimeSlotRepository.existsByStartTimeAndEndTime(dto.getStartTime(), dto.getEndTime())) {
            throw new ResourceNotFoundException("A time slot with the same start and end time already exists.");
        }

        TimeSlot slot = new TimeSlot();
        slot.setSlotName(dto.getStartTime() + " - " + dto.getEndTime()); // Optional
        slot.setStartTime(dto.getStartTime());
        slot.setEndTime(dto.getEndTime());
        slot.setCreatedAt(LocalDateTime.now());
        slot.setUpdatedAt(LocalDateTime.now());

        beautTimeSlotRepository.save(slot);
        return new ApiResponse<>(HttpStatus.CREATED.value(), "Time slot created successfully.", slot);
    }


    @Override
    public ApiResponse<?> updateTimeSlot(Long id, BeautTimeSlotDto dto) {
        TimeSlot existingSlot = beautTimeSlotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Time slot not found with id: " + id));

        if (dto.getStartTime() == null || dto.getEndTime() == null) {
            throw new IllegalArgumentException("Start time and end time are required.");
        }

        if (!dto.getEndTime().isAfter(dto.getStartTime())) {
            throw new IllegalArgumentException("End time must be after start time.");
        }

        // Optional: Check for duplicate excluding itself
        boolean exists = beautTimeSlotRepository.existsByStartTimeAndEndTime(dto.getStartTime(), dto.getEndTime());
        if (exists && (!existingSlot.getStartTime().equals(dto.getStartTime()) || !existingSlot.getEndTime().equals(dto.getEndTime()))) {
            throw new IllegalArgumentException("Another time slot with same start and end time exists.");
        }

        existingSlot.setStartTime(dto.getStartTime());
        existingSlot.setEndTime(dto.getEndTime());
        existingSlot.setSlotName(dto.getStartTime() + " - " + dto.getEndTime());
        existingSlot.setUpdatedAt(LocalDateTime.now());

        beautTimeSlotRepository.save(existingSlot);

        return new ApiResponse<>(200, "Time slot updated successfully", existingSlot);
    }

    @Override
    public ApiResponse<?> deleteTimeSlot(Long id) {
        TimeSlot slot = beautTimeSlotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Time slot not found with id: " + id));

        beautTimeSlotRepository.delete(slot);

        return new ApiResponse<>(200, "Time slot deleted successfully", null);
    }


    @Override
    public List<BeautTimeSlotResponseDto> getAllTimeSlots() {
       List<TimeSlot> timeSlots = beautTimeSlotRepository.findAll();
       if (timeSlots.isEmpty()){
           throw new ResourceNotFoundException("No time slot found!");
       }
       return  timeSlots.stream().map(this::mapTimeSlotToDto).toList();
    }

    private BeautTimeSlotResponseDto mapTimeSlotToDto(TimeSlot slot){
        BeautTimeSlotResponseDto responseDto = new BeautTimeSlotResponseDto();
        responseDto.setId(slot.getId());
        responseDto.setStartTime(slot.getStartTime());
        responseDto.setEndTime(slot.getEndTime());
        responseDto.setCreatedDate(slot.getCreatedAt());
        responseDto.setUpdatedDate(slot.getUpdatedAt());
        return responseDto;
    }

    public ApiResponse<?> toggleTimeSlotStatus(Long id) {
        TimeSlot slot = beautTimeSlotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Time slot not found with id: " + id));

        // Save the updated slot
        beautTimeSlotRepository.save(slot);

        return new ApiResponse<>(200, "Time slot availability toggled successfully", slot);
    }

}
