package com.example.BeautServices.repository;

import com.example.BeautServices.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageProfRepository extends JpaRepository<Image, Long> {
}
