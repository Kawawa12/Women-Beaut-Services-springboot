package com.example.BeautServices.services;

import com.example.BeautServices.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthServiceImpl implements AuthService{

    private final CustomerRepository customerRepository;

    @Autowired
    public AuthServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public long countCurrentlyActiveUsers() {
        LocalDateTime activeThreshold = LocalDateTime.now().minusMinutes(3);
        return customerRepository.countByLastActiveAfter(activeThreshold);
    }
}
