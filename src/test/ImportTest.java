package test;

import Data.Driver;
import Data.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import app.Import;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ImportTest {
    private Import importobj;

    @BeforeEach
    void setUp() {
        // This ensures that for each test, we start with a fresh object,
        // so the data is loaded anew from the file each time.
        importobj = new Import();
    }

    @Test
    void testFindDriverByNames() {
        // From vehicleDB.txt: F003,Max,Hoffmann,BE
        List<Driver> actualDrivers = importobj.findDriverByNames("Hoffmann");
        assertFalse(actualDrivers.isEmpty(), "Should find at least one driver with the last name 'Hoffmann'.");

        // Check if at least one of the found drivers is indeed a "Hoffmann"
        assertTrue(actualDrivers.stream().anyMatch(d -> d.getLastName().equals("Hoffmann")), "At least one driver should have the last name 'Hoffmann'.");
    }

    @Test
    void testFindVehicleBySearchTerm_findsByLicensePlate() {
        // From vehicleDB.txt: V001,Opel,Corsa,S-BC-4566
        List<Vehicle> actualVehicles = importobj.findVehicleBySearchTerm("S-BC-4566");
        assertEquals(1, actualVehicles.size(), "Should find exactly one vehicle with this unique license plate.");
        assertEquals("Opel", actualVehicles.get(0).getManufacturer(), "The manufacturer should be Opel.");
    }

    @Test
    void testFindVehicleBySearchTerm_findsByManufacturer() {
        // From vehicleDB.txt: There are multiple Volkswagen
        List<Vehicle> actualVehicles = importobj.findVehicleBySearchTerm("Volkswagen");
        assertFalse(actualVehicles.isEmpty(), "Should find multiple vehicles from manufacturer 'Volkswagen'.");
        assertTrue(actualVehicles.stream().allMatch(v -> v.getManufacturer().equals("Volkswagen")), "All found vehicles should be from 'Volkswagen'.");
    }

    @Test
    void testFindDriverByVehicleIDandDate() {
        // From vehicleDB.txt: F029,V001,156127,156383,2024-01-01T18:17:53,2024-01-01T20:08:53
        // and F029,Anna,Becker,B
        // and V001,Opel,Corsa,S-BC-4566
        List<Driver> drivers = importobj.findDriverByVehicleIDandDate("S-BC-4566", "2024-01-01");
        assertNotNull(drivers, "The driver list should not be null.");
        assertFalse(drivers.isEmpty(), "Should find drivers for the given vehicle and date.");

        // There might be multiple drivers on the same day, so we check if the expected one is present.
        boolean foundExpectedDriver = drivers.stream()
                .anyMatch(d -> d.getFirstName().equals("Anna") && d.getLastName().equals("Becker"));
        assertTrue(foundExpectedDriver, "Expected driver 'Anna Becker' should be in the list of drivers.");
    }

    @Test
    void testGetUsersVehicleByDateAndDriverID() {
        // On 2024-01-01, driver F029 (Anna Becker) and F031 (Paul Wagner) both used vehicle V001 (S-BC-4566).
        // So, searching for other users of vehicles used by F029 on that day should return Paul Wagner.
        List<String> otherUsers = importobj.getUsersVehicleByDateAndDriverID("F029", "2024-01-01");

        assertNotNull(otherUsers, "Result list should not be null.");
        assertFalse(otherUsers.isEmpty(), "Should find other users of the same vehicle on the same day.");

        // The expected output format is "FirstName LastName (LicensePlate)"
        String expectedUser = "Paul Wagner (S-BC-4566)";
        assertTrue(otherUsers.contains(expectedUser), "The list of other users should contain 'Paul Wagner (S-BC-4566)'.");
    }
}