package src.main.kiedywakacje.com;

import src.main.kiedywakacje.com.models.VehicleCondition;
import src.main.kiedywakacje.com.models.VehicleType;

import java.time.LocalDate;

public class Vehicle {
    private String registrationNumber;
    private VehicleType type;
    private LocalDate dateOfCarInspection;
    private VehicleCondition technicalConditionOfVehicle;
    private int enginePower;
    private int numberOfSeats;

    public Vehicle(String registrationNumber, VehicleType type, LocalDate dateOfCarInspection, VehicleCondition technicalConditionOfVehicle, int enginePower, int numberOfSeats) {
        this.registrationNumber = registrationNumber;
        this.type = type;
        this.dateOfCarInspection = dateOfCarInspection;
        this.technicalConditionOfVehicle = technicalConditionOfVehicle;
        this.enginePower = enginePower;
        this.numberOfSeats = numberOfSeats;
    }

    public static void CheckVehicleCondition(Vehicle vehicle){
        System.out.println(vehicle.getTechnicalConditionOfVehicle());
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "registrationNumber='" + registrationNumber + '\'' +
                ", type=" + type +
                ", dateOfCarInspection=" + dateOfCarInspection +
                ", technicalConditionOfVehicle='" + technicalConditionOfVehicle + '\'' +
                '}';
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public VehicleType getType() {
        return type;
    }

    public LocalDate getDateOfCarInspection() {
        return dateOfCarInspection;
    }

    public VehicleCondition getTechnicalConditionOfVehicle() {
        return technicalConditionOfVehicle;
    }

    public int getEnginePower() {
        return enginePower;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }

    public void setDateOfCarInspection(LocalDate dateOfCarInspection) {
        this.dateOfCarInspection = dateOfCarInspection;
    }

    public void setTechnicalConditionOfVehicle(VehicleCondition technicalConditionOfVehicle) {
        this.technicalConditionOfVehicle = technicalConditionOfVehicle;
    }

    public void setEnginePower(int enginePower) {
        this.enginePower = enginePower;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }
}
