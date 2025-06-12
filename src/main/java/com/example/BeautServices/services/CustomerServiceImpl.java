package com.example.BeautServices.services;

import com.example.BeautServices.apiresponse.ApiResponse;
import com.example.BeautServices.dto.ProfileDto;
import com.example.BeautServices.entity.Client;
import com.example.BeautServices.exceptions.UserNotFoundException;
import com.example.BeautServices.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final ClientRepository clientRepository;
    @Autowired
    public CustomerServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public ApiResponse<ProfileDto> getProfile(String email) {

        Optional<Client> customer = clientRepository.findByEmail(email);
        if (customer.isPresent()) {
            return buildProfileResponse(customer.get());
        }

        throw new UserNotFoundException("User is not found");
    }

    private ApiResponse<ProfileDto> buildProfileResponse(Client client) {
        ProfileDto dto = new ProfileDto();
        dto.setFullName(client.getFullName());
        dto.setEmail(client.getEmail());
        dto.setId(client.getId());
        dto.setPhone(client.getPhone());
        return new ApiResponse<>(200, "success", dto);
    }

    //my bookings

}