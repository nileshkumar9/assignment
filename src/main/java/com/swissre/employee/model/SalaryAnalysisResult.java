package com.swissre.employee.model;

import java.math.BigDecimal;

/**
 * Represents a salary analysis result for a manager.
 * Immutable value object.
 */
public class SalaryAnalysisResult {
    private final Employee manager;
    private final BigDecimal averageSubordinateSalary;
    private final BigDecimal minimumExpectedSalary;
    private final BigDecimal maximumExpectedSalary;
    private final SalaryIssueType issueType;
    private final BigDecimal salaryGap;

    public SalaryAnalysisResult(Employee manager, BigDecimal averageSubordinateSalary, 
                               BigDecimal minimumExpectedSalary, BigDecimal maximumExpectedSalary,
                               SalaryIssueType issueType, BigDecimal salaryGap) {
        this.manager = manager;
        this.averageSubordinateSalary = averageSubordinateSalary;
        this.minimumExpectedSalary = minimumExpectedSalary;
        this.maximumExpectedSalary = maximumExpectedSalary;
        this.issueType = issueType;
        this.salaryGap = salaryGap;
    }

    public Employee getManager() {
        return manager;
    }

    public BigDecimal getAverageSubordinateSalary() {
        return averageSubordinateSalary;
    }

    public BigDecimal getMinimumExpectedSalary() {
        return minimumExpectedSalary;
    }

    public BigDecimal getMaximumExpectedSalary() {
        return maximumExpectedSalary;
    }

    public SalaryIssueType getIssueType() {
        return issueType;
    }

    public BigDecimal getSalaryGap() {
        return salaryGap;
    }

    public enum SalaryIssueType {
        UNDERPAID("earns less than they should"),
        OVERPAID("earns more than they should");

        private final String description;

        SalaryIssueType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    @Override
    public String toString() {
        return String.format("Manager %s (ID: %d) %s by %s", 
                manager.getFullName(), 
                manager.getId(), 
                issueType.getDescription(), 
                salaryGap);
    }
}
