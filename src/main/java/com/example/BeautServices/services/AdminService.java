package com.example.BeautServices.services;

import com.example.BeautServices.apiresponse.ApiResponse;
import com.example.BeautServices.dto.ProfileDto;
import com.example.BeautServices.dto.RegisterDto;

import java.util.List;

public interface AdminService {

    ApiResponse<String> createReceptionist(RegisterDto dto);

    ApiResponse<String> deactivateAccount(Long id);
    ApiResponse<String> activateAccount(Long id);
    ApiResponse<List<ProfileDto>> getCustomers();
    ApiResponse<List<ProfileDto>> getAllReceptionist();
    ApiResponse<ProfileDto> getReceptionist(Long id);
}
