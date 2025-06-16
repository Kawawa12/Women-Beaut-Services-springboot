package com.example.BeautServices.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RooomsResponseDto {
    private int roomNo;
    private String serviceName;
    private int capacity;
    private int currentClientInService;
}
