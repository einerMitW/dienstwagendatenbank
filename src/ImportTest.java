import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import Data.Driver;
import Data.Vehicle;

import java.time.LocalDate;
import java.util.List;

class ImportTest {

    private static Import importer;

    @BeforeAll
    static void setUp() {
        importer = new Import();
    }

    @Test
    void testFindDriverByNames() {
        String name = "Sophie Mueller";
        List<Driver> drivers = importer.findDriverByNames(name);
        assertNotNull(drivers, "Fahrer sollte gefunden werden.");
        assertFalse(drivers.isEmpty(), "Die Liste der Fahrer sollte nicht leer sein.");
        assertTrue(drivers.stream().anyMatch(d -> d.getFirstName().equals("Sophie") && d.getLastName().equals("Mueller")), "Sophie Mueller sollte gefunden werden.");
    }

    @Test
    void testFindVehicleBySearchTerm() {
        String searchTerm = "S-BC-4566";
        List<Vehicle> vehicles = importer.findVehicleBySearchTerm(searchTerm);
        assertNotNull(vehicles, "Die Fahrzeugliste sollte nicht null sein.");
        assertFalse(vehicles.isEmpty(), "Es sollte mindestens ein Fahrzeug gefunden werden.");
        assertEquals(searchTerm, vehicles.get(0).getLicencePlate(), "Kennzeichen sollte übereinstimmen.");
    }

    @Test
    void testFindDriverByVehicleIDandDate() {
        String vehicleLicencePlate = "S-BC-4566";
        String date = "2024-01-25";
        List<Driver> drivers = importer.findDriverByVehicleIDandDate(vehicleLicencePlate, date);
        assertNotNull(drivers, "Fahrer für das Fahrzeug und Datum sollte gefunden werden.");
        assertFalse(drivers.isEmpty(), "Die Liste der Fahrer sollte nicht leer sein.");
        assertTrue(drivers.stream().anyMatch(d -> d.getFirstName().equals("Sophie") && d.getLastName().equals("Fischer")), "Sophie Fischer sollte gefunden werden.");
        assertTrue(drivers.stream().anyMatch(d -> d.getFirstName().equals("Lena") && d.getLastName().equals("Becker")), "Lena Becker sollte gefunden werden.");
    }

    @Test
    void testGetUsersVehicleByDateAndDriverID() {
        String driverID = "F003";
        String date = "2024-08-13";
        List<String> otherUsers = importer.getUsersVehicleByDateAndDriverID(driverID, date);
        assertNotNull(otherUsers, "Die Liste der anderen Benutzer sollte nicht null sein.");
        assertFalse(otherUsers.isEmpty(), "Die Liste der anderen Benutzer sollte nicht leer sein.");
        assertTrue(otherUsers.contains("Ben Wagner(S-GH-3277)"), "Die Liste sollte Ben Wagner enthalten.");
        assertTrue(otherUsers.contains("Mia Hoffmann(S-GH-3277)"), "Die Liste sollte Mia Hoffmann enthalten.");
    }
}
