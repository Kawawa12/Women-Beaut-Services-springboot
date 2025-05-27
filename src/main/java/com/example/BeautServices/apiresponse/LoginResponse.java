package com.example.BeautServices.apiresponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse {

    private String jwtToken;
    private String refreshToken;
    private String role;

    public LoginResponse() {
    }

    public LoginResponse(String jwtToken, String role, String refreshToken) {
        this.jwtToken = jwtToken;
        this.role = role;
        this.refreshToken = refreshToken;
    }

    public LoginResponse(String email, String role) {
        this.jwtToken = email;
        this.role = role;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
