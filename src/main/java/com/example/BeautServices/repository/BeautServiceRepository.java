package com.example.BeautServices.repository;

import com.example.BeautServices.entity.BeautService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BeautServiceRepository extends JpaRepository<BeautService, Long> {
    List<BeautService> findByActiveTrue();

}
