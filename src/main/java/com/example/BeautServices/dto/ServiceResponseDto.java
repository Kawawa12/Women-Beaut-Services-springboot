package com.example.BeautServices.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceResponseDto {

    private Long id;
    private String serviceName;
    private String categoryName;
    private String description;
    private String price;
    private String status;
    private boolean active;
    private byte[] image;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

}
