package com.example.BeautServices.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryResponseDto {

    private Long id;
    private String name;
    private boolean active;
    private LocalDateTime createDate;
    private byte[] image;
    private String description;
}
