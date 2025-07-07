package com.swissre.employee;

import com.swissre.employee.model.Employee;
import com.swissre.employee.model.ReportingLineAnalysisResult;
import com.swissre.employee.model.SalaryAnalysisResult;
import com.swissre.employee.service.EmployeeDataException;
import com.swissre.employee.service.EmployeeDataService;
import com.swissre.employee.service.ReportingLineAnalysisService;
import com.swissre.employee.service.SalaryAnalysisService;
import com.swissre.employee.service.impl.CsvEmployeeDataService;
import com.swissre.employee.service.impl.DefaultReportingLineAnalysisService;
import com.swissre.employee.service.impl.DefaultSalaryAnalysisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Main application class for Employee Analysis System.
 * Follows Dependency Inversion Principle by depending on abstractions.
 */
public class EmployeeAnalysisApplication {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeAnalysisApplication.class);
    
    private final EmployeeDataService employeeDataService;
    private final SalaryAnalysisService salaryAnalysisService;
    private final ReportingLineAnalysisService reportingLineAnalysisService;
    
    public EmployeeAnalysisApplication(EmployeeDataService employeeDataService,
                                     SalaryAnalysisService salaryAnalysisService,
                                     ReportingLineAnalysisService reportingLineAnalysisService) {
        this.employeeDataService = employeeDataService;
        this.salaryAnalysisService = salaryAnalysisService;
        this.reportingLineAnalysisService = reportingLineAnalysisService;
    }
    
    public void runAnalysis(String csvFilePath) {
        try {
            logger.info("Starting employee analysis for file: {}", csvFilePath);
            
            // Load employee data
            List<Employee> employees = employeeDataService.loadEmployeesFromCsv(csvFilePath);
            logger.info("Loaded {} employees", employees.size());
            
            // Analyze salary discrepancies
            List<SalaryAnalysisResult> salaryIssues = salaryAnalysisService.analyzeSalaryDiscrepancies(employees);
            
            // Analyze reporting lines
            List<ReportingLineAnalysisResult> reportingLineIssues = reportingLineAnalysisService.analyzeReportingLines(employees);
            
            // Print results
            printResults(salaryIssues, reportingLineIssues);
            
        } catch (EmployeeDataException e) {
            logger.error("Error during employee analysis", e);
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    private void printResults(List<SalaryAnalysisResult> salaryIssues, List<ReportingLineAnalysisResult> reportingLineIssues) {
        System.out.println("\n=== EMPLOYEE ANALYSIS RESULTS ===\n");
        
        // Print salary issues
        System.out.println("=== SALARY ANALYSIS ===");
        if (salaryIssues.isEmpty()) {
            System.out.println("No salary discrepancies found.");
        } else {
            // Group by issue type
            List<SalaryAnalysisResult> underpaidManagers = salaryIssues.stream()
                    .filter(result -> result.getIssueType() == SalaryAnalysisResult.SalaryIssueType.UNDERPAID)
                    .collect(Collectors.toList());
            
            List<SalaryAnalysisResult> overpaidManagers = salaryIssues.stream()
                    .filter(result -> result.getIssueType() == SalaryAnalysisResult.SalaryIssueType.OVERPAID)
                    .collect(Collectors.toList());
            
            if (!underpaidManagers.isEmpty()) {
                System.out.println("\nManagers earning less than they should:");
                underpaidManagers.forEach(result -> 
                    System.out.printf("- %s (ID: %d) earns %s less than expected%n", 
                        result.getManager().getFullName(), 
                        result.getManager().getId(), 
                        result.getSalaryGap()));
            }
            
            if (!overpaidManagers.isEmpty()) {
                System.out.println("\nManagers earning more than they should:");
                overpaidManagers.forEach(result -> 
                    System.out.printf("- %s (ID: %d) earns %s more than expected%n", 
                        result.getManager().getFullName(), 
                        result.getManager().getId(), 
                        result.getSalaryGap()));
            }
        }
        
        // Print reporting line issues
        System.out.println("\n=== REPORTING LINE ANALYSIS ===");
        if (reportingLineIssues.isEmpty()) {
            System.out.println("No reporting line issues found.");
        } else {
            System.out.println("\nEmployees with reporting lines too long (more than 4 managers):");
            reportingLineIssues.forEach(result -> 
                System.out.printf("- %s (ID: %d) has %d managers in reporting line%n", 
                    result.getEmployee().getFullName(), 
                    result.getEmployee().getId(), 
                    result.getReportingLineLength()));
        }
        
        System.out.println("\n=== ANALYSIS COMPLETE ===");
    }
    
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java EmployeeAnalysisApplication <csv-file-path>");
            System.err.println("Example: java EmployeeAnalysisApplication employees.csv");
            System.exit(1);
        }
        
        String csvFilePath = args[0];
        
        // Create service instances (Dependency Injection)
        EmployeeDataService employeeDataService = new CsvEmployeeDataService();
        SalaryAnalysisService salaryAnalysisService = new DefaultSalaryAnalysisService();
        ReportingLineAnalysisService reportingLineAnalysisService = new DefaultReportingLineAnalysisService();
        
        // Create and run application
        EmployeeAnalysisApplication app = new EmployeeAnalysisApplication(
                employeeDataService, 
                salaryAnalysisService, 
                reportingLineAnalysisService
        );
        
        app.runAnalysis(csvFilePath);
    }
}
