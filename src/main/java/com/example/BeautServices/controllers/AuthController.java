package com.example.BeautServices.controllers;

import com.example.BeautServices.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/online-users")
    public ResponseEntity<Long> countOnlineUsers(){
        return ResponseEntity.ok(authService.countCurrentlyActiveUsers());
    }
}
