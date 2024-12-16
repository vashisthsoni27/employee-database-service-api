package com.employee.database.service.app.controller;

import com.employee.database.service.app.model.EmployeeDto;
import com.employee.database.service.app.service.EmployeeMapperService;
import com.employee.database.service.app.service.EmployeeService;
import com.employee.database.service.app.entity.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @Mock
    private EmployeeMapperService employeeMapperService;

    @InjectMocks
    private EmployeeController employeeController;

    private Employee employee;
    private EmployeeDto employeeDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        employee = new Employee();
        employee.setId(1);
        employee.setFirstName("John");
        employee.setSurname("Doe");

        employeeDto = new EmployeeDto();
        employeeDto.setId(1);
        employeeDto.setName("John Doe");
    }

    @Test
    void createEmployee_ShouldReturnCreatedEmployeeDto() {
        when(employeeMapperService.toEntity(any(EmployeeDto.class))).thenReturn(employee);
        when(employeeService.saveEmployee(any(Employee.class))).thenReturn(employee);
        when(employeeMapperService.toDto(any(Employee.class))).thenReturn(employeeDto);

        ResponseEntity<EmployeeDto> response = employeeController.createEmployee(employeeDto);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employeeDto, response.getBody());
        verify(employeeService, times(1)).saveEmployee(employee);
    }

    @Test
    void getAllEmployees_ShouldReturnListOfEmployeeDtos() {
        List<Employee> employees = Arrays.asList(employee);
        List<EmployeeDto> employeeDtos = Arrays.asList(employeeDto);

        when(employeeService.getAllEmployees()).thenReturn(employees);
        when(employeeMapperService.toDto(any(Employee.class))).thenReturn(employeeDto);

        ResponseEntity<List<EmployeeDto>> response = employeeController.getAllEmployees();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employeeDtos, response.getBody());
        verify(employeeService, times(1)).getAllEmployees();
    }

    @Test
    void getEmployeeById_ShouldReturnEmployeeDto_WhenEmployeeExists() {
        when(employeeService.getEmployeeById(1L)).thenReturn(Optional.of(employee));
        when(employeeMapperService.toDto(any(Employee.class))).thenReturn(employeeDto);

        ResponseEntity<EmployeeDto> response = employeeController.getEmployeeById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employeeDto, response.getBody());
        verify(employeeService, times(1)).getEmployeeById(1L);
    }

    @Test
    void getEmployeeById_ShouldReturnNotFound_WhenEmployeeDoesNotExist() {
        when(employeeService.getEmployeeById(1L)).thenReturn(Optional.empty());

        ResponseEntity<EmployeeDto> response = employeeController.getEmployeeById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(employeeService, times(1)).getEmployeeById(1L);
    }

    @Test
    void updateEmployee_ShouldReturnUpdatedEmployeeDto() {
        when(employeeMapperService.toEntity(any(EmployeeDto.class))).thenReturn(employee);
        when(employeeService.updateEmployee(eq(1L), any(Employee.class))).thenReturn(employee);
        when(employeeMapperService.toDto(any(Employee.class))).thenReturn(employeeDto);

        ResponseEntity<EmployeeDto> response = employeeController.updateEmployee(1L, employeeDto);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employeeDto, response.getBody());
        verify(employeeService, times(1)).updateEmployee(eq(1L), any(Employee.class));
    }

    @Test
    void deleteEmployee_ShouldReturnSuccessMessage_WhenEmployeeExists() {
        when(employeeService.deleteEmployee(1L)).thenReturn(true);

        ResponseEntity<Map<String, String>> response = employeeController.deleteEmployee(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Employee deleted successfully", response.getBody().get("message"));
        verify(employeeService, times(1)).deleteEmployee(1L);
    }

    @Test
    void deleteEmployee_ShouldReturnNotFoundMessage_WhenEmployeeDoesNotExist() {
        when(employeeService.deleteEmployee(1L)).thenReturn(false);

        ResponseEntity<Map<String, String>> response = employeeController.deleteEmployee(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Employee not found", response.getBody().get("message"));
        verify(employeeService, times(1)).deleteEmployee(1L);
    }
}
