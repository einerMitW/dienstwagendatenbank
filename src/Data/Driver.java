package Data;

public class Driver {
    private String driverID;
    private String firstName;
    private String lastName;
    private String drivingLicence;

    public Driver(String driverID, String firstName, String lastName, String drivingLicence) {
        this.driverID = driverID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.drivingLicence = drivingLicence;
    }

    public String getDriverID() {
        return driverID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDrivingLicence() {
        return drivingLicence;
    }

    @Override
    public String toString() {
        return "=== Fahrer ===\n" +
               "Fahrer-ID: " + driverID + "\n" +
               "Vorname: " + firstName + "\n" +
               "Nachname: " + lastName + "\n" +
               "Führerschein: " + drivingLicence;
    }
}
