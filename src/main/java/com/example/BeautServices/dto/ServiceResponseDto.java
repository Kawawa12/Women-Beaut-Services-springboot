package com.example.BeautServices.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceResponseDto {

    private String serviceName;
    private String categoryName;
    private String description;
    private List<ListTimeSlotDto> timeSlots;
    private String price;
    private boolean active;
    private byte[] image;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

}
