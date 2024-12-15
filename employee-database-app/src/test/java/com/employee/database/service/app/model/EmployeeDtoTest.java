package com.employee.database.service.app.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeDtoTest {

    @Test
    void testDefaultConstructor() {
        // When
        EmployeeDto employeeDto = new EmployeeDto();

        // Then
        assertNotNull(employeeDto);
        assertNull(employeeDto.getId());
        assertNull(employeeDto.getName());
        assertNull(employeeDto.getRoleId());
    }

    @Test
    void testParameterizedConstructor() {
        // Given
        Integer expectedId = 1;
        String expectedName = "John Doe";
        Integer expectedRoleId = 2;

        // When
        EmployeeDto employeeDto = new EmployeeDto(expectedId, expectedName, expectedRoleId);

        // Then
        assertNotNull(employeeDto);
        assertEquals(expectedId, employeeDto.getId());
        assertEquals(expectedName, employeeDto.getName());
        assertEquals(expectedRoleId, employeeDto.getRoleId());
    }

    @Test
    void testGetterAndSetterForId() {
        // Given
        EmployeeDto employeeDto = new EmployeeDto();
        Integer id = 1;

        // When
        employeeDto.setId(id);

        // Then
        assertEquals(id, employeeDto.getId());
    }

    @Test
    void testGetterAndSetterForName() {
        // Given
        EmployeeDto employeeDto = new EmployeeDto();
        String name = "John Doe";

        // When
        employeeDto.setName(name);

        // Then
        assertEquals(name, employeeDto.getName());
    }

    @Test
    void testGetterAndSetterForRoleId() {
        // Given
        EmployeeDto employeeDto = new EmployeeDto();
        Integer roleId = 2;

        // When
        employeeDto.setRoleId(roleId);

        // Then
        assertEquals(roleId, employeeDto.getRoleId());
    }

    @Test
    void testJsonPropertyAnnotation() throws JsonProcessingException {
        // Given
        String jsonString = "{\"role_id\": 2}";
        EmployeeDto employeeDto = new EmployeeDto();
        ObjectMapper objectMapper = new ObjectMapper();
        employeeDto = objectMapper.readValue(jsonString, EmployeeDto.class);
        assertNotNull(employeeDto);
        assertEquals(2, employeeDto.getRoleId());
    }
}
