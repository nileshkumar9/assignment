package com.swissre.employee.service.impl;

import com.swissre.employee.model.Employee;
import com.swissre.employee.model.SalaryAnalysisResult;
import com.swissre.employee.service.SalaryAnalysisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DefaultSalaryAnalysisServiceTest {
    
    private SalaryAnalysisService salaryAnalysisService;
    
    @BeforeEach
    void setUp() {
        salaryAnalysisService = new DefaultSalaryAnalysisService();
    }
    
    @Test
    void testUnderpaidManager() {
        // Manager earning less than 20% more than average subordinate salary
        Employee manager = new Employee(1, "Manager", "One", new BigDecimal("55000"), null);
        Employee subordinate1 = new Employee(2, "Sub", "One", new BigDecimal("50000"), 1);
        Employee subordinate2 = new Employee(3, "Sub", "Two", new BigDecimal("50000"), 1);
        
        List<Employee> employees = Arrays.asList(manager, subordinate1, subordinate2);
        
        List<SalaryAnalysisResult> results = salaryAnalysisService.analyzeSalaryDiscrepancies(employees);
        
        assertEquals(1, results.size());
        SalaryAnalysisResult result = results.get(0);
        assertEquals(SalaryAnalysisResult.SalaryIssueType.UNDERPAID, result.getIssueType());
        assertEquals(manager, result.getManager());
        assertEquals(new BigDecimal("50000.00"), result.getAverageSubordinateSalary());
        assertEquals(new BigDecimal("60000.00"), result.getMinimumExpectedSalary());
        assertEquals(new BigDecimal("5000.00"), result.getSalaryGap());
    }
    
    @Test
    void testOverpaidManager() {
        // Manager earning more than 50% more than average subordinate salary
        Employee manager = new Employee(1, "Manager", "One", new BigDecimal("80000"), null);
        Employee subordinate1 = new Employee(2, "Sub", "One", new BigDecimal("50000"), 1);
        Employee subordinate2 = new Employee(3, "Sub", "Two", new BigDecimal("50000"), 1);
        
        List<Employee> employees = Arrays.asList(manager, subordinate1, subordinate2);
        
        List<SalaryAnalysisResult> results = salaryAnalysisService.analyzeSalaryDiscrepancies(employees);
        
        assertEquals(1, results.size());
        SalaryAnalysisResult result = results.get(0);
        assertEquals(SalaryAnalysisResult.SalaryIssueType.OVERPAID, result.getIssueType());
        assertEquals(manager, result.getManager());
        assertEquals(new BigDecimal("50000.00"), result.getAverageSubordinateSalary());
        assertEquals(new BigDecimal("75000.00"), result.getMaximumExpectedSalary());
        assertEquals(new BigDecimal("5000.00"), result.getSalaryGap());
    }
    
    @Test
    void testManagerWithCorrectSalary() {
        // Manager earning between 20% and 50% more than average subordinate salary
        Employee manager = new Employee(1, "Manager", "One", new BigDecimal("65000"), null);
        Employee subordinate1 = new Employee(2, "Sub", "One", new BigDecimal("50000"), 1);
        Employee subordinate2 = new Employee(3, "Sub", "Two", new BigDecimal("50000"), 1);
        
        List<Employee> employees = Arrays.asList(manager, subordinate1, subordinate2);
        
        List<SalaryAnalysisResult> results = salaryAnalysisService.analyzeSalaryDiscrepancies(employees);
        
        assertTrue(results.isEmpty());
    }
    
    @Test
    void testNoSubordinates() {
        Employee manager = new Employee(1, "Manager", "One", new BigDecimal("65000"), null);
        
        List<Employee> employees = Arrays.asList(manager);
        
        List<SalaryAnalysisResult> results = salaryAnalysisService.analyzeSalaryDiscrepancies(employees);
        
        assertTrue(results.isEmpty());
    }
    
    @Test
    void testMultipleManagers() {
        // First manager - underpaid
        Employee manager1 = new Employee(1, "Manager", "One", new BigDecimal("55000"), null);
        Employee subordinate1 = new Employee(2, "Sub", "One", new BigDecimal("50000"), 1);
        Employee subordinate2 = new Employee(3, "Sub", "Two", new BigDecimal("50000"), 1);
        
        // Second manager - overpaid
        Employee manager2 = new Employee(4, "Manager", "Two", new BigDecimal("80000"), null);
        Employee subordinate3 = new Employee(5, "Sub", "Three", new BigDecimal("50000"), 4);
        Employee subordinate4 = new Employee(6, "Sub", "Four", new BigDecimal("50000"), 4);
        
        List<Employee> employees = Arrays.asList(manager1, subordinate1, subordinate2, manager2, subordinate3, subordinate4);
        
        List<SalaryAnalysisResult> results = salaryAnalysisService.analyzeSalaryDiscrepancies(employees);
        
        assertEquals(2, results.size());
        
        // Check that we have one underpaid and one overpaid
        long underpaidCount = results.stream()
                .filter(r -> r.getIssueType() == SalaryAnalysisResult.SalaryIssueType.UNDERPAID)
                .count();
        long overpaidCount = results.stream()
                .filter(r -> r.getIssueType() == SalaryAnalysisResult.SalaryIssueType.OVERPAID)
                .count();
        
        assertEquals(1, underpaidCount);
        assertEquals(1, overpaidCount);
    }
}
