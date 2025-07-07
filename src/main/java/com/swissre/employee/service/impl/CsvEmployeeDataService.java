package com.swissre.employee.service.impl;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.swissre.employee.model.Employee;
import com.swissre.employee.service.EmployeeDataException;
import com.swissre.employee.service.EmployeeDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of EmployeeDataService using OpenCSV.
 * Follows Single Responsibility Principle.
 */
public class CsvEmployeeDataService implements EmployeeDataService {
    private static final Logger logger = LoggerFactory.getLogger(CsvEmployeeDataService.class);
    
    @Override
    public List<Employee> loadEmployeesFromCsv(String filePath) throws EmployeeDataException {
        logger.info("Loading employees from CSV file: {}", filePath);
        
        List<Employee> employees = new ArrayList<>();
        
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            List<String[]> records = reader.readAll();
            
            // Skip header if present
            boolean isFirstRow = true;
            for (String[] record : records) {
                if (isFirstRow && isHeaderRow(record)) {
                    isFirstRow = false;
                    continue;
                }
                
                Employee employee = parseEmployeeFromRecord(record);
                employees.add(employee);
            }
            
            logger.info("Successfully loaded {} employees", employees.size());
            return employees;
            
        } catch (IOException | CsvException e) {
            logger.error("Error reading CSV file: {}", filePath, e);
            throw new EmployeeDataException("Failed to read CSV file: " + filePath, e);
        }
    }
    
    private boolean isHeaderRow(String[] record) {
        // Check if first column contains "id" or "Id" (case insensitive)
        return record.length > 0 && record[0].toLowerCase().contains("id");
    }
    
    private Employee parseEmployeeFromRecord(String[] record) throws EmployeeDataException {
        if (record.length < 4) {
            throw new EmployeeDataException("Invalid record format. Expected at least 4 columns, got: " + record.length);
        }
        
        try {
            int id = Integer.parseInt(record[0].trim());
            String firstName = record[1].trim();
            String lastName = record[2].trim();
            BigDecimal salary = new BigDecimal(record[3].trim());
            
            Integer managerId = null;
            if (record.length > 4 && !record[4].trim().isEmpty()) {
                managerId = Integer.parseInt(record[4].trim());
            }
            
            return new Employee(id, firstName, lastName, salary, managerId);
            
        } catch (NumberFormatException e) {
            throw new EmployeeDataException("Invalid number format in record: " + String.join(",", record), e);
        } catch (IllegalArgumentException e) {
            throw new EmployeeDataException("Invalid employee data: " + e.getMessage(), e);
        }
    }
}
