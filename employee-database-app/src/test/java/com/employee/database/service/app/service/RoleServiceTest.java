package com.employee.database.service.app.service;

import com.employee.database.service.app.entity.Role;
import com.employee.database.service.app.repository.RoleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.StoredProcedureQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private StoredProcedureQuery storedProcedureQuery;

    @InjectMocks
    private RoleService roleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initializes mock objects
    }

    @Test
    void testGetAllRoles() {
        Role role1 = new Role(1, "Admin");
        Role role2 = new Role(2, "User");
        when(roleRepository.findAll()).thenReturn(List.of(role1, role2));

        var roles = roleService.getAllRoles();
        assertNotNull(roles);
        assertEquals(2, roles.size());
    }

    @Test
    void testGetRoleById_Found() {
        Role role = new Role(1, "Admin");
        when(roleRepository.findById(1)).thenReturn(Optional.of(role));

        var result = roleService.getRoleById(1);
        assertNotNull(result);
        assertEquals("Admin", result.getName());
    }

    @Test
    void testGetRoleById_NotFound() {
        when(roleRepository.findById(999)).thenReturn(Optional.empty());

        var result = roleService.getRoleById(999);
        assertNull(result);
    }

    @Test
    void testSaveRole() {
        Role role = new Role(1, "Admin");
        when(roleRepository.save(any(Role.class))).thenReturn(role);

        var savedRole = roleService.saveRole(role);
        assertNotNull(savedRole);
        assertEquals("Admin", savedRole.getName());
    }

    @Test
    void testUpdateRole_Success() {
        Role existingRole = new Role(1, "Admin");
        Role updatedRole = new Role(1, "SuperAdmin");

        when(roleRepository.findById(1)).thenReturn(Optional.of(existingRole));
        when(roleRepository.save(any(Role.class))).thenReturn(updatedRole);

        var result = roleService.updateRole(1, updatedRole);
        assertNotNull(result);
        assertEquals("SuperAdmin", result.getName());
    }

    @Test
    void testUpdateRole_NotFound() {
        Role updatedRole = new Role(999, "SuperAdmin");

        when(roleRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> roleService.updateRole(999, updatedRole));
    }

    @Test
    void testDeleteRole_Success() {
        when(roleRepository.existsById(1)).thenReturn(true);

        var result = roleService.deleteRole(1);
        assertTrue(result);
        verify(roleRepository).deleteById(1);
    }

    @Test
    void testDeleteRole_NotFound() {
        when(roleRepository.existsById(999)).thenReturn(false);

        var result = roleService.deleteRole(999);
        assertFalse(result);
        verify(roleRepository, never()).deleteById(anyInt());
    }

    @Test
    void testDeleteRoleWithEmployeesAndReassignProjects() {
        // Setup the mock for StoredProcedureQuery
        when(entityManager.createStoredProcedureQuery("DELETE_ROLE_AND_REASSIGN_PROJECTS")).thenReturn(storedProcedureQuery);

        // Simulate the execution of the stored procedure
        roleService.deleteRoleWithEmployeesAndReassignProjects(1, 2);

        verify(storedProcedureQuery).execute();
    }

    @Test
    void testDeleteRoleWithEmployeesAndReassignProjects_Exception() {
        when(entityManager.createStoredProcedureQuery("DELETE_ROLE_AND_REASSIGN_PROJECTS")).thenReturn(storedProcedureQuery);
        doThrow(new RuntimeException("Stored procedure error")).when(storedProcedureQuery).execute();

        assertThrows(RuntimeException.class, () -> roleService.deleteRoleWithEmployeesAndReassignProjects(1, 2));
    }
}

