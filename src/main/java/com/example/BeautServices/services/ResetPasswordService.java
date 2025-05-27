package com.example.BeautServices.services;

import com.example.BeautServices.apiresponse.ApiResponse;
import com.example.BeautServices.dto.PasswordResetRequest;

public interface ResetPasswordService {

    ApiResponse<String> resetPasswordToken(String email);
    ApiResponse<String> resetPassword(PasswordResetRequest request);

}
