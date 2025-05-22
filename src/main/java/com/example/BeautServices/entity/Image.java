package com.example.BeautServices.entity;

import jakarta.persistence.*;

@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(nullable = false)
    private byte[] img;

    @OneToOne(mappedBy = "image", optional = false)
    private Receptionist receptionist;

}
