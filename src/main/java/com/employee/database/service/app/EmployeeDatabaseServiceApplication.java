package com.employee.database.service.app;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Employee Management API", version = "v1", description = "API for managing employees, projects and roles"))
public class EmployeeDatabaseServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(EmployeeDatabaseServiceApplication.class, args);
    }
}