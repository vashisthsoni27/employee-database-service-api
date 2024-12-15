package com.employee.database.service.app.controller;

import com.employee.database.service.app.entity.Project;
import com.employee.database.service.app.model.ProjectDto;
import com.employee.database.service.app.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectControllerTest {

    @Mock
    private ProjectService projectService;

    @InjectMocks
    private ProjectController projectController;

    private Project project;

    private ProjectDto projectDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        project = new Project();
        project.setId(1);
        project.setName("Project A");

        projectDto = new ProjectDto();
        projectDto.setName("Project A");
        projectDto.setEmployeeId(101L);
    }

    @Test
    void getAllProjects_ShouldReturnListOfProjects() {
        List<Project> projects = Arrays.asList(project);
        when(projectService.getAllProjects()).thenReturn(projects);

        ResponseEntity<List<Project>> response = projectController.getAllProjects();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(projectService, times(1)).getAllProjects();
    }

    @Test
    void getProjectById_ShouldReturnProject_WhenProjectExists() {
        when(projectService.getProjectById(1)).thenReturn(Optional.of(project));

        ResponseEntity<Project> response = projectController.getProjectById(1);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Project A", response.getBody().getName());
        verify(projectService, times(1)).getProjectById(1);
    }

    @Test
    void getProjectById_ShouldReturnNotFound_WhenProjectDoesNotExist() {
        when(projectService.getProjectById(1)).thenReturn(Optional.empty());

        ResponseEntity<Project> response = projectController.getProjectById(1);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        verify(projectService, times(1)).getProjectById(1);
    }

    @Test
    void addProject_ShouldReturnCreatedProject() {
        when(projectService.addProject(any(ProjectDto.class))).thenReturn(project);

        ResponseEntity<Project> response = projectController.addProject(projectDto);

        assertNotNull(response);
        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Project A", response.getBody().getName());
        verify(projectService, times(1)).addProject(projectDto);
    }

    @Test
    void updateProject_ShouldReturnUpdatedProject_WhenProjectExists() {
        Project updatedProject = new Project();
        updatedProject.setId(1);
        updatedProject.setName("Updated Project A");

        when(projectService.updateProject(eq(1), any(ProjectDto.class))).thenReturn(updatedProject);

        ResponseEntity<Project> response = projectController.updateProject(1, projectDto);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Updated Project A", response.getBody().getName());
        verify(projectService, times(1)).updateProject(eq(1), any(ProjectDto.class));
    }

    @Test
    void updateProject_ShouldReturnNotFound_WhenProjectDoesNotExist() {
        when(projectService.updateProject(eq(1), any(ProjectDto.class))).thenReturn(null);

        ResponseEntity<Project> response = projectController.updateProject(1, projectDto);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        verify(projectService, times(1)).updateProject(eq(1), any(ProjectDto.class));
    }

    @Test
    void deleteProject_ShouldReturnOk_WhenProjectIsDeleted() {
        when(projectService.deleteProject(1)).thenReturn(true);

        ResponseEntity<Map<String, String>> response = projectController.deleteEmployee(1);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Project deleted successfully", response.getBody().get("message"));
        verify(projectService, times(1)).deleteProject(1);
    }

    @Test
    void deleteProject_ShouldReturnNotFound_WhenProjectDoesNotExist() {
        when(projectService.deleteProject(1)).thenReturn(false);

        ResponseEntity<Map<String, String>> response = projectController.deleteEmployee(1);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Project not found", response.getBody().get("message"));
        verify(projectService, times(1)).deleteProject(1);
    }
}
