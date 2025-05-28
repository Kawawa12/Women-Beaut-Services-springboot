package com.example.BeautServices.dto;

import lombok.Data;

import java.time.LocalTime;

@Data
public class BeautTimeSlotDto {
    private LocalTime startTime;
    private LocalTime endTime;
}
