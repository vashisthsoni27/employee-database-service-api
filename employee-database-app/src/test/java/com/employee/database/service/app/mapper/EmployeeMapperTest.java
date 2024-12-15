package com.employee.database.service.app.mapper;

import com.employee.database.service.app.entity.Employee;
import com.employee.database.service.app.model.EmployeeDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmployeeMapperTest {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Test
    void testToEmployeeDto() {
        // Arrange
        Employee employee = new Employee();
        employee.setFirstName("John");
        employee.setSurname("Doe");

        // Act
        EmployeeDto employeeDto = employeeMapper.toEmployeeDto(employee);

        // Assert
        assertNotNull(employeeDto);
        assertEquals("John Doe", employeeDto.getName());
    }

    @Test
    void testToEmployee() {
        // Arrange
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setName("John Doe");

        // Act
        Employee employee = employeeMapper.toEmployee(employeeDto);

        // Assert
        assertNotNull(employee);
        assertEquals("John", employee.getFirstName());
        assertEquals("Doe", employee.getSurname());
    }

    @Test
    void testCombineName() {
        // Arrange
        String firstName = "John";
        String surname = "Doe";

        // Act
        String combinedName = employeeMapper.combineName(firstName, surname);

        // Assert
        assertEquals("John Doe", combinedName);
    }

    @Test
    void testSplitName() {
        // Arrange
        String fullName = "John Doe";

        // Act
        String[] splitName = employeeMapper.splitName(fullName);

        // Assert
        assertArrayEquals(new String[] {"John", "Doe"}, splitName);
    }

    @Test
    void testSplitNameWithSingleName() {
        // Arrange
        String fullName = "John";

        // Act
        String[] splitName = employeeMapper.splitName(fullName);

        // Assert
        assertArrayEquals(new String[] {"John", ""}, splitName);
    }
}
