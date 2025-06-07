package Data;

public class Vehicle {
    private String vID;
    private String vehicleBrand;
    private String licencePlate;
    private String manufacturer;

    public Vehicle(String vID, String manufacturer, String vehicleBrand, String licencePlate) {
        this.vID = vID;
        this.manufacturer = manufacturer;
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

    public String getManufacturer() {
        return manufacturer;
    }
}
