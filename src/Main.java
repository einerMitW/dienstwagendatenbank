import Data.Driver;
import Data.Vehicle;

import java.util.logging.*;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Import importObj = new Import();
        System.out.println("Import don");
        //Checks for arugments java Prgramm is called when executed.
        if (args.length > 2 && args[0].length() > 0 && args[1].length() > 0) {
            //command mapping
            switch (args[0]) {
                case "--fahrersuche":
                    System.out.println("Case 01");
                    outputList(importObj.findDriverByNames(args[1]));
                    break;
                case "--fahrzeugsuche":
                    System.out.println("Case 02");
                    outputList(importObj.findVehicleBySearchTerm(args[1]));
                    break;
                case "--fahrerZeitpunkt":
                    break;
                case "--fahrerDatum=":
                    break;
                default:
                    System.out.println("Invalid Command");
                    break;
            }

        }
        //Test aria: Loose Test Calls
        System.out.println("Import finished");

        int a = importObj.driverMap.size();
        System.out.println(a);

        List<Driver> driverData = importObj.findDriverByNames("Tom");
        outputList(driverData);

        List<Vehicle> vehicleData = importObj.findVehicleBySearchTerm("BMW");
        outputList(vehicleData);
    }

    public static void outputList(List<?> listedData) {
        //Testing for null / no List objekt
        if (listedData != null && !listedData.isEmpty()) {
            for (Object data : listedData) {
                //Output Driver obj
                if (data instanceof Driver) {
                    Driver driver = (Driver) data;
                    System.out.println("=== Fahrer ===");
                    System.out.println("Fahrer-ID: " + driver.getDriverID());
                    System.out.println("Vorname: " + driver.getFirstName());
                    System.out.println("Nachname: " + driver.getLastName());
                    System.out.println("Führerschein: " + driver.getDrivingLicence());
                } //output Trip obj
//            else if (data instanceof Trip) {
//                Trip trip = (Trip) data;
//                System.out.println("=== Fahrt ===");
//                System.out.println("Fahrer-ID: " + trip.getDriverID());
//                System.out.println("Fahrzeug-ID: " + trip.getVehicleID());
//                System.out.println("Start-Kilometer: " + trip.getStartKm());
//                System.out.println("End-Kilometer: " + trip.getEndKm());
//                System.out.println("Startzeit: " + trip.getStartTime());
//                System.out.println("Endzeit: " + trip.getEndTime());
//            } output Vehicle obj
            else if (data instanceof Vehicle) {
                Vehicle vehicle = (Vehicle) data;
                System.out.println("=== Fahrzeug ===");
                System.out.println("Fahrzeug-ID: " + vehicle.getvID());
                System.out.println("Marke: " + vehicle.getVehicleBrand());
                System.out.println("Kennzeichen: " + vehicle.getLicencePlate());
            }
            System.out.println("-------------------");
            }
        } else {
            System.out.println("Keine Daten gefunden");
        }
    }

}