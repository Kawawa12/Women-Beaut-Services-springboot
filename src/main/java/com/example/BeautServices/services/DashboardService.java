package com.example.BeautServices.services;

import com.example.BeautServices.dto.DashboardCountDto;
import com.example.BeautServices.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ClientRepository userRepository;
    private final ServiceRoomRepository serviceRoomRepository;
    private final CategoryRepository categoryRepository;
    private final BeautServiceRepository beautServiceRepository;
    private final BookingRepository bookingRepository;

    public DashboardCountDto getDashboardCounts() {
        long customers = userRepository.countActiveClientWithRole();
        long serviceRooms = serviceRoomRepository.count();
        long categories = categoryRepository.count();
        long beautServices = beautServiceRepository.count();
        long bookings = bookingRepository.count();

        return new DashboardCountDto(customers, serviceRooms, categories,beautServices,bookings);
    }
}