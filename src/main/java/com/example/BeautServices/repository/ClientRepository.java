package com.example.BeautServices.repository;

import com.example.BeautServices.entity.Client;
import com.example.BeautServices.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByEmail(String email);
    boolean existsByEmail(String email);
    List<Client> findByRole(Role role);
    long countByLastActiveAfter(LocalDateTime time);

}
