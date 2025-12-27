package com.kiedywakacje.transport;

import com.kiedywakacje.transport.models.Rating;
import com.kiedywakacje.transport.models.UserRole;

public class Driver extends User {
    private Rating rating;
    private double salary;
    private int numberOfAllDriverTrips;
    private String licenseNumber;
    private static int numberOfTotalRides = 0;

    public Driver(String username, String password, String email, String fullName, 
                  Rating rating, double salary, String licenseNumber) {
        super(username, password, email, fullName, UserRole.DRIVER);
        this.rating = rating;
        this.salary = salary;
        this.licenseNumber = licenseNumber;
        this.numberOfAllDriverTrips = 0;
    }

    public Driver(String fullName, Rating rating, double salary, int numberOfAllDriverTrips) {
        super("driver_" + fullName.toLowerCase().replace(" ", "_"), 
              "default", "", fullName, UserRole.DRIVER);
        this.rating = rating;
        this.salary = salary;
        this.numberOfAllDriverTrips = numberOfAllDriverTrips;
        this.licenseNumber = "";
    }

    public void driveTo(String location) {
        System.out.println("Driving to " + location);
        numberOfTotalRides++;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        if (salary >= 0) {
            this.salary = salary;
        }
    }

    public int getNumberOfAllDriverTrips() {
        return numberOfAllDriverTrips;
    }

    public void setNumberOfAllDriverTrips(int numberOfAllDriverTrips) {
        if (numberOfAllDriverTrips >= 0) {
            this.numberOfAllDriverTrips = numberOfAllDriverTrips;
        }
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public static int getNumberOfTotalRides() {
        return numberOfTotalRides;
    }

    @Override
    public String toString() {
        return String.format("Driver{username='%s', name='%s', rating=%s, salary=%.2f, trips=%d, license='%s'}",
                getUsername(), getFullName(), rating, salary, numberOfAllDriverTrips, licenseNumber);
    }
}

