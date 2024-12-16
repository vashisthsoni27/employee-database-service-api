package com.employee.database.service.app.service;

import com.employee.database.service.app.entity.Employee;
import com.employee.database.service.app.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * Save a new or existing employee.
     * @param employee Employee entity to be saved.
     * @return Saved Employee entity.
     */
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    /**
     * Get all employees.
     * @return List of all employees.
     */
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    /**
     * Get employee by ID.
     * @param id Employee ID.
     * @return Optional of Employee, may be empty if not found.
     */
    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    /**
     * Update an existing employee.
     * @param id Employee ID to be updated.
     * @param updatedEmployee Employee data to update.
     * @return Updated Employee entity.
     * @throws EntityNotFoundException if employee not found.
     */
    public Employee updateEmployee(Long id, Employee updatedEmployee) {
        return employeeRepository.findById(id).map(employee -> {
            employee.setFirstName(updatedEmployee.getFirstName());
            employee.setSurname(updatedEmployee.getSurname());
            employee.setRole(updatedEmployee.getRole());
            return employeeRepository.save(employee);
        }).orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + id));
    }

    /**
     * Delete an employee by ID.
     * @param id Employee ID to be deleted.
     * @return true if employee deleted successfully.
     */
    public boolean deleteEmployee(Long id) {
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

