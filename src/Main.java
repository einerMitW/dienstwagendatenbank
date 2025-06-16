import Data.Driver;
import Data.Vehicle;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        ApplicationLogger logger = new ApplicationLogger();
        Import importObj = new Import();
        logger.logInfo( "Import finished");
        
        

        //Checks for arugments java Prgramm is called when executed.
        if (args.length > 2 && args[0].length() > 0 && args[1].length() > 0) {
            //command mapping
            switch (args[0]) {
                case "--fahrersuche":
                    logger.logDebug( "Case 01 of command Mapping selected");
                    outputList(importObj.findDriverByNames(args[1]));
                    break;
                case "--fahrzeugsuche":
                    logger.logDebug( "Case 02 of command Mapping selected");
                    outputList(importObj.findVehicleBySearchTerm(args[1]));
                    break;
                case "--fahrerZeitpunkt":
                    logger.logDebug( "Case 03 of command Mapping selected");
                    break;
                case "--fahrerDatum=":
                    logger.logDebug( "Case 04 of command Mapping selected");
                    break;
                default:
                    logger.logDebug( "default Case of command Mapping selected: Invalid Command");
                    logger.logInfo( "Invalid Command");
                    break;
            }

        }
        //Test aria: Loose Test Calls
//        List<Driver> driverData = importObj.findDriverByNames("Tom");
//        outputList(driverData);
//
//        List<Vehicle> vehicleData = importObj.findVehicleBySearchTerm("BMW");
//        outputList(vehicleData);

        List<Driver> driverData2 = importObj.findDriverByVehicleIDandDate("SS-BC-4566", "2024-01-01T18:20:00");
        System.out.println(driverData2.get(0).getFirstName());
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
//
//    private static String[] clearArgs(String[] args) {
//        for (int i = 0; i < args.length; i++) {
//            args[i] = args[i].trim().replace("\\s", "");
//        }
//        return args;
//    }

}