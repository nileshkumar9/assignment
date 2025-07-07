# Employee Analysis System

A comprehensive Java application for analyzing employee organizational structure and identifying salary discrepancies and reporting line issues.

## Problem Statement

**BIG COMPANY** is employing a lot of employees. The company would like to analyze its organizational structure and identify potential improvements. The board wants to make sure that every manager earns at least 20% more than the average salary of its direct subordinates, but no more than 50% more than that average. The company wants to avoid too long reporting lines, therefore we would like to identify all employees which have more than 4 managers between them and the CEO.

## Features

- **Salary Analysis**: Identifies managers who earn less or more than they should based on their subordinates' salaries
- **Reporting Line Analysis**: Finds employees with reporting lines that are too long (more than 4 managers)
- **CSV Data Processing**: Reads employee data from CSV files
- **Comprehensive Reporting**: Provides detailed analysis results
- **Configurable**: Easy to modify analysis parameters

## Architecture & Design Principles

This application follows **SOLID principles** and **DRY principle**:

### SOLID Principles Applied:

1. **Single Responsibility Principle (SRP)**:
   - `EmployeeDataService`: Only responsible for loading data
   - `SalaryAnalysisService`: Only responsible for salary analysis
   - `ReportingLineAnalysisService`: Only responsible for reporting line analysis

2. **Open/Closed Principle (OCP)**:
   - Services are open for extension but closed for modification
   - New analysis types can be added without modifying existing code

3. **Liskov Substitution Principle (LSP)**:
   - All implementations can be substituted for their interfaces

4. **Interface Segregation Principle (ISP)**:
   - Separate interfaces for different concerns (data loading, salary analysis, reporting line analysis)

5. **Dependency Inversion Principle (DIP)**:
   - High-level modules depend on abstractions, not concretions
   - Main application depends on service interfaces, not implementations

### DRY Principle:
- Configuration constants are centralized in `AnalysisConfig`
- Common validation logic is reused
- Utility methods prevent code duplication

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/swissre/employee/
│   │       ├── model/                          # Domain models
│   │       │   ├── Employee.java
│   │       │   ├── SalaryAnalysisResult.java
│   │       │   └── ReportingLineAnalysisResult.java
│   │       ├── service/                        # Service interfaces
│   │       │   ├── EmployeeDataService.java
│   │       │   ├── SalaryAnalysisService.java
│   │       │   ├── ReportingLineAnalysisService.java
│   │       │   └── EmployeeDataException.java
│   │       ├── service/impl/                   # Service implementations
│   │       │   ├── CsvEmployeeDataService.java
│   │       │   ├── DefaultSalaryAnalysisService.java
│   │       │   └── DefaultReportingLineAnalysisService.java
│   │       ├── config/                         # Configuration
│   │       │   └── AnalysisConfig.java
│   │       └── EmployeeAnalysisApplication.java # Main application
│   └── resources/
│       └── logback.xml                         # Logging configuration
└── test/
    ├── java/                                   # Unit tests
    │   └── com/swissre/employee/
    │       ├── model/
    │       │   └── EmployeeTest.java
    │       └── service/impl/
    │           ├── DefaultSalaryAnalysisServiceTest.java
    │           └── DefaultReportingLineAnalysisServiceTest.java
    └── resources/
        └── sample-employees.csv                # Test data
```

## Requirements

- Java 8 or higher
- Maven 3.6 or higher

## CSV File Format

The CSV file should contain the following columns:
- `id`: Employee ID (integer)
- `firstName`: Employee's first name
- `lastName`: Employee's last name
- `salary`: Employee's salary (decimal)
- `managerId`: Manager's ID (integer, empty for CEO)

### Example CSV:
```csv
id,firstName,lastName,salary,managerId
123,Joe,Doe,60000,
124,Martin,Chekov,45000,123
125,Bob,Ronstad,47000,123
300,Alice,Hasacat,50000,124
305,Brett,Hardleaf,34000,300
```

## How to Build and Run

### 1. Build the Project
```bash
mvn clean compile
```

### 2. Run Tests
```bash
mvn test
```

### 3. Package the Application
```bash
mvn clean package
```

### 4. Run the Application
```bash
# Using Maven
mvn exec:java -Dexec.mainClass="com.swissre.employee.EmployeeAnalysisApplication" -Dexec.args="path/to/your/employees.csv"

# Using Java directly (after packaging)
java -cp target/employee-analysis-1.0.0.jar com.swissre.employee.EmployeeAnalysisApplication path/to/your/employees.csv

# Example with sample data
java -cp target/employee-analysis-1.0.0.jar com.swissre.employee.EmployeeAnalysisApplication src/test/resources/sample-employees.csv
```

## Configuration

You can modify the analysis parameters in `AnalysisConfig.java`:

- `MINIMUM_SALARY_INCREASE_PERCENTAGE`: Minimum percentage increase for managers (default: 20%)
- `MAXIMUM_SALARY_INCREASE_PERCENTAGE`: Maximum percentage increase for managers (default: 50%)
- `MAXIMUM_REPORTING_LINE_LENGTH`: Maximum number of managers in reporting line (default: 4)

## Sample Output

```
=== EMPLOYEE ANALYSIS RESULTS ===

=== SALARY ANALYSIS ===

Managers earning less than they should:
- Martin Chekov (ID: 124) earns 4000.00 less than expected

Managers earning more than they should:
- Joe Doe (ID: 123) earns 14000.00 more than expected

=== REPORTING LINE ANALYSIS ===

Employees with reporting lines too long (more than 4 managers):
- Brett Hardleaf (ID: 305) has 5 managers in reporting line

=== ANALYSIS COMPLETE ===
```

## Testing

The project includes comprehensive unit tests covering:
- Model validation
- Service implementations
- Edge cases and error conditions
- Business logic validation

Run tests with:
```bash
mvn test
```

## Logging

The application uses SLF4J with Logback for logging. Logs are written to both console and file (`employee-analysis.log`).

## Error Handling

- **File not found**: Clear error message when CSV file doesn't exist
- **Invalid CSV format**: Detailed error messages for malformed data
- **Missing managers**: Graceful handling of broken reporting chains
- **Circular references**: Protection against infinite loops in reporting chains

## Extensions

The architecture allows for easy extensions:
- Add new analysis types by implementing new service interfaces
- Support different data formats by implementing new `EmployeeDataService` implementations
- Add new output formats by extending the main application
- Integrate with databases or external APIs

## Dependencies

- **OpenCSV**: For CSV file processing
- **SLF4J + Logback**: For logging
- **JUnit 5**: For unit testing
- **Mockito**: For mocking in tests

## License

This project is for educational and assessment purposes.
