package com.swissre.employee.service;

import com.swissre.employee.model.Employee;
import com.swissre.employee.model.SalaryAnalysisResult;
import java.util.List;

/**
 * Service interface for salary analysis.
 * Follows Single Responsibility Principle - only concerned with salary analysis.
 */
public interface SalaryAnalysisService {
    /**
     * Analyzes salary discrepancies for all managers.
     * 
     * @param employees list of all employees
     * @return list of managers with salary issues
     */
    List<SalaryAnalysisResult> analyzeSalaryDiscrepancies(List<Employee> employees);
}
