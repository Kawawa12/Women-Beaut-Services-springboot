package com.example.BeautServices.services;

import com.example.BeautServices.apiresponse.ApiResponse;
import com.example.BeautServices.apiresponse.LoginResponse;
import com.example.BeautServices.dto.LoginDto;
import com.example.BeautServices.dto.RegisterDto;
import com.example.BeautServices.entity.Customer;
import com.example.BeautServices.entity.Role;
import com.example.BeautServices.exceptions.NoActiveAccountException;
import com.example.BeautServices.exceptions.UnexpectedException;
import com.example.BeautServices.exceptions.UserAlreadyExistsException;
import com.example.BeautServices.repository.CustomerRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final CustomerRepository customerRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthServiceImpl(CustomerRepository customerRepository,
                           AuthenticationManager authenticationManager,
                           JwtService jwtService,
                           PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public long countCurrentlyActiveUsers() {
        return customerRepository.countByLastActiveAfter(LocalDateTime.now().minusMinutes(3));
    }

    @Override
    public ApiResponse<String> registerUser(RegisterDto dto) {
        Optional<Customer> existingUser = customerRepository.findByEmail(dto.getEmail());

        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("User already exists with the same email!");
        }

        Customer customer = new Customer();
        customer.setFullName(dto.getFullName());
        customer.setEmail(dto.getEmail());
        customer.setAddress(dto.getAddress());
        customer.setPhone(dto.getPhone());
        customer.setPassword(passwordEncoder.encode(dto.getPassword()));
        customer.setRole(Role.CUSTOMER);
        customer.setActive(true);

        customerRepository.save(customer);
        return new ApiResponse<>(201, "Your account has been created successfully!", null);
    }

    @Override
    public ApiResponse<LoginResponse> loginUser(LoginDto dto, HttpServletResponse response) {
        Customer user = customerRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new NoActiveAccountException("No active account with these credentials!"));

        if (!user.isActive()) {
            throw new NoActiveAccountException("Your account is not active. Please contact administration.");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
            );
        } catch (Exception ex) {
            throw new UnexpectedException("Invalid credentials");
        }

        user.setLastActive(LocalDateTime.now());
        customerRepository.save(user);

        String token = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefToken(new HashMap<>(), user);

        setTokenCookies(response, token, refreshToken);

        LoginResponse loginResponse = new LoginResponse(user.getEmail(), user.getRole().name());
        loginResponse.setJwtToken(token);
        loginResponse.setRefreshToken(refreshToken);

        return new ApiResponse<>(200, "Login successful", loginResponse);
    }

    private void setTokenCookies(HttpServletResponse response, String token, String refreshToken) {
        Cookie jwtCookie = new Cookie("jwt", token);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(false); // Consider `true` in production
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(60 * 60);

        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(7 * 24 * 60 * 60);

        response.addCookie(jwtCookie);
        response.addCookie(refreshCookie);
    }

}
