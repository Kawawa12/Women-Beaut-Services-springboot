package com.example.BeautServices.services;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {
    UserDetailsService userDetailsService();
    void updateLastActiveUser(String username);
}
