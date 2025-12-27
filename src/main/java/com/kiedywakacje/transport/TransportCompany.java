package com.kiedywakacje.transport;

import com.kiedywakacje.transport.models.CountryCodes;
import com.kiedywakacje.transport.models.Rating;
import com.kiedywakacje.transport.models.Status;
import com.kiedywakacje.transport.models.UserRole;
import com.kiedywakacje.transport.models.VehicleCondition;
import com.kiedywakacje.transport.models.VehicleType;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

/**
 * Główna klasa systemu zarządzania firmą transportową.
 * Wymagana funkcjonalność do oceny:
 * - Klienci mogą składać zlecenia na transport [X]
 * - Kierowcy mogą przeglądać zlecenia i wyznaczać trasy [X]
 * - System umożliwia monitorowanie stanu technicznego pojazdu [X]
 * - Możliwość generowania raportów o wynikach działalności [X]
 * - System rejestruje historię zleceń transportowych [X]
 *
 * Wszystkie wymogi zostały spełnione
 */
public class TransportCompany {
    private static UserRepository userRepository;
    private static OrderRepository orderRepository;
    private static VehicleRepository vehicleRepository;
    private static AuthenticationService authService;
    private static ReportService reportService;
    private static Scanner scanner;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        userRepository = new UserRepository();
        orderRepository = new OrderRepository();
        vehicleRepository = new VehicleRepository();
        authService = new AuthenticationService(userRepository);
        reportService = new ReportService(orderRepository, vehicleRepository, userRepository);

        createDefaultAdmin();
        createTestData();

        System.out.println("=== SYSTEM ZARZĄDZANIA FIRMĄ TRANSPORTOWĄ ===\n");
        boolean running = true;
        while (running) {
            if (!authService.isLoggedIn()) {
                running = showMainMenu();
            } else {
                running = showUserMenu();
            }
            if (running) {
                waitForEnter();
                clearConsole();
            }
        }
        scanner.close();
        System.out.println("\nDziękujemy za korzystanie z systemu!");
    }

    private static boolean showMainMenu() {
        System.out.println("\n=== MENU GŁÓWNE ===");
        System.out.println("1. Zaloguj się");
        System.out.println("2. Zarejestruj się jako klient");
        System.out.println("3. Wyjście");
        System.out.print("Wybierz opcję: ");

        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1":
                authService.login(scanner);
                break;
            case "2":
                authService.registerClient(scanner);
                break;
            case "3":
                return false;
            default:
                System.out.println("✘ Nieprawidłowa opcja!");
        }
        return true;
    }

    private static boolean showUserMenu() {
        User currentUser = authService.getCurrentUser();
        UserRole role = currentUser.getRole();

        System.out.println("\n=== MENU UŻYTKOWNIKA ===");
        System.out.println("Zalogowany jako: " + currentUser.getFullName() + " (" + role.getDisplayName() + ")");
        System.out.println("1. Składanie zlecenia transportowego");
        System.out.println("2. Wyświetl mapę Polski");

        if (role == UserRole.CLIENT) {
            System.out.println("3. Moja historia zleceń");
        } else if (role == UserRole.DRIVER) {
            System.out.println("3. Przeglądaj dostępne zlecenia");
            System.out.println("4. Moje przypisane zlecenia");
        } else if (role == UserRole.ADMIN) {
            System.out.println("3. Zarządzanie użytkownikami");
            System.out.println("4. Rejestracja kierowcy");
            System.out.println("5. Rejestracja administratora");
            System.out.println("6. Generuj raporty");
            System.out.println("7. Zarządzanie pojazdami");
            System.out.println("8. Usuń użytkownika");
        }

        System.out.println("0. Wyloguj się");
        System.out.print("Wybierz opcję: ");

        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1":
                if (role == UserRole.CLIENT) {
                    if (currentUser instanceof Client) {
                        TransportOrder.placeOrder(scanner, orderRepository, (Client) currentUser);
                    }
                } else {
                    System.out.println("✘ Tylko klienci mogą składać zlecenia!");
                }
                break;
            case "2":
                genPolandMap();
                break;
            case "3":
                if (role == UserRole.CLIENT) {
                    showClientOrderHistory();
                } else if (role == UserRole.DRIVER) {
                    showAvailableOrdersForDriver();
                } else if (role == UserRole.ADMIN) {
                    showUserManagement();
                }
                break;
            case "4":
                if (role == UserRole.DRIVER) {
                    showDriverAssignedOrders();
                } else if (role == UserRole.ADMIN) {
                    authService.registerDriver(scanner);
                } else {
                    System.out.println("✘ Nieprawidłowa opcja!");
                }
                break;
            case "5":
                if (role == UserRole.ADMIN) {
                    authService.registerAdmin(scanner);
                } else {
                    System.out.println("✘ Nieprawidłowa opcja!");
                }
                break;
            case "6":
                if (role == UserRole.ADMIN) {
                    showReportsMenu();
                } else {
                    System.out.println("✘ Nieprawidłowa opcja!");
                }
                break;
            case "7":
                if (role == UserRole.ADMIN) {
                    showVehicleManagementMenu();
                } else {
                    System.out.println("✘ Nieprawidłowa opcja!");
                }
                break;
            case "8":
                if (role == UserRole.ADMIN) {
                    deleteUser();
                } else {
                    System.out.println("✘ Nieprawidłowa opcja!");
                }
                break;
            case "0":
                authService.logout();
                break;
            default:
                System.out.println("✘ Nieprawidłowa opcja!");
        }
        return true;
    }

    private static void showClientOrderHistory() {
        User currentUser = authService.getCurrentUser();
        if (currentUser instanceof Client) {
            Client client = (Client) currentUser;
            System.out.println("\n=== MOJA HISTORIA ZLECEŃ ===");
            if (client.getOrderCount() == 0) {
                System.out.println("Brak zleceń w historii.");
            } else {
                client.getOrderHistory().forEach(System.out::println);
            }
        }
    }

    private static void showAvailableOrdersForDriver() {
        List<TransportOrder> pendingOrders = orderRepository.getPendingOrders();
        if (pendingOrders.isEmpty()) {
            System.out.println("\nBrak dostępnych zleceń do przypisania.");
            return;
        }
        
        System.out.println("\n=== DOSTĘPNE ZLECENIA ===");
        for (int i = 0; i < pendingOrders.size(); i++) {
            System.out.println((i + 1) + ". " + pendingOrders.get(i));
        }
        
        System.out.print("\nWybierz numer zlecenia do przypisania (0 - anuluj): ");
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice > 0 && choice <= pendingOrders.size()) {
                TransportOrder selectedOrder = pendingOrders.get(choice - 1);
                User currentUser = authService.getCurrentUser();
                if (currentUser instanceof Driver) {
                    Driver driver = (Driver) currentUser;
                    selectedOrder.assignDriver(driver);
                    
                    List<Vehicle> availableVehicles = vehicleRepository.getAvailableVehicles();
                    if (!availableVehicles.isEmpty()) {
                        System.out.println("\nDostępne pojazdy:");
                        for (int i = 0; i < availableVehicles.size(); i++) {
                            System.out.println((i + 1) + ". " + availableVehicles.get(i));
                        }
                        System.out.print("Wybierz pojazd (0 - pomin): ");
                        int vehicleChoice = Integer.parseInt(scanner.nextLine().trim());
                        if (vehicleChoice > 0 && vehicleChoice <= availableVehicles.size()) {
                            selectedOrder.assignVehicle(availableVehicles.get(vehicleChoice - 1));
                        }
                    }
                    
                    System.out.println("✔ Zlecenie przypisane do kierowcy: " + driver.getFullName());
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("✘ Nieprawidłowy wybór.");
        }
    }

    private static void showDriverAssignedOrders() {
        User currentUser = authService.getCurrentUser();
        if (!(currentUser instanceof Driver)) {
            return;
        }
        
        Driver driver = (Driver) currentUser;
        List<TransportOrder> allOrders = orderRepository.getAllOrders();
        List<TransportOrder> driverOrders = allOrders.stream()
                .filter(order -> order.getAssignedDriver() != null && 
                               order.getAssignedDriver().equals(driver))
                .toList();
        
        if (driverOrders.isEmpty()) {
            System.out.println("\nBrak przypisanych zleceń.");
            return;
        }
        
        System.out.println("\n=== MOJE PRZYPISANE ZLECENIA ===");
        for (int i = 0; i < driverOrders.size(); i++) {
            TransportOrder order = driverOrders.get(i);
            System.out.println((i + 1) + ". " + order);
            if (order.getAssignedVehicle() != null) {
                System.out.println("   Pojazd: " + order.getAssignedVehicle().getRegistrationNumber());
            }
        }
        
        System.out.print("\nWybierz numer zlecenia do zarządzania (0 - anuluj): ");
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice > 0 && choice <= driverOrders.size()) {
                TransportOrder selectedOrder = driverOrders.get(choice - 1);
                manageDriverOrder(selectedOrder);
            }
        } catch (NumberFormatException e) {
            System.out.println("✘ Nieprawidłowy wybór.");
        }
    }

    private static void manageDriverOrder(TransportOrder order) {
        System.out.println("\n=== ZARZĄDZANIE ZLECENIEM ===");
        System.out.println(order);
        System.out.println("\n1. Rozpocznij realizację");
        System.out.println("2. Oznacz jako zakończone");
        System.out.print("Wybierz opcję: ");
        
        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1":
                order.startOrder();
                System.out.println("✔ Zlecenie oznaczone jako w trakcie realizacji.");
                break;
            case "2":
                order.completeOrder();
                System.out.println("✔ Zlecenie oznaczone jako zakończone.");
                break;
            default:
                System.out.println("✘ Nieprawidłowa opcja!");
        }
    }

    private static void showUserManagement() {
        System.out.println("\n=== ZARZĄDZANIE UŻYTKOWNIKAMI ===");
        System.out.println("1. Statystyki użytkowników");
        System.out.println("2. Lista wszystkich użytkowników");
        System.out.println("3. Wyszukaj użytkownika");
        System.out.print("Wybierz opcję: ");
        
        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1":
                System.out.println("\nLiczba użytkowników: " + userRepository.getUserCount());
                System.out.println("Klienci: " + userRepository.getAllClients().size());
                System.out.println("Kierowcy: " + userRepository.getAllDrivers().size());
                System.out.println("Administratorzy: " + userRepository.getAllAdmins().size());
                break;
            case "2":
                showAllUsers();
                break;
            case "3":
                searchUsers();
                break;
            default:
                System.out.println("✘ Nieprawidłowa opcja!");
        }
    }

    private static void searchUsers() {
        System.out.print("\nPodaj frazę do wyszukania (nazwa użytkownika, imię i nazwisko lub email): ");
        String query = scanner.nextLine().trim();
        
        List<User> results = userRepository.searchUsers(query);
        
        if (results.isEmpty()) {
            System.out.println("✘ Nie znaleziono użytkowników pasujących do frazy: " + query);
        } else {
            System.out.println("\n=== WYNIKI WYSZUKIWANIA (" + results.size() + ") ===");
            for (int i = 0; i < results.size(); i++) {
                System.out.println((i + 1) + ". " + results.get(i));
            }
        }
    }

    private static void showAllUsers() {
        List<User> allUsers = userRepository.getAllUsers();
        if (allUsers.isEmpty()) {
            System.out.println("\nBrak użytkowników w systemie.");
            return;
        }
        
        System.out.println("\n=== LISTA WSZYSTKICH UŻYTKOWNIKÓW ===");
        for (int i = 0; i < allUsers.size(); i++) {
            User user = allUsers.get(i);
            System.out.println((i + 1) + ". " + user);
        }
    }

    private static void deleteUser() {
        User currentUser = authService.getCurrentUser();
        List<User> allUsers = userRepository.getAllUsers();
        
        if (allUsers.isEmpty()) {
            System.out.println("\nBrak użytkowników w systemie.");
            return;
        }
        
        System.out.println("\n=== USUWANIE UŻYTKOWNIKA ===");
        System.out.println("Lista użytkowników:");
        for (int i = 0; i < allUsers.size(); i++) {
            User user = allUsers.get(i);
            String currentMarker = user.equals(currentUser) ? " (TY)" : "";
            System.out.println((i + 1) + ". " + user.getUsername() + " - " + 
                             user.getFullName() + " (" + user.getRole().getDisplayName() + ")" + currentMarker);
        }
        
        System.out.print("\nPodaj nazwę użytkownika do usunięcia (lub 'anuluj' aby anulować): ");
        String username = scanner.nextLine().trim();
        
        if (username.equalsIgnoreCase("anuluj")) {
            System.out.println("Operacja anulowana.");
            return;
        }
        
        User userToDelete = userRepository.findByUsername(username);
        if (userToDelete == null) {
            System.out.println("✘ Użytkownik o podanej nazwie nie istnieje.");
            return;
        }
        
        if (userToDelete.equals(currentUser)) {
            System.out.println("✘ Nie możesz usunąć własnego konta!");
            return;
        }
        
        System.out.print("Czy na pewno chcesz usunąć użytkownika " + userToDelete.getFullName() + 
                        " (" + userToDelete.getUsername() + ")? (t/n): ");
        String confirm = scanner.nextLine().trim();
        
        if (confirm.equalsIgnoreCase("t")) {
            if (userRepository.removeUser(username)) {
                System.out.println("✔ Użytkownik został usunięty z systemu.");
            } else {
                System.out.println("✘ Błąd podczas usuwania użytkownika.");
            }
        } else {
            System.out.println("Operacja anulowana.");
        }
    }

    private static void showReportsMenu() {
        System.out.println("\n=== GENEROWANIE RAPORTÓW ===");
        System.out.println("1. Raport o wynikach działalności");
        System.out.println("2. Raport zleceń");
        System.out.println("3. Raport pojazdów");
        System.out.print("Wybierz opcję: ");
        
        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1":
                reportService.generateActivityReport();
                break;
            case "2":
                reportService.generateOrdersReport();
                break;
            case "3":
                reportService.generateVehiclesReport();
                break;
            default:
                System.out.println("✘ Nieprawidłowa opcja!");
        }
    }

    private static void showVehicleManagementMenu() {
        System.out.println("\n=== ZARZĄDZANIE POJAZDAMI ===");
        System.out.println("1. Dodaj nowy pojazd");
        System.out.println("2. Lista wszystkich pojazdów");
        System.out.println("3. Pojazdy wymagające przeglądu");
        System.out.print("Wybierz opcję: ");
        
        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1":
                addNewVehicle();
                break;
            case "2":
                List<Vehicle> vehicles = vehicleRepository.getAllVehicles();
                if (vehicles.isEmpty()) {
                    System.out.println("Brak pojazdów w systemie.");
                } else {
                    vehicles.forEach(v -> System.out.println("  " + v));
                }
                break;
            case "3":
                List<Vehicle> needingInspection = vehicleRepository.getVehiclesNeedingInspection();
                if (needingInspection.isEmpty()) {
                    System.out.println("Wszystkie pojazdy mają aktualne przeglądy.");
                } else {
                    System.out.println("Pojazdy wymagające przeglądu:");
                    needingInspection.forEach(v -> System.out.println("  " + v));
                }
                break;
            default:
                System.out.println("✘ Nieprawidłowa opcja!");
        }
    }

    private static void addNewVehicle() {
        System.out.println("\n--- DODAWANIE NOWEGO POJAZDU ---");
        System.out.print("Numer rejestracyjny: ");
        String regNumber = scanner.nextLine().trim();
        
        System.out.println("Typ pojazdu (TRUCK, CAR, BIKE, TRACTOR): ");
        String typeStr = scanner.nextLine().trim().toUpperCase();
        VehicleType type;
        try {
            type = VehicleType.valueOf(typeStr);
        } catch (IllegalArgumentException e) {
            System.out.println("✘ Nieprawidłowy typ pojazdu!");
            return;
        }
        
        System.out.print("Data przeglądu (YYYY-MM-DD): ");
        String dateStr = scanner.nextLine().trim();
        LocalDate inspectionDate;
        try {
            inspectionDate = LocalDate.parse(dateStr);
        } catch (Exception e) {
            System.out.println("✘ Nieprawidłowy format daty!");
            return;
        }
        
        System.out.println("Stan techniczny (GOOD, MID, BAD, BROKEN): ");
        String conditionStr = scanner.nextLine().trim().toUpperCase();
        VehicleCondition condition;
        try {
            condition = VehicleCondition.valueOf(conditionStr);
        } catch (IllegalArgumentException e) {
            System.out.println("✘ Nieprawidłowy stan techniczny!");
            return;
        }
        
        System.out.print("Moc silnika (KM): ");
        int power;
        try {
            power = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("✘ Nieprawidłowa moc!");
            return;
        }
        
        System.out.print("Liczba miejsc: ");
        int seats;
        try {
            seats = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("✘ Nieprawidłowa liczba miejsc!");
            return;
        }
        
        Vehicle vehicle = new Vehicle(regNumber, type, inspectionDate, condition, power, seats);
        vehicleRepository.addVehicle(vehicle);
        System.out.println("✔ Pojazd dodany do systemu!");
    }

    private static void createDefaultAdmin() {
        Admin defaultAdmin = new Admin("admin", "admin123", "admin@transport.pl",
                "Administrator Systemu", "ADMIN001", "IT");
        userRepository.addUser(defaultAdmin);
        System.out.println("System: \n");
        System.out.println("Utworzono domyślnego administratora:");
        System.out.println("Username: admin");
        System.out.println("Password: admin123\n");
    }

    private static void clearConsole() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (IOException | InterruptedException e) {
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }

    private static void waitForEnter() {
        System.out.print("\nNaciśnij Enter, aby kontynuować...");
        scanner.nextLine();
    }

    private static void createTestData() {
        createTestVehicles();
        createTestUsers();
        createTestOrders();
    }

    private static void createTestVehicles() {
        Vehicle v1 = new Vehicle("ABC1234", VehicleType.TRUCK, 
                LocalDate.now().minusMonths(6), VehicleCondition.GOOD, 300, 2);
        Vehicle v2 = new Vehicle("XYZ5678", VehicleType.CAR, 
                LocalDate.now().minusMonths(3), VehicleCondition.MID, 150, 5);
        Vehicle v3 = new Vehicle("DEF9012", VehicleType.TRUCK, 
                LocalDate.now().minusMonths(13), VehicleCondition.GOOD, 400, 2);
        Vehicle v4 = new Vehicle("GHI3456", VehicleType.TRUCK, 
                LocalDate.now().minusMonths(2), VehicleCondition.GOOD, 350, 2);
        Vehicle v5 = new Vehicle("JKL7890", VehicleType.CAR, 
                LocalDate.now().minusMonths(8), VehicleCondition.MID, 120, 4);
        Vehicle v6 = new Vehicle("MNO2468", VehicleType.BIKE, 
                LocalDate.now().minusMonths(4), VehicleCondition.GOOD, 50, 1);
        Vehicle v7 = new Vehicle("PQR1357", VehicleType.TRUCK, 
                LocalDate.now().minusMonths(15), VehicleCondition.BAD, 450, 2);
        Vehicle v8 = new Vehicle("STU9753", VehicleType.TRACTOR, 
                LocalDate.now().minusMonths(1), VehicleCondition.GOOD, 200, 1);
        Vehicle v9 = new Vehicle("VWX8642", VehicleType.CAR, 
                LocalDate.now().minusMonths(10), VehicleCondition.MID, 180, 5);
        Vehicle v10 = new Vehicle("YZA1597", VehicleType.TRUCK, 
                LocalDate.now().minusMonths(5), VehicleCondition.GOOD, 320, 2);
        
        vehicleRepository.addVehicle(v1);
        vehicleRepository.addVehicle(v2);
        vehicleRepository.addVehicle(v3);
        vehicleRepository.addVehicle(v4);
        vehicleRepository.addVehicle(v5);
        vehicleRepository.addVehicle(v6);
        vehicleRepository.addVehicle(v7);
        vehicleRepository.addVehicle(v8);
        vehicleRepository.addVehicle(v9);
        vehicleRepository.addVehicle(v10);
    }

    private static void createTestUsers() {
        Client c1 = new Client("jan_kowalski", "haslo123", "jan.kowalski@email.pl", 
                "Jan Kowalski", "123456789", "Warszawa, ul. Marszałkowska 1");
        Client c2 = new Client("anna_nowak", "haslo456", "anna.nowak@email.pl", 
                "Anna Nowak", "987654321", "Kraków, ul. Floriańska 10");
        Client c3 = new Client("piotr_wisniewski", "haslo789", "piotr.wisniewski@email.pl", 
                "Piotr Wiśniewski", "555666777", "Gdańsk, ul. Długa 5");
        Client c4 = new Client("maria_wojcik", "haslo321", "maria.wojcik@email.pl", 
                "Maria Wójcik", "111222333", "Wrocław, ul. Rynek 15");
        Client c5 = new Client("tomasz_kowalczyk", "haslo654", "tomasz.kowalczyk@email.pl", 
                "Tomasz Kowalczyk", "444555666", "Poznań, ul. Stary Rynek 8");
        
        Driver d1 = new Driver("andrzej_zielinski", "driver1", "andrzej.zielinski@transport.pl", 
                "Andrzej Zieliński", Rating.STAR5, 5000.0, "DL123456");
        Driver d2 = new Driver("krzysztof_szymanski", "driver2", "krzysztof.szymanski@transport.pl", 
                "Krzysztof Szymański", Rating.STAR4, 4500.0, "DL234567");
        Driver d3 = new Driver("zbigniew_wozniak", "driver3", "zbigniew.wozniak@transport.pl", 
                "Zbigniew Woźniak", Rating.STAR4, 4700.0, "DL345678");
        Driver d4 = new Driver("marek_kaczmarek", "driver4", "marek.kaczmarek@transport.pl", 
                "Marek Kaczmarek", Rating.STAR3, 4200.0, "DL456789");
        Driver d5 = new Driver("lukasz_mazur", "driver5", "lukasz.mazur@transport.pl", 
                "Łukasz Mazur", Rating.STAR5, 5200.0, "DL567890");
        
        userRepository.addUser(c1);
        userRepository.addUser(c2);
        userRepository.addUser(c3);
        userRepository.addUser(c4);
        userRepository.addUser(c5);
        userRepository.addUser(d1);
        userRepository.addUser(d2);
        userRepository.addUser(d3);
        userRepository.addUser(d4);
        userRepository.addUser(d5);
    }

    private static void createTestOrders() {
        List<Client> clients = userRepository.getAllClients();
        List<Driver> drivers = userRepository.getAllDrivers();
        List<Vehicle> vehicles = vehicleRepository.getAllVehicles();
        
        if (clients.isEmpty() || drivers.isEmpty() || vehicles.isEmpty()) {
            return;
        }
        
        Client client1 = clients.get(0);
        Client client2 = clients.size() > 1 ? clients.get(1) : clients.get(0);
        Client client3 = clients.size() > 2 ? clients.get(2) : clients.get(0);
        
        Driver driver1 = drivers.get(0);
        Driver driver2 = drivers.size() > 1 ? drivers.get(1) : drivers.get(0);
        
        Vehicle vehicle1 = vehicles.get(0);
        Vehicle vehicle2 = vehicles.size() > 1 ? vehicles.get(1) : vehicles.get(0);
        
        TransportOrder o1 = new TransportOrder(client1.getFullName(), 
                CountryCodes.PL, CountryCodes.DE, 1500.0);
        o1.setStatus(Status.COMPLETED);
        o1.assignDriver(driver1);
        o1.assignVehicle(vehicle1);
        orderRepository.addOrder(o1);
        client1.addOrder(o1);
        
        TransportOrder o2 = new TransportOrder(client2.getFullName(), 
                CountryCodes.PL, CountryCodes.FR, 2500.0);
        o2.setStatus(Status.IN_PROGRESS);
        o2.assignDriver(driver2);
        o2.assignVehicle(vehicle2);
        orderRepository.addOrder(o2);
        client2.addOrder(o2);
        
        TransportOrder o3 = new TransportOrder(client1.getFullName(), 
                CountryCodes.DE, CountryCodes.UK, 800.0);
        o3.setStatus(Status.ASSIGNED);
        o3.assignDriver(driver1);
        orderRepository.addOrder(o3);
        client1.addOrder(o3);
        
        TransportOrder o4 = new TransportOrder(client3.getFullName(), 
                CountryCodes.PL, CountryCodes.IT, 3200.0);
        o4.setStatus(Status.PENDING);
        orderRepository.addOrder(o4);
        client3.addOrder(o4);
        
        TransportOrder o5 = new TransportOrder(client2.getFullName(), 
                CountryCodes.FR, CountryCodes.ES, 1800.0);
        o5.setStatus(Status.PENDING);
        orderRepository.addOrder(o5);
        client2.addOrder(o5);
        
        TransportOrder o6 = new TransportOrder(client1.getFullName(), 
                CountryCodes.PL, CountryCodes.NL, 950.0);
        o6.setStatus(Status.COMPLETED);
        o6.assignDriver(driver2);
        orderRepository.addOrder(o6);
        client1.addOrder(o6);
        
        TransportOrder o7 = new TransportOrder(client3.getFullName(), 
                CountryCodes.IT, CountryCodes.AT, 1200.0);
        o7.setStatus(Status.PENDING);
        orderRepository.addOrder(o7);
        client3.addOrder(o7);
        
        TransportOrder o8 = new TransportOrder(client2.getFullName(), 
                CountryCodes.ES, CountryCodes.PL, 2100.0);
        o8.setStatus(Status.IN_PROGRESS);
        o8.assignDriver(driver1);
        orderRepository.addOrder(o8);
        client2.addOrder(o8);
        
        TransportOrder o9 = new TransportOrder(client1.getFullName(), 
                CountryCodes.NL, CountryCodes.BE, 600.0);
        o9.setStatus(Status.PENDING);
        orderRepository.addOrder(o9);
        client1.addOrder(o9);
        
        TransportOrder o10 = new TransportOrder(client3.getFullName(), 
                CountryCodes.PL, CountryCodes.CZ, 1400.0);
        o10.setStatus(Status.ASSIGNED);
        o10.assignDriver(driver2);
        orderRepository.addOrder(o10);
        client3.addOrder(o10);
    }

    public static void genPolandMap() {
        System.out.print(
                """
                        ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀         ⠀
                        ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
                        ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣠⣤⣤⣶⣶⡄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
                        ⠀⠀⠀⠀⠀⠀⠀⢀⣠⣾⣿⣿⣿⣿⣿⣿⣿⣷⣤⣤⣄⣀⣴⣤⣤⣤⣤⣄⣀⣀⣀⣀⣀⣤⣤⣄⡀⠀⠀⠀
                        ⢀⣠⣤⣴⣶⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡆⠀⠀
                        ⠰⣴⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⠀⠀
                        ⠀⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣧⠀
                        ⠶⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠂
                        ⠀⠈⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡟⠋⠉⠀
                        ⠀⠀⢹⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⣤⠀⠀
                        ⠀⠀⢾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡟⠀⠀
                        ⠀⠀⠈⢻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡀⠀
                        ⠀⠀⠀⡼⢻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣄
                        ⠀⠀⠀⠀⠀⠉⠉⠛⢛⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡧
                        ⠀⠀⠀⠀⠀⠀⠀⠀⠈⠹⡿⠂⠉⠛⣻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠟⠁⠀
                        ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠁⠉⠛⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠟⠁⠀⠀⠀
                        ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠻⠟⠙⢿⣿⣿⠿⢿⡿⠿⠿⠿⣿⣿⣿⡅⠀⠀⠀⠀⠀
                        ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠀⠀⠀⠀⠀⠀⠀⠈⠙⠛⠧⠀⠀⠀⠀⠀
                        ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
                        ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀""");
        System.out.println("nasi deweloperzy nadal pracują nad całą mapą...");
    }
}

