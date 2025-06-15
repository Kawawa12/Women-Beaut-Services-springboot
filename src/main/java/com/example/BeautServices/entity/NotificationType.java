package com.example.BeautServices.entity;

public enum NotificationType {
    GENERAL,INFO, URGENT, PROMOTION;

    @Override
    public String toString(){
        String format = name().toLowerCase();
        return format.substring(0,1).toUpperCase() + format.substring(1);
    }
}
