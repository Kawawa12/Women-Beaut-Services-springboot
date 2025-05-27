package com.example.BeautServices.services;

import com.example.BeautServices.apiresponse.ApiResponse;
import com.example.BeautServices.dto.CategoryDto;
import com.example.BeautServices.dto.CategoryResponseDto;
import com.example.BeautServices.entity.Category;
import com.example.BeautServices.exceptions.ResourceNotFoundException;
import com.example.BeautServices.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j  // Add logging
public class CategoryService {

    private final CategoryRepository categoryRepository;

    // Add validation for image size
    @Value("${category.image.max-size:2097152}") // 2MB default
    private long maxImageSize;

    public ApiResponse<?> createCategory(CategoryDto categoryDto) throws IOException {

        if(categoryRepository.existsByName(categoryDto.getName())){
            throw new ResourceNotFoundException("Category exists!");
        }

        MultipartFile imageFile = categoryDto.getImageFile();

        // Validate input
        if (imageFile == null || imageFile.isEmpty()) {
            throw new IllegalArgumentException("Image file is required");
        }

        if (imageFile.getSize() > maxImageSize) {
            throw new IllegalArgumentException("Image size exceeds maximum allowed size");
        }

        try {
            byte[] imageBytes = imageFile.getBytes();

            Category category = Category.builder()
                    .name(categoryDto.getName())
                    .description(categoryDto.getDescription())
                    .image(imageBytes)
                    .build();

            categoryRepository.save(category);
        } catch (IOException e) {
            log.error("Error processing image file", e);
            throw e;
        }

        return new ApiResponse<>(HttpStatus.CREATED.value(), "Category created Successfully", null);
    }


    public ApiResponse<?> updateCategory(CategoryDto categoryDto, Long id)throws IOException {

       try{
           Category category = categoryRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Category is not found!"));
           category.setName(categoryDto.getName());
           category.setDescription(categoryDto.getDescription());

           MultipartFile imageFile = categoryDto.getImageFile();
           if (imageFile.getSize() > maxImageSize) {
               throw new IllegalArgumentException("Image size exceeds maximum allowed size");
           }
           byte[] imageBytes = imageFile.getBytes();

           category.setImage(imageBytes);
           category.setUpdatedAt(LocalDateTime.now());

           categoryRepository.save(category);
       } catch (IOException e) {
           log.error("Error processing image file", e);
           throw e;
       }
       return new ApiResponse<>(HttpStatus.CREATED.value(), "Updated Successfully", null);
    }

    public List<Category> getAllActiveCategories() {
        return categoryRepository.findByActiveTrue();
    }

    public List<CategoryResponseDto> getAllCategories() {
         List<Category> categories = categoryRepository.findAll();
         if(categories.isEmpty())throw new ResourceNotFoundException("No category found");
         return categories.stream().map(this::mapCategoryToDto).collect(Collectors.toList());
    }

    private CategoryResponseDto mapCategoryToDto(Category category){
        CategoryResponseDto dto = new CategoryResponseDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        dto.setActive(category.isActive());
        dto.setCreateDate(category.getCreatedAt());
        dto.setImage(category.getImage());
        return dto;
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }

    public void deactivateCategory(Long id) {
        Category category = getCategoryById(id);
        category.setActive(false);
        categoryRepository.save(category);
    }
}