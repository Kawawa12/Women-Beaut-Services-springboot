package com.example.BeautServices.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class BeautService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private double price;
    private String imageUrl;

    @ManyToOne
    private Category category;

    @OneToMany(mappedBy = "beautService", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BeautTimeSlot> timeSlots = new ArrayList<>();

    public BeautService() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<BeautTimeSlot> getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(List<BeautTimeSlot> timeSlots) {
        this.timeSlots = timeSlots;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
