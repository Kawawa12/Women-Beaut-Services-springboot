package com.example.BeautServices.entity;

public enum ServiceStatus {
    AVAILABLE,
    RESERVED,
    NOT_AVAILABLE;

    @Override
    public String toString(){
        //Convert status to readable Capitalized and removed underscore
        String formatted = name().toLowerCase().replace("_", " ");
        return formatted.substring(0,1).toUpperCase() + formatted.substring(1);
    }
}
