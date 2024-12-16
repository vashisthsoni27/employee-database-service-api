package com.employee.database.service.app.handler;

import java.sql.*;

public class RoleAndProjectHandler {

    public static void deleteRoleAndReassignProjects(Connection conn, int roleId, int defaultEmployeeId) throws SQLException {
        // Reassign all projects associated with those employees to a default employee
        String updateProjectQuery = "UPDATE PROJECT SET employee_id = ? WHERE employee_id IN (SELECT id FROM EMPLOYEE WHERE role_id = ?)";
        try (PreparedStatement updateProjectStmt = conn.prepareStatement(updateProjectQuery)) {
            updateProjectStmt.setInt(1, defaultEmployeeId);
            updateProjectStmt.setInt(2, roleId);
            updateProjectStmt.executeUpdate();
        }

        // Delete all employees with the given role ID
        String deleteEmployeeQuery = "DELETE FROM EMPLOYEE WHERE role_id = ?";
        try (PreparedStatement deleteEmployeeStmt = conn.prepareStatement(deleteEmployeeQuery)) {
            deleteEmployeeStmt.setInt(1, roleId);
            deleteEmployeeStmt.executeUpdate();
        }

        // Delete all employees with the given role ID
        String deleteRoleQuery = "DELETE FROM ROLE WHERE id = ?";
        try (PreparedStatement deleteRoleStmt = conn.prepareStatement(deleteRoleQuery)) {
            deleteRoleStmt.setInt(1, roleId);
            deleteRoleStmt.executeUpdate();
        }
    }
}

