package com.employee.database.service.app.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeDto {
    private Integer id;

    private String name;

    @JsonProperty("role_id")
    private Integer roleId;

    public EmployeeDto(Integer id, String name, Integer roleId) {
        this.id = id;
        this.name = name;
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public EmployeeDto() {}
}
