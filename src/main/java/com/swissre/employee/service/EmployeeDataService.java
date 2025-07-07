package com.swissre.employee.service;

import com.swissre.employee.model.Employee;
import java.util.List;

/**
 * Service interface for reading employee data.
 * Follows Single Responsibility Principle - only concerned with data loading.
 */
public interface EmployeeDataService {
    /**
     * Loads employee data from a CSV file.
     * 
     * @param filePath the path to the CSV file
     * @return list of employees
     * @throws EmployeeDataException if there's an error reading the file
     */
    List<Employee> loadEmployeesFromCsv(String filePath) throws EmployeeDataException;
}
