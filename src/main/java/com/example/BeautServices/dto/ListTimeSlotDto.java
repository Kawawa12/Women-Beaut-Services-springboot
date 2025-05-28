package com.example.BeautServices.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListTimeSlotDto {

    private Long id;
    private String slotName;

}
