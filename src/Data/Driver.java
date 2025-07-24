package Data;

/**
 * Represents a driver with their personal information and driving license details.
 */
public class Driver {
    private String driverID;
    private String firstName;
    private String lastName;
    private String drivingLicence;

    /**
     * Constructs a new Driver object.
     *
     * @param driverID       The unique ID of the driver.
     * @param firstName      The first name of the driver.
     * @param lastName       The last name of the driver.
     * @param drivingLicence The driving license category of the driver.
     */
    public Driver(String driverID, String firstName, String lastName, String drivingLicence) {
        this.driverID = driverID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.drivingLicence = drivingLicence;
    }

    /**
     * Gets the driver's ID.
     *
     * @return The driver's ID.
     */
    public String getDriverID() {
        return driverID;
    }

    /**
     * Gets the driver's first name.
     *
     * @return The driver's first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Gets the driver's last name.
     *
     * @return The driver's last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Gets the driver's driving license category.
     *
     * @return The driver's driving license category.
     */
    public String getDrivingLicence() {
        return drivingLicence;
    }

    /**
     * Returns a string representation of the driver.
     *
     * @return A string containing the driver's details.
     */
    @Override
    public String toString() {
        return "=== Fahrer ===" + "\n" +
               "Fahrer-ID: " + driverID + "\n" +
               "Vorname: " + firstName + "\n" +
               "Nachname: " + lastName + "\n" +
               "Führerschein: " + drivingLicence;
    }
}