package com.kiedywakacje.transport.models;

public enum UserRole {
    CLIENT("Klient"),
    DRIVER("Kierowca"),
    ADMIN("Administrator");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

