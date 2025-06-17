package com.example.BeautServices.controllers;

import com.example.BeautServices.dto.DashboardCountDto;
import com.example.BeautServices.services.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/counts")
    public ResponseEntity<DashboardCountDto> getDashboardCounts() {
        return ResponseEntity.ok(dashboardService.getDashboardCounts());
    }
}
