package com.swissre.employee.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Represents an Employee in the organization.
 * Immutable value object following best practices.
 */
public class Employee {
    private final int id;
    private final String firstName;
    private final String lastName;
    private final BigDecimal salary;
    private final Integer managerId;

    public Employee(int id, String firstName, String lastName, BigDecimal salary, Integer managerId) {
        validateInput(id, firstName, lastName, salary);
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.managerId = managerId;
    }

    private void validateInput(int id, String firstName, String lastName, BigDecimal salary) {
        if (id <= 0) {
            throw new IllegalArgumentException("Employee ID must be positive");
        }
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be null or empty");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be null or empty");
        }
        if (salary == null || salary.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Salary must be positive");
        }
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public boolean isCEO() {
        return managerId == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return id == employee.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", salary=" + salary +
                ", managerId=" + managerId +
                '}';
    }
}
