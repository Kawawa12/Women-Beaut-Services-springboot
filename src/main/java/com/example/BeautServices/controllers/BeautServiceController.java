package com.example.BeautServices.controllers;

import com.example.BeautServices.apiresponse.ApiResponse;
import com.example.BeautServices.dto.BeautServiceDto;
import com.example.BeautServices.dto.ServiceResponseDto;
import com.example.BeautServices.services.BeautServiceService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@AllArgsConstructor
public class BeautServiceController {

    private final BeautServiceService beautServiceService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createService(
            @ModelAttribute BeautServiceDto serviceDto
    ) {
        ApiResponse<?> response = beautServiceService.createBeautService(serviceDto);
        return ResponseEntity.ok(response);
    }


    // Get all services
    @GetMapping
    public ResponseEntity<ApiResponse<List<ServiceResponseDto>>> getAllServices() {
        ApiResponse<List<ServiceResponseDto>> response = beautServiceService.getAllBeautService();
        return ResponseEntity.ok(response);
    }
}
