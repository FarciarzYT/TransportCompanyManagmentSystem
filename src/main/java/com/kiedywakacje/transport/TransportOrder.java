package com.kiedywakacje.transport;

import com.kiedywakacje.transport.models.CountryCodes;
import com.kiedywakacje.transport.models.Status;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TransportOrder {
    private static int idCounter = 1;
    private int id;
    private String clientName;
    private CountryCodes origin;
    private CountryCodes destination;
    private double weight;
    private Status status;
    private LocalDateTime orderDate;
    private Driver assignedDriver;
    private Vehicle assignedVehicle;
    private Payment payment;

    public TransportOrder(String clientName, CountryCodes origin, CountryCodes destination, double weight) {
        this.id = idCounter++;
        this.clientName = clientName;
        this.origin = origin;
        this.destination = destination;
        this.weight = weight;
        this.status = Status.PENDING;
        this.orderDate = LocalDateTime.now();
    }

    public static void placeOrder(Scanner scanner) {
        List<TransportOrder> orders = new ArrayList<>();

        System.out.println("\n--- NOWE ZLECENIE TRANSPORTOWE ---");

        System.out.print("Podaj nazwę klienta: ");
        String name = scanner.nextLine();

        System.out.println("Dostępne kody krajów: PL, DE, FR, ES, UK, IT, SE, NL, BE, CZ, SK, AT, DK, NO");
        System.out.print("Podaj kod kraju pochodzenia: ");
        String originCode = scanner.nextLine().toUpperCase();
        CountryCodes origin = CountryCodes.fromName(originCode);
        
        if (origin == null) {
            System.out.println("✘ Nieprawidłowy kod kraju. Zlecenie anulowane.");
            return;
        }

        System.out.print("Podaj kod kraju docelowego: ");
        String destCode = scanner.nextLine().toUpperCase();
        CountryCodes destination = CountryCodes.fromName(destCode);
        
        if (destination == null) {
            System.out.println("✘ Nieprawidłowy kod kraju. Zlecenie anulowane.");
            return;
        }

        System.out.print("Podaj wagę ładunku (kg): ");
        double weight;
        try {
            weight = Double.parseDouble(scanner.nextLine());
            if (weight <= 0) {
                System.out.println("✘ Waga musi być większa od zera. Zlecenie anulowane.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("✘ Nieprawidłowy format wagi. Zlecenie anulowane.");
            return;
        }

        System.out.print("Potwierdź trasę? (t/n): ");
        if (scanner.nextLine().equalsIgnoreCase("t")) {
            TransportOrder order = new TransportOrder(name, origin, destination, weight);
            orders.add(order);
            System.out.println("✔ Zlecenie potwierdzone i zarejestrowane!");
            System.out.println(order);
        } else {
            System.out.println("✘ Zlecenie anulowane.");
        }
    }

    public void assignDriver(Driver driver) {
        if (driver != null && this.status == Status.PENDING) {
            this.assignedDriver = driver;
            this.status = Status.ASSIGNED;
        }
    }

    public void assignVehicle(Vehicle vehicle) {
        if (vehicle != null) {
            this.assignedVehicle = vehicle;
        }
    }

    public void startOrder() {
        if (this.status == Status.ASSIGNED) {
            this.status = Status.IN_PROGRESS;
        }
    }

    public void completeOrder() {
        if (this.status == Status.IN_PROGRESS) {
            this.status = Status.COMPLETED;
        }
    }

    public void cancelOrder() {
        this.status = Status.CANCELLED;
    }

    @Override
    public String toString() {
        return String.format("[ID: %d] Klient: %-15s | Z: %-15s | Do: %-15s | Waga: %.1f kg | Status: %s",
                id, clientName, origin.getPolishName(), destination.getPolishName(), 
                weight, status != null ? status.name() : "NIEUSTAWIONY");
    }

    public int getId() {
        return id;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public CountryCodes getOrigin() {
        return origin;
    }

    public void setOrigin(CountryCodes origin) {
        this.origin = origin;
    }

    public CountryCodes getDestination() {
        return destination;
    }

    public void setDestination(CountryCodes destination) {
        this.destination = destination;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        if (weight > 0) {
            this.weight = weight;
        }
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public Driver getAssignedDriver() {
        return assignedDriver;
    }

    public Vehicle getAssignedVehicle() {
        return assignedVehicle;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}

