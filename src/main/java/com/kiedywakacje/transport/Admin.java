package com.kiedywakacje.transport;

import com.kiedywakacje.transport.models.UserRole;

public class Admin extends User {
    private String adminId;
    private String department;

    public Admin(String username, String password, String email, String fullName, 
                  String adminId, String department) {
        super(username, password, email, fullName, UserRole.ADMIN);
        this.adminId = adminId;
        this.department = department;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return String.format("Admin{username='%s', fullName='%s', adminId='%s', department='%s'}",
                getUsername(), getFullName(), adminId, department);
    }
}

