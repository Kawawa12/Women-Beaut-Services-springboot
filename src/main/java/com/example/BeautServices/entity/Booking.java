package com.example.BeautServices.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private BeautService service;

    @ManyToOne
    private TimeSlot timeSlot;

    @ManyToOne
    private Client client;

    private LocalDate bookingDate;

    private String paymentMethod;
    private Double paidAmount;

    private String confirmationPin;

    private LocalDateTime pinExpiry;

    @Enumerated(EnumType.STRING)
    private BookingStatus status = BookingStatus.PENDING;

    @ManyToOne
    @JoinColumn(name = "booking_id") // FK column in Client table
    private ServiceRoom serviceRoom;


}

