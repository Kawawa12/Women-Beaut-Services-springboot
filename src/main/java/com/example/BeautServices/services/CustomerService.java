package com.example.BeautServices.services;

import com.example.BeautServices.apiresponse.ApiResponse;
import com.example.BeautServices.dto.ProfileDto;

public interface CustomerService {
    ApiResponse<ProfileDto> getProfile(String email);
}
