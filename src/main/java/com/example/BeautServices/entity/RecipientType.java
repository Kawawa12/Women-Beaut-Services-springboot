package com.example.BeautServices.entity;


public enum RecipientType {
    ALL, RECEPTIONISTS, CLIENTS;

    @Override
    public String toString(){
        String format = name().toLowerCase();
        return format.substring(0,1).toUpperCase() + format.substring(1);
    }
}
