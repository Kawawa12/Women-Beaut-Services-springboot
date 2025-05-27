package com.example.BeautServices.repository;

import com.example.BeautServices.entity.Customer;
import com.example.BeautServices.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByEmail(String email);
    boolean existsByEmail(String email);
    List<Customer> findByRole(Role role);
    long countByLastActiveAfter(LocalDateTime time);

}
