package src.main.kiedywakacje.com;

import src.main.kiedywakacje.com.models.Rating;

public class Driver {
    private String fullName;
    private Rating rating;
    private double Salary;
    private int theNumberOfAllDriverTrips;
    private static int numberOfTotalRides;

    public void DriveTo(String location){
        System.out.println("Driving to " + location);
    }

    public Driver(String fullName, Rating rating, double salary, int theNumberOfAllDriverTrips) {
        this.fullName = fullName;
        this.rating = rating;
        Salary = salary;
        this.theNumberOfAllDriverTrips = theNumberOfAllDriverTrips;
    }
}
