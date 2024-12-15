package com.employee.database.service.app.service;

import com.employee.database.service.app.entity.Employee;
import com.employee.database.service.app.entity.Project;
import com.employee.database.service.app.model.ProjectDto;
import com.employee.database.service.app.repository.EmployeeRepository;
import com.employee.database.service.app.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private ProjectService projectService;

    private Project project;
    private Employee employee;
    private ProjectDto projectDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        project = new Project();
        project.setId(1);
        project.setName("Project A");

        employee = new Employee();
        employee.setId(101);
        employee.setFirstName("John");
        employee.setSurname("John");

        projectDto = new ProjectDto();
        projectDto.setName("Project A");
        projectDto.setEmployeeId(101L);
    }

    @Test
    void addProject_ShouldReturnCreatedProject() {
        Project newProject = new Project();
        newProject.setId(1);
        newProject.setName("Project A");
        newProject.setEmployee(employee);

        when(employeeRepository.findById(101L)).thenReturn(Optional.of(employee));
        when(projectRepository.save(any(Project.class))).thenReturn(newProject);

        Project createdProject = projectService.addProject(projectDto);

        assertNotNull(createdProject);
        assertEquals("Project A", createdProject.getName());
        assertEquals(101, createdProject.getEmployee().getId());
        verify(employeeRepository, times(1)).findById(101L);
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    void addProject_ShouldThrowException_WhenEmployeeNotFound() {
        when(employeeRepository.findById(101L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> projectService.addProject(projectDto));

        assertEquals("Employee with ID 101 not found", exception.getMessage());
        verify(employeeRepository, times(1)).findById(101L);
        verify(projectRepository, never()).save(any(Project.class));
    }

    @Test
    void getProjectById_ShouldReturnProject_WhenProjectExists() {
        when(projectRepository.findById(1)).thenReturn(Optional.of(project));

        Optional<Project> foundProject = projectService.getProjectById(1);

        assertTrue(foundProject.isPresent());
        assertEquals("Project A", foundProject.get().getName());
        verify(projectRepository, times(1)).findById(1);
    }

    @Test
    void getProjectById_ShouldReturnEmptyOptional_WhenProjectDoesNotExist() {
        when(projectRepository.findById(1)).thenReturn(Optional.empty());

        Optional<Project> foundProject = projectService.getProjectById(1);

        assertFalse(foundProject.isPresent());
        verify(projectRepository, times(1)).findById(1);
    }

    @Test
    void getAllProjects_ShouldReturnListOfProjects() {
        List<Project> projects = Collections.singletonList(project);
        when(projectRepository.findAll()).thenReturn(projects);

        List<Project> result = projectService.getAllProjects();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Project A", result.get(0).getName());
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    public void testUpdateProject_Success() {
        // Mock repository calls
        when(projectRepository.findById(1)).thenReturn(Optional.of(project));
        when(employeeRepository.findById(101L)).thenReturn(Optional.of(employee));
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        // Call the updateProject method
        Project updatedProject = projectService.updateProject(1, projectDto);

        // Assert the result
        assertNotNull(updatedProject);
        assertEquals("Project A", updatedProject.getName());
        assertEquals(employee, updatedProject.getEmployee());
    }

    @Test
    public void testUpdateProject_ProjectNotFound() {
        // Mock repository call to return empty for the project
        when(projectRepository.findById(1)).thenReturn(Optional.empty());

        // Call the updateProject method and assert that the exception is thrown
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            projectService.updateProject(1, projectDto);
        });

        assertEquals("Project not found with ID: 1", exception.getMessage());
    }

    @Test
    public void testUpdateProject_EmployeeNotFound() {
        // Mock repository calls
        when(projectRepository.findById(1)).thenReturn(Optional.of(project));
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        // Call the updateProject method and assert that the exception is thrown
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            projectService.updateProject(1, projectDto);
        });

        assertEquals("Employee with Id 101 not found", exception.getMessage());
    }

    @Test
    public void testUpdateProject_NoEmployeeId() {
        projectDto.setEmployeeId(null);

        // Mock repository calls
        when(projectRepository.findById(1)).thenReturn(Optional.of(project));
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        // Call the updateProject method and assert the project is updated correctly
        Project updatedProject = projectService.updateProject(1, projectDto);

        // Assert the result (no employee should be set)
        assertNotNull(updatedProject);
        assertEquals("Project A", updatedProject.getName());
        assertNull(updatedProject.getEmployee());
    }

    @Test
    void deleteProject_ShouldReturnTrue_WhenProjectIsDeleted() {
        when(projectRepository.existsById(1)).thenReturn(true);

        boolean isDeleted = projectService.deleteProject(1);

        assertTrue(isDeleted);
        verify(projectRepository, times(1)).existsById(1);
        verify(projectRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteProject_ShouldReturnFalse_WhenProjectDoesNotExist() {
        when(projectRepository.existsById(1)).thenReturn(false);

        boolean isDeleted = projectService.deleteProject(1);

        assertFalse(isDeleted);
        verify(projectRepository, times(1)).existsById(1);
        verify(projectRepository, never()).deleteById(1);
    }
}
