package com.example.BeautServices.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
public class BeautTimeSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalTime startTime;
    private LocalTime endTime; // Added to show the range
    private LocalDate date; // Optional: For booking specific dates

    @Enumerated(EnumType.STRING)
    private SlotStatus status = SlotStatus.AVAILABLE;

    private LocalDateTime reservedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private BeautService beautService;

    // Helper method to format time range as string
    public String getTimeRange() {
        return startTime + " - " + endTime;
    }
}
