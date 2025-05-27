package com.example.BeautServices.services;

import com.example.BeautServices.apiresponse.ApiResponse;
import com.example.BeautServices.apiresponse.LoginResponse;
import com.example.BeautServices.dto.LoginDto;
import com.example.BeautServices.dto.RegisterDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

    long countCurrentlyActiveUsers();
    ApiResponse<String> registerUser(RegisterDto registerDto);
    public ApiResponse<LoginResponse> loginUser(LoginDto loginDto, HttpServletResponse httpResponse);

}
