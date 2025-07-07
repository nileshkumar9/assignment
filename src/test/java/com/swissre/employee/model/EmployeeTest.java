package com.swissre.employee.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class EmployeeTest {

    @Test
    void testValidEmployeeCreation() {
        Employee employee = new Employee(1, "John", "Doe", new BigDecimal("50000"), 2);
        
        assertEquals(1, employee.getId());
        assertEquals("John", employee.getFirstName());
        assertEquals("Doe", employee.getLastName());
        assertEquals("John Doe", employee.getFullName());
        assertEquals(new BigDecimal("50000"), employee.getSalary());
        assertEquals(Integer.valueOf(2), employee.getManagerId());
        assertFalse(employee.isCEO());
    }
    
    @Test
    void testCEOEmployee() {
        Employee ceo = new Employee(1, "Jane", "Smith", new BigDecimal("100000"), null);
        
        assertTrue(ceo.isCEO());
        assertNull(ceo.getManagerId());
    }
    
    @Test
    void testInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> 
            new Employee(0, "John", "Doe", new BigDecimal("50000"), 2));
        
        assertThrows(IllegalArgumentException.class, () -> 
            new Employee(-1, "John", "Doe", new BigDecimal("50000"), 2));
    }
    
    @Test
    void testInvalidFirstName() {
        assertThrows(IllegalArgumentException.class, () -> 
            new Employee(1, null, "Doe", new BigDecimal("50000"), 2));
        
        assertThrows(IllegalArgumentException.class, () -> 
            new Employee(1, "", "Doe", new BigDecimal("50000"), 2));
        
        assertThrows(IllegalArgumentException.class, () -> 
            new Employee(1, "   ", "Doe", new BigDecimal("50000"), 2));
    }
    
    @Test
    void testInvalidLastName() {
        assertThrows(IllegalArgumentException.class, () -> 
            new Employee(1, "John", null, new BigDecimal("50000"), 2));
        
        assertThrows(IllegalArgumentException.class, () -> 
            new Employee(1, "John", "", new BigDecimal("50000"), 2));
        
        assertThrows(IllegalArgumentException.class, () -> 
            new Employee(1, "John", "   ", new BigDecimal("50000"), 2));
    }
    
    @Test
    void testInvalidSalary() {
        assertThrows(IllegalArgumentException.class, () -> 
            new Employee(1, "John", "Doe", null, 2));
        
        assertThrows(IllegalArgumentException.class, () -> 
            new Employee(1, "John", "Doe", BigDecimal.ZERO, 2));
        
        assertThrows(IllegalArgumentException.class, () -> 
            new Employee(1, "John", "Doe", new BigDecimal("-1000"), 2));
    }
    
    @Test
    void testEquality() {
        Employee emp1 = new Employee(1, "John", "Doe", new BigDecimal("50000"), 2);
        Employee emp2 = new Employee(1, "Jane", "Smith", new BigDecimal("60000"), 3);
        Employee emp3 = new Employee(2, "John", "Doe", new BigDecimal("50000"), 2);
        
        assertEquals(emp1, emp2); // Same ID
        assertNotEquals(emp1, emp3); // Different ID
        assertEquals(emp1.hashCode(), emp2.hashCode()); // Same ID, same hash
    }
}
