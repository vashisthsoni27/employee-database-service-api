package com.employee.database.service.app.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProjectDto {
    private String name;

    @JsonProperty("employee_id")
    private Long employeeId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public ProjectDto(String name, Long employeeId) {
        this.name = name;
        this.employeeId = employeeId;
    }

    public ProjectDto() {
        super();
    }
}

