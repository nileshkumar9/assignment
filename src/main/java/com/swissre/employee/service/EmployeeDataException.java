package com.swissre.employee.service;

/**
 * Custom exception for employee data related errors.
 */
public class EmployeeDataException extends Exception {
    public EmployeeDataException(String message) {
        super(message);
    }

    public EmployeeDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
