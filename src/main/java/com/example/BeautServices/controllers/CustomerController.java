package com.example.BeautServices.controllers;

import com.example.BeautServices.apiresponse.ApiResponse;
import com.example.BeautServices.dto.ProfileDto;
import com.example.BeautServices.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/customer")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<ProfileDto>> getCustomerProfile(@RequestParam String email){
        ApiResponse<ProfileDto> response = customerService.getProfile(email);
        return ResponseEntity.ok(response);
    }
}
