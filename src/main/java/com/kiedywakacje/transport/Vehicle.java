package com.kiedywakacje.transport;

import com.kiedywakacje.transport.models.VehicleCondition;
import com.kiedywakacje.transport.models.VehicleType;

import java.time.LocalDate;

public class Vehicle {
    private String registrationNumber;
    private VehicleType type;
    private LocalDate dateOfCarInspection;
    private VehicleCondition technicalConditionOfVehicle;
    private int enginePower;
    private int numberOfSeats;

    public Vehicle(String registrationNumber, VehicleType type, LocalDate dateOfCarInspection,
                   VehicleCondition technicalConditionOfVehicle, int enginePower, int numberOfSeats) {
        this.registrationNumber = registrationNumber;
        this.type = type;
        this.dateOfCarInspection = dateOfCarInspection;
        this.technicalConditionOfVehicle = technicalConditionOfVehicle;
        this.enginePower = enginePower;
        this.numberOfSeats = numberOfSeats;
    }

    public static void checkVehicleCondition(Vehicle vehicle) {
        if (vehicle == null) {
            System.out.println("Pojazd nie istnieje.");
            return;
        }
        System.out.println("Stan techniczny pojazdu " + vehicle.getRegistrationNumber() + 
                          ": " + vehicle.getTechnicalConditionOfVehicle().getDisplayName());
    }


    public boolean needsInspection() {
        if (dateOfCarInspection == null) {
            return true;
        }
        LocalDate nextInspection = dateOfCarInspection.plusYears(1);
        return LocalDate.now().isAfter(nextInspection) || LocalDate.now().isEqual(nextInspection);
    }

    @Override
    public String toString() {
        String inspectionStatus = needsInspection() ? "⚠ Wymaga przeglądu" : "✓ OK";
        return String.format("┌─ Pojazd: %s ───────────────────────\n" +
                             "│ Typ: %-28s                         \n" +
                             "│ Stan: %-28s                        \n" +
                             "│ Moc: %-4d KM                       \n" +
                             "| Miejsc: %-2d                       \n" +
                             "│ Przegląd: %-25s                    \n" +
                             "│ Status: %-28s                      \n" +
                             "└─────────────────────────────────────",
                registrationNumber,
                type.getDisplayName(),
                technicalConditionOfVehicle.getDisplayName(),
                enginePower, numberOfSeats,
                dateOfCarInspection != null ? dateOfCarInspection.toString() : "Brak danych",
                inspectionStatus);
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public VehicleType getType() {
        return type;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }

    public LocalDate getDateOfCarInspection() {
        return dateOfCarInspection;
    }

    public void setDateOfCarInspection(LocalDate dateOfCarInspection) {
        this.dateOfCarInspection = dateOfCarInspection;
    }

    public VehicleCondition getTechnicalConditionOfVehicle() {
        return technicalConditionOfVehicle;
    }

    public void setTechnicalConditionOfVehicle(VehicleCondition technicalConditionOfVehicle) {
        this.technicalConditionOfVehicle = technicalConditionOfVehicle;
    }

    public int getEnginePower() {
        return enginePower;
    }

    public void setEnginePower(int enginePower) {
        if (enginePower > 0) {
            this.enginePower = enginePower;
        }
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        if (numberOfSeats > 0) {
            this.numberOfSeats = numberOfSeats;
        }
    }
}

