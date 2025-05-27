package com.example.BeautServices.services;

import com.example.BeautServices.apiresponse.ApiResponse;
import com.example.BeautServices.dto.ProfileDto;
import com.example.BeautServices.entity.Customer;
import com.example.BeautServices.exceptions.UserNotFoundException;
import com.example.BeautServices.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public ApiResponse<ProfileDto> getProfile(String email) {

        Optional<Customer> customer = customerRepository.findByEmail(email);
        if (customer.isPresent()) {
            return buildProfileResponse(customer.get());
        }

        throw new UserNotFoundException("User is not found");
    }

    private ApiResponse<ProfileDto> buildProfileResponse(Customer customer) {
        ProfileDto dto = new ProfileDto();
        dto.setFullName(customer.getFullName());
        dto.setEmail(customer.getEmail());
        dto.setId(customer.getId());
        dto.setPhone(customer.getPhone());
        return new ApiResponse<>(200, "success", dto);
    }
}