package com.employee.database.service.app.service;

import com.employee.database.service.app.entity.Employee;
import com.employee.database.service.app.entity.Role;
import com.employee.database.service.app.repository.EmployeeRepository;
import com.employee.database.service.app.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;
    private Role role;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        role = new Role();
        role.setId(1);
        role.setName("Developer");

        employee = new Employee();
        employee.setId(1);
        employee.setFirstName("John");
        employee.setSurname("Doe");
        employee.setRole(role);
    }

    @Test
    void saveEmployee_ShouldReturnSavedEmployee() {
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        Employee savedEmployee = employeeService.saveEmployee(employee);

        assertNotNull(savedEmployee);
        assertEquals("John", savedEmployee.getFirstName());
        assertEquals("Developer", savedEmployee.getRole().getName());
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    void getAllEmployees_ShouldReturnEmployeeList() {
        List<Employee> employees = Arrays.asList(employee);
        when(employeeRepository.findAll()).thenReturn(employees);

        List<Employee> result = employeeService.getAllEmployees();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void getEmployeeById_ShouldReturnEmployee_WhenEmployeeExists() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        Optional<Employee> result = employeeService.getEmployeeById(1L);

        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirstName());
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    void getEmployeeById_ShouldReturnEmpty_WhenEmployeeDoesNotExist() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Employee> result = employeeService.getEmployeeById(1L);

        assertFalse(result.isPresent());
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    void updateEmployee_ShouldUpdateAndReturnEmployee_WhenEmployeeExists() {
        Employee updatedEmployee = new Employee();
        updatedEmployee.setFirstName("Jane");
        updatedEmployee.setSurname("Smith");
        updatedEmployee.setRole(role);

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(updatedEmployee);

        Employee result = employeeService.updateEmployee(1L, updatedEmployee);

        assertNotNull(result);
        assertEquals("Jane", result.getFirstName());
        assertEquals("Smith", result.getSurname());
        verify(employeeRepository, times(1)).findById(1L);
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    void updateEmployee_ShouldThrowException_WhenEmployeeDoesNotExist() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        Employee updatedEmployee = new Employee();
        updatedEmployee.setFirstName("Jane");
        updatedEmployee.setSurname("Smith");

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            employeeService.updateEmployee(1L, updatedEmployee);
        });

        assertEquals("Employee not found with id: 1", exception.getMessage());
        verify(employeeRepository, times(1)).findById(1L);
        verify(employeeRepository, times(0)).save(any(Employee.class));
    }

    @Test
    void deleteEmployee_ShouldReturnTrue_WhenEmployeeExists() {
        when(employeeRepository.existsById(1L)).thenReturn(true);

        boolean result = employeeService.deleteEmployee(1L);

        assertTrue(result);
        verify(employeeRepository, times(1)).existsById(1L);
        verify(employeeRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteEmployee_ShouldReturnFalse_WhenEmployeeDoesNotExist() {
        when(employeeRepository.existsById(1L)).thenReturn(false);

        boolean result = employeeService.deleteEmployee(1L);

        assertFalse(result);
        verify(employeeRepository, times(1)).existsById(1L);
        verify(employeeRepository, times(0)).deleteById(anyLong());
    }
}
