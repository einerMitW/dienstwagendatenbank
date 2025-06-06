package Data;

public class Vehicle {
    private String vID;
    private String vehicleBrand;
    private String licencePlate;

    public Vehicle(String vID, String vehicleBrand, String licencePlate) {
        this.vID = vID;
        this.vehicleBrand = vehicleBrand;
        this.licencePlate = licencePlate;
    }

    public String getvID() {
        return vID;
    }

    public String getVehicleBrand() {
        return vehicleBrand;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

}
