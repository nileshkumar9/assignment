package com.swissre.employee.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a reporting line analysis result.
 * Immutable value object.
 */
public class ReportingLineAnalysisResult {
    private final Employee employee;
    private final List<Employee> reportingChain;
    private final int reportingLineLength;

    public ReportingLineAnalysisResult(Employee employee, List<Employee> reportingChain, int reportingLineLength) {
        this.employee = employee;
        this.reportingChain = Collections.unmodifiableList(new ArrayList<>(reportingChain)); // Java 8 compatible defensive copy
        this.reportingLineLength = reportingLineLength;
    }

    public Employee getEmployee() {
        return employee;
    }

    public List<Employee> getReportingChain() {
        return reportingChain;
    }

    public int getReportingLineLength() {
        return reportingLineLength;
    }

    @Override
    public String toString() {
        return String.format("Employee %s (ID: %d) has a reporting line of %d managers", 
                employee.getFullName(), 
                employee.getId(), 
                reportingLineLength);
    }
}
