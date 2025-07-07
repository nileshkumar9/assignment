package com.swissre.employee.service.impl;

import com.swissre.employee.config.AnalysisConfig;
import com.swissre.employee.model.Employee;
import com.swissre.employee.model.ReportingLineAnalysisResult;
import com.swissre.employee.service.ReportingLineAnalysisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of ReportingLineAnalysisService.
 * Follows Single Responsibility Principle and Open/Closed Principle.
 */
public class DefaultReportingLineAnalysisService implements ReportingLineAnalysisService {
    private static final Logger logger = LoggerFactory.getLogger(DefaultReportingLineAnalysisService.class);
    
    @Override
    public List<ReportingLineAnalysisResult> analyzeReportingLines(List<Employee> employees) {
        logger.info("Analyzing reporting lines for {} employees", employees.size());
        
        List<ReportingLineAnalysisResult> results = new ArrayList<>();
        
        // Create a map for quick employee lookup
        Map<Integer, Employee> employeeMap = employees.stream()
                .collect(Collectors.toMap(Employee::getId, emp -> emp));
        
        for (Employee employee : employees) {
            if (!employee.isCEO()) {
                ReportingLineAnalysisResult result = analyzeEmployeeReportingLine(employee, employeeMap);
                if (result != null) {
                    results.add(result);
                }
            }
        }
        
        logger.info("Found {} employees with reporting lines too long", results.size());
        return results;
    }
    
    private ReportingLineAnalysisResult analyzeEmployeeReportingLine(Employee employee, Map<Integer, Employee> employeeMap) {
        List<Employee> reportingChain = buildReportingChain(employee, employeeMap);
        int reportingLineLength = reportingChain.size();
        
        if (reportingLineLength > AnalysisConfig.MAXIMUM_REPORTING_LINE_LENGTH) {
            return new ReportingLineAnalysisResult(employee, reportingChain, reportingLineLength);
        }
        
        return null;
    }
    
    private List<Employee> buildReportingChain(Employee employee, Map<Integer, Employee> employeeMap) {
        List<Employee> reportingChain = new ArrayList<>();
        Employee current = employee;
        
        while (current.getManagerId() != null) {
            Employee manager = employeeMap.get(current.getManagerId());
            if (manager == null) {
                logger.warn("Manager with ID {} not found for employee {}", current.getManagerId(), current.getId());
                break;
            }
            
            reportingChain.add(manager);
            current = manager;
            
            // Prevent infinite loops in case of circular references
            if (reportingChain.size() > 100) {
                logger.error("Potential circular reference detected in reporting chain for employee {}", employee.getId());
                break;
            }
        }
        
        return reportingChain;
    }
}
