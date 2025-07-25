package Data;

/**
 * Represents a trip made by a driver with a specific vehicle.
 */
public class Trip {
    private String driverID;
    private String vehicleID;
    private int startKm;
    private int endKm;
    private String startTime;
    private String endTime;

    /**
     * Constructs a new Trip object.
     *
     * @param driverID  The ID of the driver who made the trip.
     * @param vehicleID The ID of the vehicle used for the trip.
     * @param startKm   The starting kilometer reading of the vehicle.
     * @param endKm     The ending kilometer reading of the vehicle.
     * @param starttime The start time of the trip.
     * @param endtime   The end time of the trip.
     */
    public Trip(String driverID, String vehicleID, int startKm, int endKm, String starttime, String endtime) {
        this.driverID = driverID;
        this.vehicleID = vehicleID;
        this.startKm = startKm;
        this.endKm = endKm;
        this.startTime = starttime;
        this.endTime = endtime;
    }

    /**
     * Gets the ID of the driver.
     *
     * @return The driver's ID.
     */
    public String getDriverID() {
        return driverID;
    }

    /**
     * Gets the ID of the vehicle.
     *
     * @return The vehicle's ID.
     */
    public String getVehicleID() {
        return vehicleID;
    }

    /**
     * Gets the starting kilometer reading.
     *
     * @return The starting kilometer reading.
     */
    public int getStartKm() {
        return startKm;
    }

    /**
     * Gets the ending kilometer reading.
     *
     * @return The ending kilometer reading.
     */
    public int getEndKm() {
        return endKm;
    }

    /**
     * Gets the start time of the trip.
     *
     * @return The start time.
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * Gets the end time of the trip.
     *
     * @return The end time.
     */
    public String getEndTime() {
        return endTime;
    }
}