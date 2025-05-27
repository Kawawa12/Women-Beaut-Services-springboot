package com.example.BeautServices.repository;

import com.example.BeautServices.entity.Customer;
import com.example.BeautServices.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

        // Existing methods
        @Query("SELECT p FROM PasswordResetToken p WHERE p.customer.email = :email AND p.expiryDate > CURRENT_TIMESTAMP AND p.used = false")
        Optional<PasswordResetToken> findValidTokenByCustomerEmail(@Param("email") String email);

        @Query("SELECT p FROM PasswordResetToken p WHERE p.token = :token AND p.expiryDate > CURRENT_TIMESTAMP AND p.used = false")
        Optional<PasswordResetToken> findValidByToken(@Param("token") String token);

        @Query("SELECT p FROM PasswordResetToken p WHERE p.customer = :customer AND p.expiryDate < CURRENT_TIMESTAMP")
        List<PasswordResetToken> findAllByCustomerAndExpiryDateBefore(
                @Param("customer") Customer customer,
                @Param("now") LocalDateTime now);

        boolean existsByToken(String token);

}
