package com.swissre.employee.service.impl;

import com.swissre.employee.model.Employee;
import com.swissre.employee.model.ReportingLineAnalysisResult;
import com.swissre.employee.service.ReportingLineAnalysisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DefaultReportingLineAnalysisServiceTest {
    
    private ReportingLineAnalysisService reportingLineAnalysisService;
    
    @BeforeEach
    void setUp() {
        reportingLineAnalysisService = new DefaultReportingLineAnalysisService();
    }
    
    @Test
    void testEmployeeWithTooLongReportingLine() {
        // Create a reporting chain: CEO -> Manager1 -> Manager2 -> Manager3 -> Manager4 -> Employee
        Employee ceo = new Employee(1, "CEO", "Boss", new BigDecimal("100000"), null);
        Employee manager1 = new Employee(2, "Manager", "One", new BigDecimal("80000"), 1);
        Employee manager2 = new Employee(3, "Manager", "Two", new BigDecimal("70000"), 2);
        Employee manager3 = new Employee(4, "Manager", "Three", new BigDecimal("60000"), 3);
        Employee manager4 = new Employee(5, "Manager", "Four", new BigDecimal("55000"), 4);
        Employee employee = new Employee(6, "Employee", "Six", new BigDecimal("50000"), 5);
        
        List<Employee> employees = Arrays.asList(ceo, manager1, manager2, manager3, manager4, employee);
        
        List<ReportingLineAnalysisResult> results = reportingLineAnalysisService.analyzeReportingLines(employees);
        
        assertEquals(1, results.size());
        ReportingLineAnalysisResult result = results.get(0);
        assertEquals(employee, result.getEmployee());
        assertEquals(5, result.getReportingLineLength()); // 5 managers in the chain
        
        // Verify the reporting chain
        List<Employee> reportingChain = result.getReportingChain();
        assertEquals(5, reportingChain.size());
        assertEquals(manager4, reportingChain.get(0)); // Direct manager
        assertEquals(manager3, reportingChain.get(1));
        assertEquals(manager2, reportingChain.get(2));
        assertEquals(manager1, reportingChain.get(3));
        assertEquals(ceo, reportingChain.get(4)); // Top of chain
    }
    
    @Test
    void testEmployeeWithAcceptableReportingLine() {
        // Create a reporting chain: CEO -> Manager1 -> Manager2 -> Manager3 -> Employee (4 managers - acceptable)
        Employee ceo = new Employee(1, "CEO", "Boss", new BigDecimal("100000"), null);
        Employee manager1 = new Employee(2, "Manager", "One", new BigDecimal("80000"), 1);
        Employee manager2 = new Employee(3, "Manager", "Two", new BigDecimal("70000"), 2);
        Employee manager3 = new Employee(4, "Manager", "Three", new BigDecimal("60000"), 3);
        Employee employee = new Employee(5, "Employee", "Five", new BigDecimal("50000"), 4);
        
        List<Employee> employees = Arrays.asList(ceo, manager1, manager2, manager3, employee);
        
        List<ReportingLineAnalysisResult> results = reportingLineAnalysisService.analyzeReportingLines(employees);
        
        assertTrue(results.isEmpty()); // 4 managers is acceptable
    }
    
    @Test
    void testCEONotAnalyzed() {
        Employee ceo = new Employee(1, "CEO", "Boss", new BigDecimal("100000"), null);
        
        List<Employee> employees = Arrays.asList(ceo);
        
        List<ReportingLineAnalysisResult> results = reportingLineAnalysisService.analyzeReportingLines(employees);
        
        assertTrue(results.isEmpty()); // CEO should not be analyzed
    }
    
    @Test
    void testDirectReportToCEO() {
        Employee ceo = new Employee(1, "CEO", "Boss", new BigDecimal("100000"), null);
        Employee directReport = new Employee(2, "Direct", "Report", new BigDecimal("80000"), 1);
        
        List<Employee> employees = Arrays.asList(ceo, directReport);
        
        List<ReportingLineAnalysisResult> results = reportingLineAnalysisService.analyzeReportingLines(employees);
        
        assertTrue(results.isEmpty()); // 1 manager (CEO) is fine
    }
    
    @Test
    void testMultipleEmployeesWithLongReportingLines() {
        // Create multiple chains with too many managers
        Employee ceo = new Employee(1, "CEO", "Boss", new BigDecimal("100000"), null);
        Employee manager1 = new Employee(2, "Manager", "One", new BigDecimal("80000"), 1);
        Employee manager2 = new Employee(3, "Manager", "Two", new BigDecimal("70000"), 2);
        Employee manager3 = new Employee(4, "Manager", "Three", new BigDecimal("60000"), 3);
        Employee manager4 = new Employee(5, "Manager", "Four", new BigDecimal("55000"), 4);
        Employee manager5 = new Employee(6, "Manager", "Five", new BigDecimal("54000"), 5);
        
        Employee employee1 = new Employee(7, "Employee", "One", new BigDecimal("50000"), 6); // 6 managers
        Employee employee2 = new Employee(8, "Employee", "Two", new BigDecimal("50000"), 5); // 5 managers
        
        List<Employee> employees = Arrays.asList(ceo, manager1, manager2, manager3, manager4, manager5, employee1, employee2);
        
        List<ReportingLineAnalysisResult> results = reportingLineAnalysisService.analyzeReportingLines(employees);
        
        assertEquals(3, results.size());
        
        // Check that both employees and manager5 are flagged
        assertTrue(results.stream().anyMatch(r -> r.getEmployee().equals(employee1) && r.getReportingLineLength() == 6));
        assertTrue(results.stream().anyMatch(r -> r.getEmployee().equals(employee2) && r.getReportingLineLength() == 5));
        assertTrue(results.stream().anyMatch(r -> r.getEmployee().equals(manager5) && r.getReportingLineLength() == 5));
    }
    
    @Test
    void testMissingManager() {
        // Employee reports to a manager that doesn't exist
        Employee employee = new Employee(1, "Employee", "One", new BigDecimal("50000"), 999);
        
        List<Employee> employees = Arrays.asList(employee);
        
        List<ReportingLineAnalysisResult> results = reportingLineAnalysisService.analyzeReportingLines(employees);
        
        assertTrue(results.isEmpty()); // Should handle gracefully
    }
}
