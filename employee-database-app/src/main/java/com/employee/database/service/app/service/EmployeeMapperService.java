package com.employee.database.service.app.service;

import com.employee.database.service.app.entity.Employee;
import com.employee.database.service.app.entity.Role;
import com.employee.database.service.app.mapper.EmployeeMapper;
import com.employee.database.service.app.model.EmployeeDto;
import com.employee.database.service.app.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.util.Optional;

@Service
public class EmployeeMapperService {

    @Autowired
    private RoleRepository roleRepository;

    /**
     * Converts Employee entity to EmployeeDto.
     * @param employee The Employee entity.
     * @return EmployeeDto or null if employee is null.
     */
    public EmployeeDto toDto(Employee employee) {

        if (employee == null) {
            return null;
        }

        // Map Employee entity to EmployeeDto using the mapper
        EmployeeDto employeeDto = EmployeeMapper.INSTANCE.toEmployeeDto(employee);

        // Fetch the Role and map it to DTO
        Optional<Role> role = roleRepository.findById(employee.getRole().getId());
        role.ifPresent(value -> employeeDto.setRoleId(value.getId()));

        return employeeDto;
    }

    /**
     * Converts EmployeeDto to Employee entity.
     * @param employeeDto The EmployeeDto.
     * @return Employee entity.
     * @throws RuntimeException if the Role with the given ID is not found.
     */
    public Employee toEntity(EmployeeDto employeeDto) {

        if (employeeDto == null) {
            return null;
        }

        // Map EmployeeDto to Employee entity
        Employee employee = EmployeeMapper.INSTANCE.toEmployee(employeeDto);

        // Fetch the Role from the database, and handle missing role scenario
        Role role = roleRepository.findById(employeeDto.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role with ID " + employeeDto.getRoleId() + " not found"));

        // Set the Role to Employee entity
        employee.setRole(role);

        return employee;
    }
}
