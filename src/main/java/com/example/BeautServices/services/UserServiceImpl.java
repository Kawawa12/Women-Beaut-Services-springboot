package com.example.BeautServices.services;

import com.example.BeautServices.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService{

    private final CustomerRepository customerRepository;
    @Autowired
    public UserServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public UserDetailsService userDetailsService() {
        return username ->  customerRepository.findByEmail(username).orElseThrow(
                ()-> new UsernameNotFoundException("User is not found")
        );
    }

    @Override
    public void updateLastActiveUser(String username) {
        customerRepository.findByEmail(username).ifPresent(user-> {
            user.setLastActive(LocalDateTime.now()); //update last user login
            customerRepository.save(user);
        });
    }
}
