package com.kiedywakacje.transport;

import com.kiedywakacje.transport.models.VehicleCondition;
import com.kiedywakacje.transport.models.VehicleType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class VehicleRepository {
    private Map<String, Vehicle> vehiclesByRegistration;
    private List<Vehicle> allVehicles;

    public VehicleRepository() {
        this.vehiclesByRegistration = new HashMap<>();
        this.allVehicles = new ArrayList<>();
    }

    public void addVehicle(Vehicle vehicle) {
        if (vehicle != null && !vehiclesByRegistration.containsKey(vehicle.getRegistrationNumber())) {
            vehiclesByRegistration.put(vehicle.getRegistrationNumber(), vehicle);
            allVehicles.add(vehicle);
        }
    }

    public Vehicle getVehicleByRegistration(String registrationNumber) {
        return vehiclesByRegistration.get(registrationNumber);
    }

    public List<Vehicle> getAllVehicles() {
        return new ArrayList<>(allVehicles);
    }

    public List<Vehicle> getVehiclesByType(VehicleType type) {
        return allVehicles.stream()
                .filter(vehicle -> vehicle.getType() == type)
                .collect(Collectors.toList());
    }

    public List<Vehicle> getVehiclesByCondition(VehicleCondition condition) {
        return allVehicles.stream()
                .filter(vehicle -> vehicle.getTechnicalConditionOfVehicle() == condition)
                .collect(Collectors.toList());
    }

    public List<Vehicle> getAvailableVehicles() {
        return allVehicles.stream()
                .filter(vehicle -> vehicle.getTechnicalConditionOfVehicle() != VehicleCondition.BROKEN)
                .collect(Collectors.toList());
    }

    public List<Vehicle> getVehiclesNeedingInspection() {
        return allVehicles.stream()
                .filter(Vehicle::needsInspection)
                .collect(Collectors.toList());
    }

    public int getTotalVehiclesCount() {
        return allVehicles.size();
    }
}

