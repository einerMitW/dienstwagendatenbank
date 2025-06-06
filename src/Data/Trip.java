package Data;

public class Trip {
    private String driverID;
    private String vehicleID;
    private int startKm;
    private int endKm;
    private String startTime;
    private String endTime;

    public Trip(String driverID, String vehicleID, int startKm, int endKm, String starttime, String endtime) {
        this.driverID = driverID;
        this.vehicleID = vehicleID;
        this.startKm = startKm;
        this.endKm = endKm;
        this.startTime = starttime;
        this.endTime = endtime;
    }

    public String getDriverID() {
        return driverID;
    }

    public String getVehicleID() {
        return vehicleID;
    }

    public int getStartKm() {
        return startKm;
    }

    public int getEndKm() {
        return endKm;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }
}
