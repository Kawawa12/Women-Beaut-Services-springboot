package com.example.BeautServices.services;

import com.example.BeautServices.apiresponse.ApiResponse;
import com.example.BeautServices.dto.BeautServiceDto;
import com.example.BeautServices.dto.ServiceResponseDto;
import com.example.BeautServices.entity.BeautService;
import com.example.BeautServices.entity.TimeSlot;
import com.example.BeautServices.entity.Category;
import com.example.BeautServices.entity.ServiceStatus;
import com.example.BeautServices.exceptions.ResourceNotFoundException;
import com.example.BeautServices.exceptions.UnexpectedException;
import com.example.BeautServices.repository.BeautServiceRepository;
import com.example.BeautServices.repository.BeautTimeSlotRepository;
import com.example.BeautServices.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BeautServiceImpl implements BeautServiceService {


    private final BeautServiceRepository beautServiceRepository;
    private final BeautTimeSlotRepository timeSlotRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public ApiResponse<BeautService> createBeautService(BeautServiceDto dto) {
        Optional<Category> category = categoryRepository.findById(dto.getCatId());
        if (category.isEmpty()) {
            throw new ResourceNotFoundException("Category is not found!");
        }

        BeautService beautService = new BeautService();
        beautService.setName(dto.getName());
        beautService.setDescription(dto.getDescription());
        beautService.setPrice(Double.parseDouble(dto.getPrice()));
        beautService.setCategory(category.get());

        // 📷 Handle image file
        try {
            MultipartFile imageFile = dto.getImageFile();
            if (imageFile != null && !imageFile.isEmpty()) {
                beautService.setImage(imageFile.getBytes());
            }
        } catch (IOException e) {
            throw new UnexpectedException("Image processing failed: " + e.getMessage());
        }

        // 🕒 Fetch all available time slots
        List<TimeSlot> allTimeSlots = timeSlotRepository.findAll();


        // ✅ Set metadata
        beautService.setStatus(ServiceStatus.AVAILABLE);
        beautService.setCreatedAt(LocalDateTime.now());
        beautService.setUpdatedAt(LocalDateTime.now());
        beautService.setActive(true);

        // 💾 Save to DB
        beautServiceRepository.save(beautService);

        return new ApiResponse<>(HttpStatus.CREATED.value(), "Service created successfully.", beautService);
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<List<ServiceResponseDto>> getAllBeautService() {
        List<BeautService> serviceLists = beautServiceRepository.findByActiveTrue();

        List<ServiceResponseDto> responseList = serviceLists.stream().map(service -> {
            ServiceResponseDto dto = new ServiceResponseDto();
            dto.setId(service.getId());
            dto.setServiceName(service.getName());
            dto.setCategoryName(service.getCategory().getName());
            dto.setDescription(service.getDescription());
            dto.setPrice(String.valueOf(service.getPrice()));
            dto.setActive(service.isActive());
            dto.setStatus(service.getStatus().toString());
            dto.setImage(service.getImage());
            dto.setCreateDate(service.getCreatedAt());
            dto.setUpdateDate(service.getUpdatedAt());
            return dto;
        }).toList();

        return new ApiResponse<>(HttpStatus.OK.value(), "Success", responseList);
    }


    @Override
    @Transactional(readOnly = true)
    public ApiResponse<List<ServiceResponseDto>> getBeautServices() {
        List<BeautService> serviceLists = beautServiceRepository.findAll();

        List<ServiceResponseDto> responseList = serviceLists.stream().map(service -> {
            ServiceResponseDto dto = new ServiceResponseDto();
            dto.setId(service.getId());
            dto.setServiceName(service.getName());
            dto.setCategoryName(service.getCategory().getName());
            dto.setDescription(service.getDescription());
            dto.setPrice(String.valueOf(service.getPrice()));
            dto.setActive(service.isActive());
            dto.setStatus(service.getStatus().toString());
            dto.setImage(service.getImage());
            dto.setCreateDate(service.getCreatedAt());
            dto.setUpdateDate(service.getUpdatedAt());
            return dto;
        }).toList();

        return new ApiResponse<>(HttpStatus.OK.value(), "Success", responseList);
    }


    @Override
    public ApiResponse<BeautService> updateBeautService(Long id, BeautServiceDto dto) {
        BeautService service = beautServiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id: " + id));

        Optional<Category> category = categoryRepository.findById(dto.getCatId());
        if (category.isEmpty()) {
            throw new ResourceNotFoundException("Category is not found!");
        }

        service.setName(dto.getName());
        service.setDescription(dto.getDescription());
        service.setPrice(Double.parseDouble(dto.getPrice()));
        service.setCategory(category.get());

        try {
            MultipartFile imageFile = dto.getImageFile();
            if (imageFile != null && !imageFile.isEmpty()) {
                service.setImage(imageFile.getBytes());
            }
        } catch (IOException e) {
            throw new UnexpectedException("Image processing failed: " + e.getMessage());
        }

        service.setUpdatedAt(LocalDateTime.now());

        beautServiceRepository.save(service);

        return new ApiResponse<>(HttpStatus.OK.value(), "Service updated successfully.", service);
    }

    @Override
    public ApiResponse<String> toggleActiveStatus(Long id) {
        BeautService service = beautServiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id: " + id));

        service.setActive(!service.isActive());
        service.setUpdatedAt(LocalDateTime.now());
        beautServiceRepository.save(service);

        String statusMsg = service.isActive() ? "activated" : "deactivated";
        return new ApiResponse<>(HttpStatus.OK.value(), "Service successfully " + statusMsg, null);
    }


}
