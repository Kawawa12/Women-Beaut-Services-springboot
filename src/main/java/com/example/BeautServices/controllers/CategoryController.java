package com.example.BeautServices.controllers;

import com.example.BeautServices.apiresponse.ApiResponse;
import com.example.BeautServices.dto.CategoryDto;
import com.example.BeautServices.dto.CategoryResponseDto;
import com.example.BeautServices.entity.Category;
import com.example.BeautServices.services.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<?> createCategory(
            @RequestParam("name") String name,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("imageFile") MultipartFile imageFile) {

        try {
            CategoryDto categoryDto = CategoryDto.builder()
                    .name(name)
                    .description(description)
                    .imageFile(imageFile)
                    .build();

            ApiResponse<?> response = categoryService.createCategory(categoryDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            log.warn("Validation error: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            log.error("Error processing category creation", e);
            return ResponseEntity.internalServerError().body("Error processing image file");
        }
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCategory(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("imageFile") MultipartFile imageFile) {

        try {
            CategoryDto categoryDto = CategoryDto.builder()
                    .name(name)
                    .description(description)
                    .imageFile(imageFile)
                    .build();

            ApiResponse<?> response = categoryService.updateCategory(categoryDto,id);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {

            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            log.error("Error processing category update", e);
            return ResponseEntity.internalServerError().body("Error processing image file");
        }
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
        List<CategoryResponseDto> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateCategory(@PathVariable Long id) {
        categoryService.deactivateCategory(id);
        return ResponseEntity.noContent().build();
    }
}