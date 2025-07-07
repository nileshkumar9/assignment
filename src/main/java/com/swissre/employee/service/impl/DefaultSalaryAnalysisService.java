package com.swissre.employee.service.impl;

import com.swissre.employee.config.AnalysisConfig;
import com.swissre.employee.model.Employee;
import com.swissre.employee.model.SalaryAnalysisResult;
import com.swissre.employee.service.SalaryAnalysisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of SalaryAnalysisService.
 * Follows Single Responsibility Principle and Open/Closed Principle.
 */
public class DefaultSalaryAnalysisService implements SalaryAnalysisService {
    private static final Logger logger = LoggerFactory.getLogger(DefaultSalaryAnalysisService.class);
    
    @Override
    public List<SalaryAnalysisResult> analyzeSalaryDiscrepancies(List<Employee> employees) {
        logger.info("Analyzing salary discrepancies for {} employees", employees.size());
        
        List<SalaryAnalysisResult> results = new ArrayList<>();
        
        // Group employees by manager
        Map<Integer, List<Employee>> subordinatesByManager = employees.stream()
                .filter(emp -> emp.getManagerId() != null)
                .collect(Collectors.groupingBy(Employee::getManagerId));
        
        // Create a map for quick employee lookup
        Map<Integer, Employee> employeeMap = employees.stream()
                .collect(Collectors.toMap(Employee::getId, emp -> emp));
        
        for (Map.Entry<Integer, List<Employee>> entry : subordinatesByManager.entrySet()) {
            Integer managerId = entry.getKey();
            List<Employee> subordinates = entry.getValue();
            
            Employee manager = employeeMap.get(managerId);
            if (manager != null) {
                SalaryAnalysisResult result = analyzeManagerSalary(manager, subordinates);
                if (result != null) {
                    results.add(result);
                }
            }
        }
        
        logger.info("Found {} salary discrepancies", results.size());
        return results;
    }
    
    private SalaryAnalysisResult analyzeManagerSalary(Employee manager, List<Employee> subordinates) {
        BigDecimal averageSubordinateSalary = calculateAverageSubordinateSalary(subordinates);
        BigDecimal minimumExpectedSalary = AnalysisConfig.getMinimumExpectedSalary(averageSubordinateSalary);
        BigDecimal maximumExpectedSalary = AnalysisConfig.getMaximumExpectedSalary(averageSubordinateSalary);
        
        BigDecimal managerSalary = manager.getSalary();
        
        if (managerSalary.compareTo(minimumExpectedSalary) < 0) {
            // Manager is underpaid
            BigDecimal salaryGap = minimumExpectedSalary.subtract(managerSalary);
            return new SalaryAnalysisResult(
                    manager, 
                    averageSubordinateSalary, 
                    minimumExpectedSalary, 
                    maximumExpectedSalary,
                    SalaryAnalysisResult.SalaryIssueType.UNDERPAID, 
                    salaryGap
            );
        } else if (managerSalary.compareTo(maximumExpectedSalary) > 0) {
            // Manager is overpaid
            BigDecimal salaryGap = managerSalary.subtract(maximumExpectedSalary);
            return new SalaryAnalysisResult(
                    manager, 
                    averageSubordinateSalary, 
                    minimumExpectedSalary, 
                    maximumExpectedSalary,
                    SalaryAnalysisResult.SalaryIssueType.OVERPAID, 
                    salaryGap
            );
        }
        
        return null; // No salary issue
    }
    
    private BigDecimal calculateAverageSubordinateSalary(List<Employee> subordinates) {
        BigDecimal totalSalary = subordinates.stream()
                .map(Employee::getSalary)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return totalSalary.divide(BigDecimal.valueOf(subordinates.size()), 2, RoundingMode.HALF_UP);
    }
}
