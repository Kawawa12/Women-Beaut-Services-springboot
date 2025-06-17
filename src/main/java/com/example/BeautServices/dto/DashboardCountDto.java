package com.example.BeautServices.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardCountDto {
    private long activeCustomers;
    private long totalServiceRooms;
    private long totalCategories;
    private long totalBeautServices;
    private long totalBookings;
}