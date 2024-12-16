package com.employee.database.service.app.controller;

import com.employee.database.service.app.entity.Project;

import com.employee.database.service.app.model.ProjectDto;
import com.employee.database.service.app.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    // Get all projects
    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {
        List<Project> projects = projectService.getAllProjects();
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @Operation(summary = "Get a project by ID", description = "Retrieve a specific project by its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Project.class))),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Integer id) {
        Optional<Project> project = projectService.getProjectById(id);
        return project.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Add a new project", description = "Create a new project and associate it with an employee.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Project created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Project.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<Project> addProject(@RequestBody ProjectDto projectDTO) {
        Project createdProject = projectService.addProject(projectDTO);
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }


    @Operation(summary = "Update an existing project", description = "Update the details of a project by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Project.class))),
            @ApiResponse(responseCode = "404", description = "Project not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable Integer id, @RequestBody ProjectDto projectDto) {
        Project updatedProject = projectService.updateProject(id, projectDto);
        return updatedProject != null
                ? new ResponseEntity<>(updatedProject, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Delete a project", description = "Delete a project by its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteEmployee(@PathVariable("id") Integer id) {
        boolean isDeleted = projectService.deleteProject(id);
        Map<String, String> response = new HashMap<>();
        if (isDeleted) {
            response.put("message", "Project deleted successfully");
            return ResponseEntity.ok(response);  // HTTP 200
        } else {
            response.put("message", "Project not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);  // HTTP 404
        }
    }
}
