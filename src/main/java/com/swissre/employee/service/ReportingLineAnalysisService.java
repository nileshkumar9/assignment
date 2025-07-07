package com.swissre.employee.service;

import com.swissre.employee.model.Employee;
import com.swissre.employee.model.ReportingLineAnalysisResult;
import java.util.List;

/**
 * Service interface for reporting line analysis.
 * Follows Single Responsibility Principle - only concerned with reporting line analysis.
 */
public interface ReportingLineAnalysisService {
    /**
     * Analyzes reporting lines for all employees.
     * 
     * @param employees list of all employees
     * @return list of employees with reporting lines that are too long
     */
    List<ReportingLineAnalysisResult> analyzeReportingLines(List<Employee> employees);
}
