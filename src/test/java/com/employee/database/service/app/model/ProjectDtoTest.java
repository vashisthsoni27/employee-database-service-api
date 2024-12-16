package com.employee.database.service.app.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProjectDtoTest {

    @Test
    void testDefaultConstructor() {
        // Given
        ProjectDto projectDto = new ProjectDto();

        // Then
        assertNotNull(projectDto);
        assertNull(projectDto.getName());
        assertNull(projectDto.getEmployeeId());
    }

    @Test
    void testParameterizedConstructor() {
        // Given
        String expectedName = "Project A";
        Long expectedEmployeeId = 100L;

        // When
        ProjectDto projectDto = new ProjectDto(expectedName, expectedEmployeeId);

        // Then
        assertNotNull(projectDto);
        assertEquals(expectedName, projectDto.getName());
        assertEquals(expectedEmployeeId, projectDto.getEmployeeId());
    }

    @Test
    void testGetterAndSetterForName() {
        // Given
        ProjectDto projectDto = new ProjectDto();
        String name = "Project B";

        // When
        projectDto.setName(name);

        // Then
        assertEquals(name, projectDto.getName());
    }

    @Test
    void testGetterAndSetterForEmployeeId() {
        // Given
        ProjectDto projectDto = new ProjectDto();
        Long employeeId = 101L;

        // When
        projectDto.setEmployeeId(employeeId);

        // Then
        assertEquals(employeeId, projectDto.getEmployeeId());
    }

    @Test
    void testJsonPropertyAnnotationForEmployeeId() throws JsonProcessingException {
        // Given
        String jsonString = "{\"employee_id\": 100}";
        ProjectDto projectDto = new ProjectDto();

        ObjectMapper objectMapper = new ObjectMapper();
        projectDto = objectMapper.readValue(jsonString, ProjectDto.class);

        assertNotNull(projectDto);

        assertEquals(100L, projectDto.getEmployeeId());
    }
}
