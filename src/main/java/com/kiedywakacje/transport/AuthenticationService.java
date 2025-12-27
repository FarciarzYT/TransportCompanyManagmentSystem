package com.kiedywakacje.transport;

import com.kiedywakacje.transport.models.Rating;
import com.kiedywakacje.transport.models.UserRole;

import java.util.Scanner;

public class AuthenticationService {
    private UserRepository userRepository;
    private User currentUser;

    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.currentUser = null;
    }

    public Client registerClient(Scanner scanner) {
        System.out.println("\n=== REJESTRACJA KLIENTA ===");
        
        System.out.print("Nazwa użytkownika: ");
        String username = scanner.nextLine().trim();
        
        if (userRepository.usernameExists(username)) {
            System.out.println("✘ Nazwa użytkownika już istnieje!");
            return null;
        }

        System.out.print("Hasło: ");
        String password = scanner.nextLine().trim();
        if (password.isEmpty()) {
            System.out.println("✘ Hasło nie może być puste!");
            return null;
        }

        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        if (userRepository.emailExists(email)) {
            System.out.println("✘ Email już jest zarejestrowany!");
            return null;
        }

        System.out.print("Pełne imię i nazwisko: ");
        String fullName = scanner.nextLine().trim();

        System.out.print("Numer telefonu: ");
        String phoneNumber = scanner.nextLine().trim();

        System.out.print("Adres: ");
        String address = scanner.nextLine().trim();

        Client client = new Client(username, password, email, fullName, phoneNumber, address);
        
        if (userRepository.addUser(client)) {
            System.out.println("✔ Rejestracja zakończona pomyślnie!");
            return client;
        } else {
            System.out.println("✘ Błąd podczas rejestracji!");
            return null;
        }
    }

    public Driver registerDriver(Scanner scanner) {
        System.out.println("\n=== REJESTRACJA KIEROWCY ===");
        
        System.out.print("Nazwa użytkownika: ");
        String username = scanner.nextLine().trim();
        
        if (userRepository.usernameExists(username)) {
            System.out.println("✘ Nazwa użytkownika już istnieje!");
            return null;
        }

        System.out.print("Hasło: ");
        String password = scanner.nextLine().trim();
        if (password.isEmpty()) {
            System.out.println("✘ Hasło nie może być puste!");
            return null;
        }

        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        if (userRepository.emailExists(email)) {
            System.out.println("✘ Email już jest zarejestrowany!");
            return null;
        }

        System.out.print("Pełne imię i nazwisko: ");
        String fullName = scanner.nextLine().trim();

        System.out.print("Numer prawa jazdy: ");
        String licenseNumber = scanner.nextLine().trim();

        System.out.print("Wynagrodzenie: ");
        double salary;
        try {
            salary = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("✘ Nieprawidłowy format wynagrodzenia!");
            return null;
        }

        System.out.println("Ocena (STAR1-STAR5): ");
        String ratingStr = scanner.nextLine().trim().toUpperCase();
        Rating rating;
        try {
            rating = Rating.valueOf(ratingStr);
        } catch (IllegalArgumentException e) {
            rating = Rating.STAR3; // Domyślna ocena
            System.out.println("Ustawiono domyślną ocenę: STAR3");
        }

        Driver driver = new Driver(username, password, email, fullName, rating, salary, licenseNumber);
        
        if (userRepository.addUser(driver)) {
            System.out.println("✔ Kierowca zarejestrowany pomyślnie!");
            return driver;
        } else {
            System.out.println("✘ Błąd podczas rejestracji!");
            return null;
        }
    }

    public Admin registerAdmin(Scanner scanner) {
        System.out.println("\n=== REJESTRACJA ADMINISTRATORA ===");
        
        System.out.print("Nazwa użytkownika: ");
        String username = scanner.nextLine().trim();
        
        if (userRepository.usernameExists(username)) {
            System.out.println("✘ Nazwa użytkownika już istnieje!");
            return null;
        }

        System.out.print("Hasło: ");
        String password = scanner.nextLine().trim();
        if (password.isEmpty()) {
            System.out.println("✘ Hasło nie może być puste!");
            return null;
        }

        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        if (userRepository.emailExists(email)) {
            System.out.println("✘ Email już jest zarejestrowany!");
            return null;
        }

        System.out.print("Pełne imię i nazwisko: ");
        String fullName = scanner.nextLine().trim();

        System.out.print("ID administratora: ");
        String adminId = scanner.nextLine().trim();

        System.out.print("Dział: ");
        String department = scanner.nextLine().trim();

        Admin admin = new Admin(username, password, email, fullName, adminId, department);
        
        if (userRepository.addUser(admin)) {
            System.out.println("✔ Administrator zarejestrowany pomyślnie!");
            return admin;
        } else {
            System.out.println("✘ Błąd podczas rejestracji!");
            return null;
        }
    }

    public User login(Scanner scanner) {
        System.out.println("\n=== LOGOWANIE ===");
        
        System.out.print("Nazwa użytkownika: ");
        String username = scanner.nextLine().trim();
        
        System.out.print("Hasło: ");
        String password = scanner.nextLine().trim();

        User user = userRepository.findByUsername(username);
        
        if (user == null) {
            System.out.println("✘ Nieprawidłowa nazwa użytkownika lub hasło!");
            return null;
        }

        if (!user.isActive()) {
            System.out.println("✘ Konto jest nieaktywne!");
            return null;
        }

        if (!user.checkPassword(password)) {
            System.out.println("✘ Nieprawidłowa nazwa użytkownika lub hasło!");
            return null;
        }

        this.currentUser = user;
        System.out.println("✔ Zalogowano pomyślnie jako: " + user.getFullName() + " (" + user.getRole().getDisplayName() + ")");
        return user;
    }

    public void logout() {
        if (currentUser != null) {
            System.out.println("Wylogowano użytkownika: " + currentUser.getUsername());
            currentUser = null;
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public boolean hasRole(UserRole role) {
        return currentUser != null && currentUser.getRole() == role;
    }
}

