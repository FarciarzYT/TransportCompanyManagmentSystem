package com.kiedywakacje.transport;

import com.kiedywakacje.transport.models.UserRole;
import com.kiedywakacje.transport.models.VehicleCondition;
import com.kiedywakacje.transport.models.VehicleType;
import java.time.LocalDate;
import java.util.Scanner;

/**
 * Główna klasa systemu zarządzania firmą transportową.
 * Wymagana funkcjonalność do oceny:
 * - Klienci mogą składać zlecenia na transport [X]
 * - Kierowcy mogą przeglądać zlecenia i wyznaczać trasy []
 * - System umożliwia monitorowanie stanu technicznego pojazdu [X]
 * - Możliwość generowania raportów o wynikach działalności []
 * - System rejestruje historię zleceń transportowych []
 * Wymagana funkcjonalność dla użytkownika:
 * - login/register for Client/Driver/Admin [X]
 * - full map for better user experience
 */
public class TransportCompany {
    private static UserRepository userRepository;
    private static AuthenticationService authService;
    private static Scanner scanner;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        userRepository = new UserRepository();
        authService = new AuthenticationService(userRepository);

        createDefaultAdmin();

        System.out.println("=== SYSTEM ZARZĄDZANIA FIRMĄ TRANSPORTOWĄ ===\n");
        boolean running = true;
        while (running) {
            if (!authService.isLoggedIn()) {
                running = showMainMenu();
            } else {
                running = showUserMenu();
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
        System.out.println("2. Sprawdzanie stanu pojazdu");
        System.out.println("3. Wyświetl mapę Polski");

        if (role == UserRole.CLIENT) {
            System.out.println("4. Moja historia zleceń");
        } else if (role == UserRole.DRIVER) {
            System.out.println("4. Przeglądaj dostępne zlecenia");
        } else if (role == UserRole.ADMIN) {
            System.out.println("4. Zarządzanie użytkownikami");
            System.out.println("5. Rejestracja kierowcy");
            System.out.println("6. Rejestracja administratora");
        }

        System.out.println("0. Wyloguj się");
        System.out.print("Wybierz opcję: ");

        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1":
                if (role == UserRole.CLIENT) {
                    TransportOrder.placeOrder(scanner);
                } else {
                    System.out.println("✘ Tylko klienci mogą składać zlecenia!");
                }
                break;
            case "2":
                showVehicleCheckExample();
                break;
            case "3":
                genPolandMap();
                break;
            case "4":
                if (role == UserRole.CLIENT) {
                    showClientOrderHistory();
                } else if (role == UserRole.DRIVER) {
                    System.out.println("Funkcjonalność w przygotowaniu...");
                } else if (role == UserRole.ADMIN) {
                    showUserManagement();
                }
                break;
            case "5":
                if (role == UserRole.ADMIN) {
                    authService.registerDriver(scanner);
                } else {
                    System.out.println("✘ Tylko administratorzy mogą rejestrować kierowców!");
                }
                break;
            case "6":
                if (role == UserRole.ADMIN) {
                    authService.registerAdmin(scanner);
                } else {
                    System.out.println("✘ Tylko administratorzy mogą rejestrować administratorów!");
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

    private static void showVehicleCheckExample() {
        System.out.println("\n--- Przykład sprawdzania pojazdu ---");
        Vehicle exampleVehicle = new Vehicle(
            "ABC1234",
            VehicleType.TRUCK,
            LocalDate.now().minusMonths(6),
            VehicleCondition.GOOD,
            300,
            2
        );
        Vehicle.checkVehicleCondition(exampleVehicle);
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

    private static void showUserManagement() {
        System.out.println("\n=== ZARZĄDZANIE UŻYTKOWNIKAMI ===");
        System.out.println("Liczba użytkowników: " + userRepository.getUserCount());
        System.out.println("Klienci: " + userRepository.getAllClients().size());
        System.out.println("Kierowcy: " + userRepository.getAllDrivers().size());
        System.out.println("Administratorzy: " + userRepository.getAllAdmins().size());
    }

    private static void createDefaultAdmin() {
        Admin defaultAdmin = new Admin("admin", "admin123", "admin@transport.pl",
                "Administrator Systemu", "ADMIN001", "IT");
        userRepository.addUser(defaultAdmin);
        System.out.println("===============SYSTEM LOG======================");
        System.out.println("Utworzono domyślnego administratora:");
        System.out.println("Username: admin");
        System.out.println("Password: admin123\n");
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
    }
}

