package com.kiedywakacje.transport;

import com.kiedywakacje.transport.models.UserRole;

public abstract class User {
    private String username;
    private String password;
    private String email;
    private String fullName;
    private UserRole role;
    private boolean isActive;

    public User(String username, String password, String email, String fullName, UserRole role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
        this.isActive = true;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public boolean changePassword(String oldPassword, String newPassword) {
        if (checkPassword(oldPassword) && newPassword != null && !newPassword.isEmpty()) {
            this.password = newPassword;
            return true;
        }
        return false;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username != null && !username.isEmpty()) {
            this.username = username;
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email != null && !email.isEmpty()) {
            this.email = email;
        }
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        if (fullName != null && !fullName.isEmpty()) {
            this.fullName = fullName;
        }
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return String.format("User{username='%s', email='%s', fullName='%s', role=%s, active=%s}",
                username, email, fullName, role.getDisplayName(), isActive);
    }
}

