package com.example.BeautServices.services;

import com.example.BeautServices.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService{

    private final ClientRepository clientRepository;
    @Autowired
    public UserServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public UserDetailsService userDetailsService() {
        return username ->  clientRepository.findByEmail(username).orElseThrow(
                ()-> new UsernameNotFoundException("User is not found")
        );
    }

    @Override
    public void updateLastActiveUser(String username) {
        clientRepository.findByEmail(username).ifPresent(user-> {
            user.setLastActive(LocalDateTime.now()); //update last user login
            clientRepository.save(user);
        });
    }
}
