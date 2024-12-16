package com.employee.database.service.app.controller;

import com.employee.database.service.app.model.EmployeeDto;
import com.employee.database.service.app.service.EmployeeMapperService;
import com.employee.database.service.app.service.EmployeeService;
import com.employee.database.service.app.entity.Employee;
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
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    public EmployeeService employeeService;

    @Autowired
    public EmployeeMapperService employeeMapperService;

    @Operation(summary = "Create a new employee", description = "Create a new employee with the provided details.")
    @ApiResponse(responseCode = "201", description = "Employee created successfully")
    @PostMapping
    public ResponseEntity<EmployeeDto> createEmployee(@RequestBody EmployeeDto employeeDto) {
        Employee createdEmployee = employeeService.saveEmployee(employeeMapperService.toEntity(employeeDto));
        return new ResponseEntity<>(employeeMapperService.toDto(createdEmployee), HttpStatus.OK);
    }

    @Operation(summary = "Get all employees", description = "Retrieve a list of all employees.")
    @ApiResponse(responseCode = "200", description = "List of employees retrieved successfully")
    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        List<EmployeeDto> employeeDtoList = employees.stream().map(e -> employeeMapperService.toDto(e)).toList();
        return new ResponseEntity<>(employeeDtoList, HttpStatus.OK);
    }

    @Operation(summary = "Get Employee by ID", description = "Fetch an employee by their unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeDto.class))),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable("id") Long id) {
        Optional<Employee> employee = employeeService.getEmployeeById(id);
        return employee.map(value -> new ResponseEntity<>(employeeMapperService.toDto(value), HttpStatus.OK))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());

    }

    @Operation(summary = "Update an existing employee", description = "Update details of an existing employee by their unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeDto.class))),
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable("id") Long id, @RequestBody EmployeeDto employeeDto) {
        Employee updatedEmployee = employeeService.updateEmployee(id, employeeMapperService.toEntity(employeeDto));
        return new ResponseEntity<>(employeeMapperService.toDto(updatedEmployee), HttpStatus.OK);
    }

    @Operation(summary = "Delete an employee", description = "Delete an employee by their unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteEmployee(@PathVariable("id") Long id) {
        boolean isDeleted = employeeService.deleteEmployee(id);
        // Prepare response
        Map<String, String> response = new HashMap<>();
        if (isDeleted) {
            response.put("message", "Employee deleted successfully");
            return ResponseEntity.ok(response);  // HTTP 200
        } else {
            response.put("message", "Employee not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);  // HTTP 404
        }
    }
}
