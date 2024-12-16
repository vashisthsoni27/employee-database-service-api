package com.employee.database.service.app.mapper;

import com.employee.database.service.app.entity.Employee;
import com.employee.database.service.app.entity.Role;
import com.employee.database.service.app.model.EmployeeDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

    @Mapping(target = "name", expression = "java(combineName(employee.getFirstName(), employee.getSurname()))")
    EmployeeDto toEmployeeDto(Employee employee);

    @Mapping(target = "firstName", expression = "java(splitName(employeeDto.getName())[0])")
    @Mapping(target = "surname", expression = "java(splitName(employeeDto.getName())[1])")
    Employee toEmployee(EmployeeDto employeeDto);

    default String combineName(String firstname, String surname) {
        return firstname + " " + surname;
    }

    default String[] splitName(String fullName) {
        String[] parts = fullName.split(" ", 2);
        return new String[] {parts[0], parts.length > 1 ? parts[1] : ""};
    }
}

