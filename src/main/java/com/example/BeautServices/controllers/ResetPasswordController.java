package com.example.BeautServices.controllers;

import com.example.BeautServices.apiresponse.ApiResponse;
import com.example.BeautServices.dto.PasswordResetRequest;
import com.example.BeautServices.services.ResetPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/password")
public class ResetPasswordController {

    private final ResetPasswordService resetPasswordService;

    @Autowired
    public ResetPasswordController(ResetPasswordService resetPasswordService) {
        this.resetPasswordService = resetPasswordService;
    }

    @PostMapping("/reset-token")
    public ResponseEntity<ApiResponse<String>> resetPasswordToken(@RequestParam String email){
        ApiResponse<String> response = resetPasswordService.resetPasswordToken(email);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<String>> resetPassword(@RequestBody PasswordResetRequest resetRequest){
        ApiResponse<String> response = resetPasswordService.resetPassword(resetRequest);
        return ResponseEntity.ok(response);
    }
}
