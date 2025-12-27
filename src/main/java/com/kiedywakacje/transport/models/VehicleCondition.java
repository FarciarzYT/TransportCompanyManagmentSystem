package com.kiedywakacje.transport.models;

public enum VehicleCondition {
    GOOD("Dobry"),
    MID("Średni"),
    BAD("Zły"),
    BROKEN("Uszkodzony");

    private final String displayName;

    VehicleCondition(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

