package com.sms.exception;

/**
 * Thrown when a student ID does not exist in the database.
 */
public class StudentNotFoundException extends Exception {
    public StudentNotFoundException(String message) {
        super(message);
    }
}
