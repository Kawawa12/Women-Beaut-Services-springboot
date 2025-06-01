package com.example.BeautServices.entity;

public enum BookingStatus {
    PENDING,
    TEMPORARY,
    RESERVED,
    CONFIRMED,
    IN_SERVICE,
    COMPLETED,
    EXPIRED;

    @Override
    public String toString(){
        String formated = name().toLowerCase().replace("_"," ");
        return formated.substring(0,1).toUpperCase() + formated.substring(1);
    }
}
