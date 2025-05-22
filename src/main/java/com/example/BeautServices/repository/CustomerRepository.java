package com.example.BeautServices.repository;

import com.example.BeautServices.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByEmail(String email);
    long countByLastActiveAfter(LocalDateTime time);

}
