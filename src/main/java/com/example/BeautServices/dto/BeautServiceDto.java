package com.example.BeautServices.dto;

import com.example.BeautServices.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BeautServiceDto {
    private Long catId;
    private String name;
    private String description;
    private String price;
    private MultipartFile imageFile;

}
