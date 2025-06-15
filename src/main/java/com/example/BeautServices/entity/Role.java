package com.example.BeautServices.entity;

public enum Role {

    CUSTOMER,
    RECEPTIONIST,
    ADMIN;

    public String toString(){
        return name().substring(0,1).toUpperCase() + name().substring(1).toLowerCase();
    }
}
