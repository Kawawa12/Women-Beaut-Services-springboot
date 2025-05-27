package com.example.BeautServices.services;

import com.example.BeautServices.apiresponse.ApiResponse;
import com.example.BeautServices.dto.PasswordResetRequest;
import com.example.BeautServices.entity.Customer;
import com.example.BeautServices.entity.PasswordResetToken;
import com.example.BeautServices.exceptions.InvalidTokenException;
import com.example.BeautServices.exceptions.NoActiveAccountException;
import com.example.BeautServices.exceptions.UnexpectedException;
import com.example.BeautServices.repository.CustomerRepository;
import com.example.BeautServices.repository.PasswordResetTokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class ResetPasswordServiceImpl implements ResetPasswordService{

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    public ResetPasswordServiceImpl(CustomerRepository customerRepository, PasswordEncoder passwordEncoder,
                                    PasswordResetTokenRepository passwordResetTokenRepository) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }


    @Override
    @Transactional
    public ApiResponse<String> resetPasswordToken(String email) {
        try {
            // 1. Find customer
            Customer customer = customerRepository.findByEmail(email)
                    .orElseThrow(() -> new NoActiveAccountException("No active account with email: " + email));

            // 2. Delete expired tokens
            passwordResetTokenRepository.deleteAll(
                    passwordResetTokenRepository.findAllByCustomerAndExpiryDateBefore(
                            customer,
                            LocalDateTime.now()
                    )
            );

            // 3. Check for existing valid token
            Optional<PasswordResetToken> validTokenOpt = passwordResetTokenRepository
                    .findValidTokenByCustomerEmail(email);

            if (validTokenOpt.isPresent()) {
                return new ApiResponse<>(
                        200,
                        "A valid OTP already exists.",
                        validTokenOpt.get().getToken()
                );
            }

            // 4. Generate 6-character alphanumeric OTP
            String otp;
            int maxAttempts = 3;
            int attempts = 0;

            do {
                otp = generateMixedCaseAlphanumericOTP();
                attempts++;
            } while (passwordResetTokenRepository.existsByToken(otp) && attempts < maxAttempts);

            if (attempts == maxAttempts) {
                throw new UnexpectedException("Failed to generate a unique OTP after retries.");
            }

            // 5. Save new token
            PasswordResetToken newToken = new PasswordResetToken();
            newToken.setToken(otp);
            newToken.setCustomer(customer);
            newToken.setExpiryDate(LocalDateTime.now().plusMinutes(5)); // 5-minute expiry
            newToken.setUsed(false);

            passwordResetTokenRepository.save(newToken);

            return new ApiResponse<>(200, "OTP generated successfully.", otp);

        } catch (NoActiveAccountException e) {
            throw e;
        } catch (Exception e) {
            throw new UnexpectedException("Failed to process OTP request."+ e.getMessage());
        }
    }

    // Helper method to generate 6-character mixed alphanumeric OTP
    private String generateMixedCaseAlphanumericOTP() {
        String alphanumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder otp = new StringBuilder(6);

        for (int i = 0; i < 6; i++) {
            int index = (int)(Math.random() * alphanumeric.length());
            otp.append(alphanumeric.charAt(index));
        }

        return otp.toString();
    }
    // Helper method to generate 6-digit OTP
    private String generateSixDigitOTP() {
        // Generate random number between 100000 and 999999
        int otpNumber = 100000 + new Random().nextInt(900000);
        return String.valueOf(otpNumber);
    }


    @Override
    @Transactional
    public ApiResponse<String> resetPassword(PasswordResetRequest request) {
        try {
            // 1. Verify customer exists
            Customer customer = customerRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new NoActiveAccountException("No active account found with provided credential!"));

            // 2. Find valid, unused token
            PasswordResetToken token = passwordResetTokenRepository.findValidByToken(request.getOtp())
                    .orElseThrow(() -> new InvalidTokenException("Invalid or expired OTP"));

            // 3. Verify token belongs to this customer
            if (!token.getCustomer().getId().equals(customer.getId())) {
                throw new InvalidTokenException("OTP does not match with a provided credentials!");
            }

            // 4. Mark token as used
            token.setUsed(true);
            passwordResetTokenRepository.save(token);

            // 5. Update password
            customer.setPassword(passwordEncoder.encode(request.getNewPassword()));
            customerRepository.save(customer);

            // 6. Cleanup any other tokens for this user
            passwordResetTokenRepository.deleteAll(
                    passwordResetTokenRepository.findAllByCustomerAndExpiryDateBefore(
                            customer,
                            LocalDateTime.now()
                    )
            );

            return new ApiResponse<>(200, "Password has been reset successfully", null);

        } catch (NoActiveAccountException | InvalidTokenException e) {
            throw e; // Specific exceptions
        } catch (Exception e) {

            throw new UnexpectedException("Failed to reset password. Please try again."+ e.getMessage());
        }
    }

}
