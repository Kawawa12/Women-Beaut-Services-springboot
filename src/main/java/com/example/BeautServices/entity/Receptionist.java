package com.example.BeautServices.entity;

import jakarta.persistence.*;

@Entity
public class Receptionist extends Customer {

    private String address;

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Image image;

    public Receptionist() {
        super();
    }

    // ✅ Constructor calling super to set Customer fields
    public Receptionist(String fullName, String email, String phone, String password, String address, Image image) {
        super(fullName, email, phone, password, Role.RECEPTIONIST);
        this.address = address;
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
