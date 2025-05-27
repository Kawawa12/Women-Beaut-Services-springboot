package com.example.BeautServices.services;

import com.example.BeautServices.apiresponse.ApiResponse;
import com.example.BeautServices.apiresponse.ReceptionistResponseDto;
import com.example.BeautServices.dto.ProfileDto;
import com.example.BeautServices.dto.RegisterDto;
import com.example.BeautServices.entity.Customer;
import com.example.BeautServices.entity.Role;
import com.example.BeautServices.exceptions.UnexpectedException;
import com.example.BeautServices.exceptions.UserAlreadyExistsException;
import com.example.BeautServices.exceptions.UserNotFoundException;
import com.example.BeautServices.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminServiceImpl(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ApiResponse<String> createReceptionist(RegisterDto dto) {
        try {
            // Check if receptionist exists with same email
            if (customerRepository.existsByEmail(dto.getEmail())) {
                throw new UserAlreadyExistsException("User already exists with the same email!");
            }
            // Create receptionist
            Customer receptionist = new Customer();
            receptionist.setFullName(dto.getFullName());
            receptionist.setEmail(dto.getEmail());
            receptionist.setAddress(dto.getAddress());
            receptionist.setPhone(dto.getPhone());
            receptionist.setActive(true);
            receptionist.setRole(Role.RECEPTIONIST);
            receptionist.setPassword(passwordEncoder.encode(dto.getPassword()));
            customerRepository.save(receptionist);

            return new ApiResponse<>(201, "Receptionist created successfully.", null);
        }
        catch (UserAlreadyExistsException e) {
            // Re-throw to ensure the correct error message is shown
            throw e;
        }
        catch (Exception e) {
            // Only catch unexpected errors here
            throw new UnexpectedException("Image processing failed: " + e.getMessage());
        }
    }



    @Override
    public ApiResponse<String> deactivateAccount(Long id) {

        try{
            Optional<Customer> optionalReceptionist = customerRepository.findById(id);
            if(optionalReceptionist.isPresent()){
                Customer receptionist = optionalReceptionist.get();
                receptionist.setActive(false);
                customerRepository.save(receptionist);
            }
            if(optionalReceptionist.isEmpty()){
                throw new UserNotFoundException("User is not found!");
            }
            return new ApiResponse<>(201, "Account deactivated successfully!", null);

        }catch(UserNotFoundException e){
            throw e;
        }catch(Exception e){
            throw new UnexpectedException(e.getMessage());
        }

    }

    //Activate account
    @Override
    public ApiResponse<String> activateAccount(Long id) {

        try{
            Optional<Customer> optionalReceptionist = customerRepository.findById(id);
            if(optionalReceptionist.isPresent()){
                Customer receptionist = optionalReceptionist.get();
                receptionist.setActive(true);
                customerRepository.save(receptionist);
            }
            if(optionalReceptionist.isEmpty()){
                throw new UserNotFoundException("User is not found!");
            }
            return new ApiResponse<>(201, "Account activated successfully!", null);

        }catch(UserNotFoundException e){
            throw e;
        }catch(Exception e){
            throw new UnexpectedException(e.getMessage());
        }
    }

    private ReceptionistResponseDto mapReceptionToDto(Customer receptionist) {
        ReceptionistResponseDto responseDto = new ReceptionistResponseDto();
        responseDto.setFullName(receptionist.getFullName());
        responseDto.setEmail(receptionist.getEmail());
        responseDto.setAddress(receptionist.getAddress());
        responseDto.setPhone(receptionist.getPhone());
        return responseDto;
    }

    public ApiResponse<List<ProfileDto>> getCustomers() {

        try {
            List<Customer> customers = customerRepository.findByRole(Role.CUSTOMER);

            if (customers.isEmpty()) {
                throw new UserNotFoundException("No customer is found!");
            }
            List<ProfileDto> userList = new ArrayList<>();
            for(Customer user:customers){
                ProfileDto dto = new ProfileDto();
                dto.setId(user.getId());
                dto.setFullName(user.getFullName());
                dto.setEmail(user.getEmail());
                dto.setAddress(user.getAddress());
                dto.setPhone(user.getPhone());
                userList.add(dto);
            }

            return new ApiResponse<>(200, "Success", userList);

        } catch (UserNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new UnexpectedException("Unexpected error occurred: " + ex.getMessage());
        }
    }

    public ApiResponse<ProfileDto> getReceptionist(Long id){
        return new ApiResponse<>(200, "success", new Customer().getCustomerDto());
    }

    public ApiResponse<List<ProfileDto>> getAllReceptionist(){

        try{
            List<Customer> customers = customerRepository.findByRole(Role.RECEPTIONIST);

            if(customers.isEmpty()){
                throw new UserNotFoundException("No Receptionist is found");
            }
            List<ProfileDto> userList = new ArrayList<>();
            for(Customer user:customers){
                ProfileDto dto = new ProfileDto();
                dto.setId(user.getId());
                dto.setFullName(user.getFullName());
                dto.setEmail(user.getEmail());
                dto.setAddress(user.getAddress());
                dto.setPhone(user.getPhone());
                dto.setActive(user.isActive());
                userList.add(dto);
            }

            return new ApiResponse<>(200, "success", userList);

        }catch(UserNotFoundException ex){
            throw  ex;
        }catch (Exception ex){
            throw  new UnexpectedException("Unexpected error occurred: " + ex.getMessage());
        }
    }

}