package src.main.kiedywakacje.com;
/*
 **************************************************************
 * REQUIRED FUNCTIONALITY
 * TODO:
 *  - Klienci mogą składać zlecenia na transport []
 *  - Kierowcy mogą przeglądać zlecenia i wyznaczać trasy []
 *  - System umożliwia monitorowanie stanu technicznego pojazdu [X]
 *  - Możliwość generowania raportów o wynikach działalnosći []
 *  - System rejestruje historię zleceń transportowych []
 **************************************************************
 */

import src.main.kiedywakacje.com.models.VehicleCondition;
import src.main.kiedywakacje.com.models.VehicleType;
import java.time.LocalDate;
import static src.main.kiedywakacje.com.Vehicle.CheckVehicleCondition;

public class TransportCompany {

    public static void main(String[] args) {
        System.out.println("Welcome to the Transport Company Management System!\n");

        Vehicle vehicle = new Vehicle("RTA82931", VehicleType.TRUCK, LocalDate.of(2025, 12, 18), VehicleCondition.GOOD, 100, 100);
        System.out.println(vehicle);
        CheckVehicleCondition(vehicle);
    }


}
