package Data;

/**
 * Represents a vehicle in the company's fleet.
 */
public class Vehicle {
    private String vID;
    private String vehicleBrand;
    private String licencePlate;
    private String manufacturer;

    /**
     * Constructs a new Vehicle object.
     *
     * @param vID          The unique ID of the vehicle.
     * @param manufacturer The manufacturer of the vehicle.
     * @param vehicleBrand The model/brand of the vehicle.
     * @param licencePlate The license plate of the vehicle.
     */
    public Vehicle(String vID, String manufacturer, String vehicleBrand, String licencePlate) {
        this.vID = vID;
        this.manufacturer = manufacturer;
        this.vehicleBrand = vehicleBrand;
        this.licencePlate = licencePlate;
    }

    /**
     * Gets the vehicle's ID.
     *
     * @return The vehicle's ID.
     */
    public String getvID() {
        return vID;
    }

    /**
     * Gets the vehicle's brand/model.
     *
     * @return The vehicle's brand/model.
     */
    public String getVehicleBrand() {
        return vehicleBrand;
    }

    /**
     * Gets the vehicle's license plate.
     *
     * @return The vehicle's license plate.
     */
    public String getLicencePlate() {
        return licencePlate;
    }

    /**
     * Gets the vehicle's manufacturer.
     *
     * @return The vehicle's manufacturer.
     */
    public String getManufacturer() {
        return manufacturer;
    }

    /**
     * Returns a string representation of the vehicle.
     *
     * @return A string containing the vehicle's details.
     */
    @Override
    public String toString() {
        return "=== Fahrzeug ===" + "\n"+
               "Fahrzeug-ID: " + vID + "\n" +
               "Hersteller: " + manufacturer + "\n" +
               "Modell: " + vehicleBrand + "\n" +
               "Kennzeichen: " + licencePlate;
    }
}