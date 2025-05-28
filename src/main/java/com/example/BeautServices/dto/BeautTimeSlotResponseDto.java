package com.example.BeautServices.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BeautTimeSlotResponseDto {
    private Long id;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private boolean available;

}
