package com.sg.ollie.dao;

public class NonExistentOrderException extends Exception {
    public NonExistentOrderException(String message) {
        super(message);
    }
    NonExistentOrderException(String message, Throwable cause) {
        super(message, cause);
    }
}