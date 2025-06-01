package com.example.BeautServices.services;

import com.example.BeautServices.apiresponse.ApiResponse;
import com.example.BeautServices.apiresponse.LoginResponse;
import com.example.BeautServices.dto.LoginDto;
import com.example.BeautServices.dto.RegisterDto;
import com.example.BeautServices.entity.Client;
import com.example.BeautServices.entity.Role;
import com.example.BeautServices.exceptions.NoActiveAccountException;
import com.example.BeautServices.exceptions.UnexpectedException;
import com.example.BeautServices.exceptions.UserAlreadyExistsException;
import com.example.BeautServices.repository.ClientRepository;
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

    private final ClientRepository clientRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthServiceImpl(ClientRepository clientRepository,
                           AuthenticationManager authenticationManager,
                           JwtService jwtService,
                           PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public long countCurrentlyActiveUsers() {
        return clientRepository.countByLastActiveAfter(LocalDateTime.now().minusMinutes(3));
    }

    @Override
    public ApiResponse<String> registerUser(RegisterDto dto) {
        Optional<Client> existingUser = clientRepository.findByEmail(dto.getEmail());

        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("User already exists with the same email!");
        }

        Client client = new Client();
        client.setFullName(dto.getFullName());
        client.setEmail(dto.getEmail());
        client.setAddress(dto.getAddress());
        client.setPhone(dto.getPhone());
        client.setPassword(passwordEncoder.encode(dto.getPassword()));
        client.setRole(Role.CUSTOMER);
        client.setActive(true);

        clientRepository.save(client);
        return new ApiResponse<>(201, "Your account has been created successfully!", null);
    }

    @Override
    public ApiResponse<LoginResponse> loginUser(LoginDto dto, HttpServletResponse response) {
        Client user = clientRepository.findByEmail(dto.getEmail())
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
        clientRepository.save(user);

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
