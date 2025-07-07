package com.swissre.employee.config;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Configuration class for analysis parameters.
 * Makes the system configurable and follows DRY principle.
 */
public class AnalysisConfig {
    public static final BigDecimal MINIMUM_SALARY_INCREASE_PERCENTAGE = new BigDecimal("0.20"); // 20%
    public static final BigDecimal MAXIMUM_SALARY_INCREASE_PERCENTAGE = new BigDecimal("0.50"); // 50%
    public static final int MAXIMUM_REPORTING_LINE_LENGTH = 4;

    // Private constructor to prevent instantiation
    private AnalysisConfig() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Gets the minimum expected salary for a manager based on average subordinate salary.
     * 
     * @param averageSubordinateSalary the average salary of direct subordinates
     * @return the minimum expected salary
     */
    public static BigDecimal getMinimumExpectedSalary(BigDecimal averageSubordinateSalary) {
        return averageSubordinateSalary.multiply(BigDecimal.ONE.add(MINIMUM_SALARY_INCREASE_PERCENTAGE))
                .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Gets the maximum expected salary for a manager based on average subordinate salary.
     * 
     * @param averageSubordinateSalary the average salary of direct subordinates
     * @return the maximum expected salary
     */
    public static BigDecimal getMaximumExpectedSalary(BigDecimal averageSubordinateSalary) {
        return averageSubordinateSalary.multiply(BigDecimal.ONE.add(MAXIMUM_SALARY_INCREASE_PERCENTAGE))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
