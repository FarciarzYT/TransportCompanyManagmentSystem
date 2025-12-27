# System Zarządzania Firmą Transportową

Kompleksowy system do zarządzania operacjami firmy transportowej, napisanym w języku Java. Aplikacja umożliwia obsługę zleceń transportowych, zarządzanie flotą pojazdów oraz administrację użytkownikami o różnych uprawnieniach.

## Główne Funkcjonalności

### 1. Zarządzanie Użytkownikami
System obsługuje trzy główne role użytkowników:
*   **Administrator**: Pełne zarządzanie systemem, użytkownikami, pojazdami oraz dostęp do raportów.
*   **Kierowca**: Przeglądanie przypisanych zleceń, zmiana statusu zleceń (rozpoczęcie, zakończenie).
*   **Klient**: Składanie nowych zleceń transportowych i przeglądanie historii swoich zamówień.

### 2. Obsługa Zleceń (Transport Orders)
*   Tworzenie nowych zleceń z określeniem kraju pochodzenia, przeznaczenia oraz wagi ładunku.
*   Śledzenie statusu zlecenia: `PENDING`, `ASSIGNED`, `IN_PROGRESS`, `COMPLETED`, `CANCELLED`.
*   Przypisywanie kierowców i pojazdów do konkretnych zadań.

### 3. Zarządzanie Flotą (Vehicle Management)
*   Ewidencja pojazdów (numer rejestracyjny, typ, moc silnika, liczba miejsc).
*   Monitorowanie stanu technicznego i terminów przeglądów okresowych.
*   Automatyczne powiadomienia o konieczności wykonania przeglądu.

### 4. Raportowanie i Statystyki
*   Generowanie raportów działalności (statystyki zleceń, pojazdów i użytkowników).
*   Szczegółowe zestawienia zleceń i floty.
*   Podsumowania wagowe przewiezionych ładunków.

## Architektura i Technologie
*   **Język**: Java
*   **Wzorce projektowe**: Wykorzystanie polimorfizmu, enkapsulacji oraz klas abstrakcyjnych (np. klasa bazowa `User`).
*   **Struktura danych**: Repozytoria zarządzające danymi w pamięci (In-memory repositories).
*   **Interfejs**: Konsolowy (CLI) z interaktywnym menu.

## Struktura Projektu
*   `com.kiedywakacje.transport` - Główne klasy logiki biznesowej.
*   `com.kiedywakacje.transport.models` - Enuma i klasy pomocnicze (statusy, typy pojazdów, kraje).

## Uruchomienie
Główna klasa uruchomieniowa to `com.kiedywakacje.transport.TransportCompany`. Po uruchomieniu system tworzy domyślne dane testowe (użytkowników, pojazdy i zlecenia) dla ułatwienia prezentacji funkcjonalności.

Domyślne konto administratora:
*   **Login**: admin
*   **Hasło**: admin123

Zalecam testowanie projektu w innej konsoli niż w tej dostępnej przez Idea od jetbrians poniewaz nie chce wspolpracowac z metoda clearConsole() 
