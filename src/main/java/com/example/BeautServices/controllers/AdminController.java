package com.example.BeautServices.controllers;

import com.example.BeautServices.apiresponse.ApiResponse;
import com.example.BeautServices.dto.ProfileDto;
import com.example.BeautServices.dto.RegisterDto;
import com.example.BeautServices.services.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@AllArgsConstructor
@RestController
@RequestMapping("/api/auth/admin")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/add-receptionist")
    public ResponseEntity<ApiResponse<String>> addReceptionist(@RequestBody RegisterDto dto){
        return ResponseEntity.ok(adminService.createReceptionist(dto));
    }

    @GetMapping("/receptionist/{id}")
    public ResponseEntity<ApiResponse<ProfileDto>> getReceptionist(@PathVariable Long id){
        return ResponseEntity.ok(adminService.getReceptionist(id));
    }

    @PostMapping("/deactivate-account/{id}")
    public ResponseEntity<ApiResponse<String>> deactivateAccount(@PathVariable Long id){
        return ResponseEntity.ok(adminService.deactivateAccount(id));
    }

    @PostMapping("/activate-account/{id}")
    public ResponseEntity<ApiResponse<String>> activateAccount(@PathVariable Long id){
        return ResponseEntity.ok(adminService.activateAccount(id));
    }

    @GetMapping("/all-customers")
    public ResponseEntity<ApiResponse<List<ProfileDto>>> getAllCustomer(){
        return ResponseEntity.ok(adminService.getCustomers());
    }

    @GetMapping("/all-receptionist")
    public ResponseEntity<ApiResponse<List<ProfileDto>>> getAllReceptionist(){
        return ResponseEntity.ok(adminService.getAllReceptionist());
    }


}
