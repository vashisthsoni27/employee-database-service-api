package com.employee.database.service.app.service;

import com.employee.database.service.app.entity.Employee;
import com.employee.database.service.app.entity.Role;
import com.employee.database.service.app.model.EmployeeDto;
import com.employee.database.service.app.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EmployeeMapperServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private EmployeeMapperService employeeMapperService;

    private Employee employee;
    private EmployeeDto employeeDto;
    private Role role;

    @BeforeEach
    void setUp() {
        // Initialize test data
        role = new Role(1, "ADMIN");
        employee = new Employee(1, "John", "Doe", role);
        employeeDto = new EmployeeDto(1, "John Doe", 1);
    }

    // Test the 'toDto' method

    @Test
    void testToDtoWhenEmployeeIsNull() {
        // Test when employee is null
        EmployeeDto result = employeeMapperService.toDto(null);
        assertNull(result, "EmployeeDto should be null when employee is null");
    }

    @Test
    void testToDtoWhenEmployeeIsNotNullAndRoleFound() {
        // Test when employee is not null and role is found
        Mockito.when(roleRepository.findById(employee.getRole().getId())).thenReturn(Optional.of(role));
        EmployeeDto result = employeeMapperService.toDto(employee);

        assertNotNull(result, "EmployeeDto should not be null");
        assertEquals(employee.getFirstName() + " " + employee.getSurname(), result.getName(), "EmployeeDto name should match the employee's full name");
        assertEquals(role.getId(), result.getRoleId(), "Role ID should be correctly set in the EmployeeDto");
    }

    @Test
    void testToDtoWhenEmployeeIsNotNullAndRoleNotFound() {
        // Test when employee is not null and role is not found
        Mockito.when(roleRepository.findById(employee.getRole().getId())).thenReturn(Optional.empty());
        EmployeeDto result = employeeMapperService.toDto(employee);

        assertNotNull(result, "EmployeeDto should not be null");
        assertEquals(employee.getFirstName() + " " + employee.getSurname(), result.getName(), "EmployeeDto name should match the employee's full name");
        assertNull(result.getRoleId(), "Role ID should be null when role is not found");
    }

    // Test the 'toEntity' method

    @Test
    void testToEntityWhenEmployeeDtoIsNull() {
        // Test when employeeDto is null
        Employee result = employeeMapperService.toEntity(null);
        assertNull(result, "Employee should be null when EmployeeDto is null");
    }

    @Test
    void testToEntityWhenEmployeeDtoIsNotNullAndRoleFound() {
        // Test when employeeDto is not null and role is found
        Mockito.when(roleRepository.findById(employeeDto.getRoleId())).thenReturn(Optional.of(role));
        Employee result = employeeMapperService.toEntity(employeeDto);

        assertNotNull(result, "Employee should not be null");
        assertEquals(employeeDto.getRoleId(), result.getRole().getId(), "Role ID should be correctly set in the Employee entity");
    }

    @Test
    void testToEntityWhenEmployeeDtoIsNotNullAndRoleNotFound() {
        // Test when employeeDto is not null and role is not found
        Mockito.when(roleRepository.findById(employeeDto.getRoleId())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> employeeMapperService.toEntity(employeeDto),
                "Expected to throw RuntimeException when role is not found");
        assertEquals("Role with ID " + employeeDto.getRoleId() + " not found", exception.getMessage(),
                "Exception message should indicate that the role was not found");
    }
}
