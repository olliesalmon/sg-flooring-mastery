package com.sg.ollie.dao;

public class FilePersistenceException extends Exception {
    FilePersistenceException(String message) {
        super(message);
    }
    FilePersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}