package src.main.kiedywakacje.com;

import java.time.LocalDateTime;

public class TransportOrder {
    private static int idCounter = 1;
    private int id;
    private String clientName;
    private String destination;
    private double weight;
    private boolean isAssigned;

    public TransportOrder(String clientName, String destination, double weight) {
        this.id = idCounter++;
        this.clientName = clientName;
        this.destination = destination;
        this.weight = weight;
        this.isAssigned = false;
    }

    @Override
    public String toString() {
        return String.format("[ID: %d] Client: %-10s | To: %-15s | Weight: %.1fkg | Status: %s",
                id, clientName, destination, weight, isAssigned ? "ASSIGNED" : "PENDING");
    }
}