package com.sg.ollie.service;

public class NoProductsAvailableException extends Exception{
    NoProductsAvailableException(String message){
        super((message));
    }
    NoProductsAvailableException(String message, Throwable cause) {
        super(message, cause);
    }
}