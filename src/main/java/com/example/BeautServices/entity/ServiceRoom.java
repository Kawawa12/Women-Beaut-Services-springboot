package com.example.BeautServices.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int roomNo;
    private String serviceName;
    private int capacity;
    private int currentClientInService;

    @OneToMany(mappedBy = "serviceRoom", cascade = CascadeType.ALL)
    private List<Booking> bookings = new ArrayList<>();


}
