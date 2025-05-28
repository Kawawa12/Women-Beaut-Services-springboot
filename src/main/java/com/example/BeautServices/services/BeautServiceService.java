package com.example.BeautServices.services;

import com.example.BeautServices.apiresponse.ApiResponse;
import com.example.BeautServices.dto.BeautServiceDto;
import com.example.BeautServices.dto.ServiceResponseDto;
import com.example.BeautServices.entity.BeautService;

import java.util.List;

public interface BeautServiceService {

    ApiResponse<BeautService> createBeautService(BeautServiceDto dto);
    ApiResponse<List<ServiceResponseDto>> getAllBeautService();
}
