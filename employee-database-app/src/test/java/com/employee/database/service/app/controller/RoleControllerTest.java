package com.employee.database.service.app.controller;

import com.employee.database.service.app.entity.Role;
import com.employee.database.service.app.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(RoleController.class)
class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoleService roleService;

    @InjectMocks
    private RoleController roleController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(roleController).build();
    }

    @Test
    void testGetAllRoles() throws Exception {
        Role role1 = new Role(1, "Admin");
        Role role2 = new Role(2, "User");

        when(roleService.getAllRoles()).thenReturn(List.of(role1, role2));

        mockMvc.perform(get("/api/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Admin"))
                .andExpect(jsonPath("$[1].name").value("User"));
    }

    @Test
    void testGetRoleById_Found() throws Exception {
        Role role = new Role(1, "Admin");

        when(roleService.getRoleById(1)).thenReturn(role);

        mockMvc.perform(get("/api/roles/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Admin"));
    }

    @Test
    void testGetRoleById_NotFound() throws Exception {
        when(roleService.getRoleById(999)).thenReturn(null);

        mockMvc.perform(get("/api/roles/{id}", 999))
                .andExpect(status().isNotFound());
    }

    @Test
    void testAddRole() throws Exception {
        Role role = new Role(1, "Admin");
        when(roleService.saveRole(any(Role.class))).thenReturn(role);

        mockMvc.perform(post("/api/roles")
                        .contentType("application/json")
                        .content("{\"name\": \"Admin\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Admin"));
    }

    @Test
    void testUpdateRole_Success() throws Exception {
        Role existingRole = new Role(1, "Admin");
        Role updatedRole = new Role(1, "SuperAdmin");

        when(roleService.updateRole(anyInt(), any(Role.class))).thenReturn(updatedRole);

        mockMvc.perform(put("/api/roles/{id}", 1)
                        .contentType("application/json")
                        .content("{\"name\": \"SuperAdmin\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("SuperAdmin"));
    }

    @Test
    void testUpdateRole_NotFound() throws Exception {
        Role updatedRole = new Role(999, "SuperAdmin");

        when(roleService.updateRole(anyInt(), any(Role.class))).thenThrow(new RuntimeException("Error while updating role"));

        mockMvc.perform(put("/api/roles/{id}", 999)
                        .contentType("application/json")
                        .content("{\"name\": \"SuperAdmin\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteRole_Success() throws Exception {
        doNothing().when(roleService).deleteRoleWithEmployeesAndReassignProjects(anyInt(), anyInt());

        mockMvc.perform(delete("/api/roles/{roleId}?defaultEmployeeId=1", 1))
                .andExpect(status().isNoContent());

        verify(roleService).deleteRoleWithEmployeesAndReassignProjects(1, 1);
    }

    @Test
    void testDeleteRole_NotFound() throws Exception {
        doThrow(new RuntimeException("Error while deleting role")).when(roleService).deleteRoleWithEmployeesAndReassignProjects(anyInt(), anyInt());

        mockMvc.perform(delete("/api/roles/{roleId}?defaultEmployeeId=1", 999))
                .andExpect(status().isNotFound());
    }
}

