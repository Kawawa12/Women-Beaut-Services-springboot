package com.example.BeautServices.controllers;

import com.example.BeautServices.apiresponse.ApiResponse;
import com.example.BeautServices.apiresponse.LoginResponse;
import com.example.BeautServices.dto.LoginDto;
import com.example.BeautServices.dto.RegisterDto;
import com.example.BeautServices.dto.UserInfoResponse;
import com.example.BeautServices.entity.Customer;
import com.example.BeautServices.repository.CustomerRepository;
import com.example.BeautServices.services.AuthService;
import com.example.BeautServices.services.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final CustomerRepository customerRepository;

    @GetMapping("/online-users")
    public ResponseEntity<Long> countOnlineUsers(){
        return ResponseEntity.ok(authService.countCurrentlyActiveUsers());
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @RequestBody LoginDto loginDto,
            HttpServletResponse response) {

        ApiResponse<LoginResponse> loginResult = authService.loginUser(loginDto, response);

        if (loginResult.getData() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(401, "Login failed", null));
        }

        return ResponseEntity.ok(loginResult);
    }



    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> signup(@RequestBody RegisterDto registerDto) {
        ApiResponse<String> response = authService.registerUser(registerDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserInfoResponse>> getCurrentUser(
            @CookieValue(value = "jwt", required = false) String jwtToken
    ) {
        if (jwtToken == null || jwtToken.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(401, "Unauthorized: No token found", null));
        }

        String email = jwtService.extractUsername(jwtToken);
        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(401, "Invalid token", null));
        }

        Customer user = customerRepository.findByEmail(email).orElse(null);
        if (user == null || !jwtService.isTokenValid(jwtToken, user)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(401, "Invalid or expired token", null));
        }

        return ResponseEntity.ok(
                new ApiResponse<>(200, "Authenticated", new UserInfoResponse(user.getEmail(), user.getRole().name()))
        );
    }


    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie jwtCookie = new Cookie("jwt", null);
        jwtCookie.setMaxAge(0);
        jwtCookie.setPath("/");

        Cookie refreshCookie = new Cookie("refreshToken", null);
        refreshCookie.setMaxAge(0);
        refreshCookie.setPath("/");

        response.addCookie(jwtCookie);
        response.addCookie(refreshCookie);

        return ResponseEntity.ok(new ApiResponse<>(200, "Logged out successfully", null));
    }


}
