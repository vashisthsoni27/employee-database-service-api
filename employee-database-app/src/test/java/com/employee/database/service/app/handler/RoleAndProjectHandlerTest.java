package com.employee.database.service.app.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class RoleAndProjectHandlerTest {

    @Mock
    private Connection conn;

    @Mock
    private PreparedStatement updateProjectStmt;

    @Mock
    private PreparedStatement deleteEmployeeStmt;

    @Mock
    private PreparedStatement deleteRoleStmt;

    @InjectMocks
    private RoleAndProjectHandler roleAndProjectHandler;

    private final int roleId = 1;
    private final int defaultEmployeeId = 999;

    @BeforeEach
    void setUp() throws SQLException {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Mock the connection to return prepared statements
        when(conn.prepareStatement(anyString())).thenReturn(updateProjectStmt, deleteEmployeeStmt, deleteRoleStmt);
    }

    // Test case 1: Successfully deleting role and reassigning projects
    @Test
    void testDeleteRoleAndReassignProjectsSuccess() throws SQLException {
        // Call the method to be tested
        roleAndProjectHandler.deleteRoleAndReassignProjects(conn, roleId, defaultEmployeeId);

        // Verify that the queries are executed
        verify(updateProjectStmt).setInt(1, defaultEmployeeId);
        verify(updateProjectStmt).setInt(2, roleId);
        verify(updateProjectStmt).executeUpdate();

        verify(deleteEmployeeStmt).setInt(1, roleId);
        verify(deleteEmployeeStmt).executeUpdate();

        verify(deleteRoleStmt).setInt(1, roleId);
        verify(deleteRoleStmt).executeUpdate();
    }

    // Test case 2: SQLException during updating projects
    @Test
    void testDeleteRoleAndReassignProjectsSQLExceptionOnUpdateProject() throws SQLException {
        // Simulate SQLException during updateProjectStmt execution
        doThrow(new SQLException("Database error")).when(updateProjectStmt).executeUpdate();

        // Verify that SQLException is thrown
        SQLException exception = assertThrows(SQLException.class, () -> {
            roleAndProjectHandler.deleteRoleAndReassignProjects(conn, roleId, defaultEmployeeId);
        });
        assertEquals("Database error", exception.getMessage());
    }

    // Test case 5: Verify SQL statements and parameters are set correctly
    @Test
    void testDeleteRoleAndReassignProjectsVerifyStatements() throws SQLException {
        // Call the method to be tested
        roleAndProjectHandler.deleteRoleAndReassignProjects(conn, roleId, defaultEmployeeId);

        // Verify if the correct SQL statements were executed
        verify(conn).prepareStatement("UPDATE PROJECT SET employee_id = ? WHERE employee_id IN (SELECT id FROM EMPLOYEE WHERE role_id = ?)");
        verify(conn).prepareStatement("DELETE FROM EMPLOYEE WHERE role_id = ?");
        verify(conn).prepareStatement("DELETE FROM ROLE WHERE id = ?");
    }
}
