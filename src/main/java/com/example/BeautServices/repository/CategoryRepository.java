package com.example.BeautServices.repository;

import com.example.BeautServices.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Find all active categories
    List<Category> findByActiveTrue();

    boolean existsByName(String name);

    // Optional: Find by name (exact match)
    List<Category> findByName(String name);
}
