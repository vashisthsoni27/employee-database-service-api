package com.employee.database.service.app.service;

import com.employee.database.service.app.entity.Employee;
import com.employee.database.service.app.entity.Project;
import com.employee.database.service.app.model.ProjectDto;
import com.employee.database.service.app.repository.EmployeeRepository;
import com.employee.database.service.app.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private EmployeeRepository employeeRepository;  // If you need to fetch employee data while updating project

    /**
     * Add a new project.
     * @param projectDto DTO containing project data.
     * @return Saved Project entity.
     */
    public Project addProject(ProjectDto projectDto) {
        Employee employee = employeeRepository.findById(projectDto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee with ID " + projectDto.getEmployeeId() + " not found"));

        Project project = new Project();
        project.setName(projectDto.getName());
        project.setEmployee(employee);

        return projectRepository.save(project);
    }

    /**
     * Get a project by ID.
     * @param id Project ID.
     * @return Project or null if not found.
     */
    public Optional<Project> getProjectById(Integer id) {
        return projectRepository.findById(id);
    }

    /**
     * Get all projects.
     * @return List of all projects.
     */
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    /**
     * Update an existing project.
     * @param id Project ID to update.
     * @param projectDto DTO containing updated project data.
     * @return Updated Project entity.
     */
    public Project updateProject(Integer id, ProjectDto projectDto) {
        Project existingProject = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + id));

        if (projectDto.getName() != null) {
            existingProject.setName(projectDto.getName());
        }

        // Optionally update other fields, e.g., reassociate the project with a different employee
        if (projectDto.getEmployeeId() != null) {
            Employee employee = employeeRepository.findById(projectDto.getEmployeeId())
                    .orElseThrow(() -> new RuntimeException("Employee with Id " + projectDto.getEmployeeId() + " not found"));
            existingProject.setEmployee(employee);
        }

        return projectRepository.save(existingProject);
    }

    /**
     * Delete a project by ID.
     * @param id Project ID to delete.
     * @return True if deleted, false if not found.
     */
    public boolean deleteProject(Integer id) {
        if (projectRepository.existsById(id)) {
            projectRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
