package com.sg.ollie.service;

public class NonExistentTaxProfile extends Exception{
    NonExistentTaxProfile(String message) {
        super(message);
    }
    NonExistentTaxProfile(String message, Throwable cause) {
        super(message, cause);
    }
}