package com.kiedywakacje.transport;

import com.kiedywakacje.transport.models.Status;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReportService {
    private OrderRepository orderRepository;
    private VehicleRepository vehicleRepository;
    private UserRepository userRepository;

    public ReportService(OrderRepository orderRepository, VehicleRepository vehicleRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.vehicleRepository = vehicleRepository;
        this.userRepository = userRepository;
    }

    public void generateActivityReport() {
        System.out.println("\n=== RAPORT O WYNIKACH DZIAŁALNOŚCI ===");
        System.out.println("Data wygenerowania: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        System.out.println("\n--- STATYSTYKI ZLECEŃ ---");
        System.out.println("Łączna liczba zleceń: " + orderRepository.getTotalOrdersCount());
        System.out.println("Oczekujące (PENDING): " + orderRepository.getOrdersCountByStatus(Status.PENDING));
        System.out.println("Przypisane (ASSIGNED): " + orderRepository.getOrdersCountByStatus(Status.ASSIGNED));
        System.out.println("W trakcie (IN_PROGRESS): " + orderRepository.getOrdersCountByStatus(Status.IN_PROGRESS));
        System.out.println("Zakończone (COMPLETED): " + orderRepository.getOrdersCountByStatus(Status.COMPLETED));
        System.out.println("Anulowane (CANCELLED): " + orderRepository.getOrdersCountByStatus(Status.CANCELLED));

        System.out.println("\n--- STATYSTYKI POJAZDÓW ---");
        System.out.println("Łączna liczba pojazdów: " + vehicleRepository.getTotalVehiclesCount());
        System.out.println("Pojazdy wymagające przeglądu: " + vehicleRepository.getVehiclesNeedingInspection().size());
        System.out.println("Dostępne pojazdy: " + vehicleRepository.getAvailableVehicles().size());

        System.out.println("\n--- STATYSTYKI UŻYTKOWNIKÓW ---");
        System.out.println("Łączna liczba użytkowników: " + userRepository.getUserCount());
        System.out.println("Klienci: " + userRepository.getAllClients().size());
        System.out.println("Kierowcy: " + userRepository.getAllDrivers().size());
        System.out.println("Administratorzy: " + userRepository.getAllAdmins().size());

        double totalWeight = orderRepository.getAllOrders().stream()
                .mapToDouble(TransportOrder::getWeight)
                .sum();
        System.out.println("\n--- DODATKOWE STATYSTYKI ---");
        System.out.println("Łączna waga przewiezionych ładunków: " + String.format("%.2f", totalWeight) + " kg");
    }

    public void generateOrdersReport() {
        System.out.println("\n=== RAPORT ZLECEŃ ===");
        List<TransportOrder> allOrders = orderRepository.getAllOrders();
        if (allOrders.isEmpty()) {
            System.out.println("Brak zleceń w systemie.");
            return;
        }
        System.out.println("Liczba zleceń: " + allOrders.size());
        System.out.println("\nLista wszystkich zleceń:");
        allOrders.forEach(order -> System.out.println("  " + order));
    }

    public void generateVehiclesReport() {
        System.out.println("\n=== RAPORT POJAZDÓW ===");
        List<Vehicle> allVehicles = vehicleRepository.getAllVehicles();
        if (allVehicles.isEmpty()) {
            System.out.println("Brak pojazdów w systemie.");
            return;
        }
        System.out.println("Liczba pojazdów: " + allVehicles.size());
        System.out.println("\nLista wszystkich pojazdów:");
        allVehicles.forEach(vehicle -> {
            System.out.println("  " + vehicle);
            if (vehicle.needsInspection()) {
                System.out.println("    ⚠ Wymaga przeglądu!");
            }
        });
    }
}

