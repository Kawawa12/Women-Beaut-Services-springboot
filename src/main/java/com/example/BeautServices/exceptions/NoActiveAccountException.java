package com.example.BeautServices.exceptions;

public class NoActiveAccountException extends RuntimeException{
    public NoActiveAccountException(String message) {
        super(message);
    }
}
