package com.employee.database.service.app.service;

import com.employee.database.service.app.entity.Role;
import com.employee.database.service.app.repository.RoleRepository;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


/**
 * Service class to handle business logic related to roles.
 * Provides CRUD operations and additional functionalities like deleting a role with employees
 * and reassigning associated projects.
 */
@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @PersistenceContext
    private final EntityManager entityManager;

    /**
     * Constructor to initialize the RoleService with RoleRepository and EntityManager.
     *
     * @param roleRepository the repository to access role data
     * @param entityManager  the entity manager for interacting with the database
     */
    public RoleService(RoleRepository roleRepository, EntityManager entityManager) {
        this.roleRepository = roleRepository;
        this.entityManager = entityManager;
    }

    /**
     * Retrieves all roles from the repository.
     *
     * @return a list of all roles
     */
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    /**
     * Retrieves a role by its ID.
     *
     * @param id the ID of the role to retrieve
     * @return the role if found, otherwise null
     */
    public Role getRoleById(Integer id) {
        Optional<Role> role = roleRepository.findById(id);
        return role.orElse(null);
    }

    /**
     * Saves a new role to the repository.
     *
     * @param role the role entity to save
     * @return the saved role
     */
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    /**
     * Updates an existing role by its ID with new details.
     *
     * @param id          the ID of the role to update
     * @param roleDetails the new details to update the role with
     * @return the updated role
     * @throws EntityNotFoundException if the role with the given ID is not found
     */
    public Role updateRole(Integer id, Role roleDetails) {
        return roleRepository.findById(id).map(role -> {
            role.setName(roleDetails.getName());
            return roleRepository.save(role);
        }).orElseThrow(() -> new EntityNotFoundException("Error while updating role"));
    }

    /**
     * Deletes a role by its ID.
     *
     * @param id the ID of the role to delete
     * @return true if the role was deleted successfully, otherwise false
     */
    public boolean deleteRole(Integer id) {
        if (roleRepository.existsById(id)) {
            roleRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Deletes a role, reassigns its associated projects to a default employee, and removes the role.
     *
     * @param roleId           the ID of the role to delete
     * @param defaultEmployeeId the ID of the employee to reassign projects to
     * @throws RuntimeException if the stored procedure execution fails
     */
    @Transactional
    public void deleteRoleWithEmployeesAndReassignProjects(Integer roleId, Integer defaultEmployeeId) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("DELETE_ROLE_AND_REASSIGN_PROJECTS");
        query.registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter(2, Integer.class, ParameterMode.IN);
        query.setParameter(1, roleId);
        query.setParameter(2, defaultEmployeeId);
        query.execute();
    }
}
