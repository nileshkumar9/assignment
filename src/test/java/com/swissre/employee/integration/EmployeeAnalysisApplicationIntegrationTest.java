package com.swissre.employee.integration;

import com.swissre.employee.EmployeeAnalysisApplication;
import com.swissre.employee.service.impl.CsvEmployeeDataService;
import com.swissre.employee.service.impl.DefaultReportingLineAnalysisService;
import com.swissre.employee.service.impl.DefaultSalaryAnalysisService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration test for the complete Employee Analysis Application.
 */
class EmployeeAnalysisApplicationIntegrationTest {

    @TempDir
    Path tempDir;

    @Test
    void testCompleteAnalysisWithSampleData() throws IOException {
        // Create a test CSV file
        String csvContent = "id,firstName,lastName,salary,managerId\n" +
                "123,Joe,Doe,60000,\n" +
                "124,Martin,Chekov,45000,123\n" +
                "125,Bob,Ronstad,47000,123\n" +
                "300,Alice,Hasacat,50000,124\n" +
                "305,Brett,Hardleaf,34000,300\n";
        
        Path csvFile = tempDir.resolve("test-employees.csv");
        Files.write(csvFile, csvContent.getBytes());

        // Capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            // Create and run application
            EmployeeAnalysisApplication app = new EmployeeAnalysisApplication(
                    new CsvEmployeeDataService(),
                    new DefaultSalaryAnalysisService(),
                    new DefaultReportingLineAnalysisService()
            );

            app.runAnalysis(csvFile.toString());

            // Verify output contains expected sections
            String output = outputStream.toString();
            assertTrue(output.contains("EMPLOYEE ANALYSIS RESULTS"));
            assertTrue(output.contains("SALARY ANALYSIS"));
            assertTrue(output.contains("REPORTING LINE ANALYSIS"));
            assertTrue(output.contains("ANALYSIS COMPLETE"));

        } finally {
            System.setOut(originalOut);
        }
    }
}
