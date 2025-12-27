package com.kiedywakacje.transport.models;

public enum VehicleType {
    TRUCK("Ciężarówka"),
    CAR("Samochód"),
    BIKE("Motocykl"),
    TRACTOR("Ciągnik");

    private final String displayName;

    VehicleType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

