import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;

import Data.Driver;
import Data.Trip;
import Data.Vehicle;

public class Import {
    //    private Driver driver;
//    private Trip trip;
//    private Vehicle vehicle;
    Map<String, Driver> driverMap;
    Map<String, Vehicle> vehicleMap;
    Map<String, Trip> tripMap;
    ApplicationLogger logger;

    public Import() {
        logger = new ApplicationLogger();
        driverMap = new HashMap<>();
        vehicleMap = new HashMap<>();
        tripMap = new HashMap<>();

        final String filePath = "C:\\DHBW\\Semester2\\Java-Programmierung\\Dienstwagendatenbank\\vehicleDB.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                saveData(line);
            }
        } catch (FileNotFoundException e) {
            logger.logWarning("File not found");
        } catch (IOException e) {
            logger.logError("Error reading file: " + e.getMessage(), e);
        }
    }

    private void saveData(String rawLine) {
        String[] data = clearData(rawLine);
        int datalen = data.length;
        //Filtering dataset for length and saving Data
        switch (datalen) {
            case 4:
                //Test for Vihicle or Driver Primary Key
                if (data[0].charAt(0) == 'V') {
                    Vehicle vehicleObj = new Vehicle(data[0], data[1], data[2], data[3]);
                    //When Vehicle dosent exists writing in Map. Id as Key
                    if (vehicleMap.get(data[0]) == null) {
                        //put Vehicle in Hash map. Id as Key
                        vehicleMap.put(data[0], vehicleObj);
                    }

                } else {
                    //Writing Data as Driver Obj
                    Driver driverObj = new Driver(data[0], data[1], data[2], data[3]);
                    //When Driver dosent exists writing in Map. Id as Key
                    if (driverMap.get(data[0]) == null) {
                        //put Driver in Hash map
                        driverMap.put(data[0], driverObj);
                    }
                }
                break;

            case 6:
                //Creating Trip OBJ
                Trip trip = new Trip(data[0], data[1], Integer.parseInt(data[2]), Integer.parseInt(data[3]), data[4], data[5]);
                //Check if Trip is allready existing thru combined Primary Key
                if (tripMap.get(data[0] + data[1]) == null) {
                    //Putting trip in Hash map. Combinded Primary Key
                    tripMap.put(data[0] + data[1], trip);
                }
                break;

            default:
                System.out.println("Invalid Data");
                break;
        }
    }

    private String[] clearData(String rawData) {
        //Is ist better to worke whith global Array hear?
        String[] splitData;
        if (!rawData.startsWith("New_Entity")) {
            //replace whitspace with regex \\s to null
            rawData = rawData.replace("\\s", "");
            splitData = rawData.split(",");
            return splitData;
        } else {
            return splitData = new String[0];
        }
    }

    public List<Driver> findDriverByNames(String name) {
        List<Driver> foundDrivers = new ArrayList<>();
        String[] splitName;

        if (name.contains(" ")) {
            splitName = name.split(" ");
        } else {
            splitName = new String[]{name};
        }

        for (Driver driver : driverMap.values()) {
            String firstNameDriver = driver.getFirstName();
            String lastNameDriver = driver.getLastName();

            if (firstNameDriver.contains(splitName[0]) ||
                    (splitName.length > 1 && lastNameDriver.contains(splitName[1]))) {
                foundDrivers.add(driver);
            }
        }
        return foundDrivers;
    }

    public List<Vehicle> findVehicleBySearchTerm(String searchTerm) {
        //List of all found Objects whith sertch Propertys
        List<Vehicle> foundVehicles = new ArrayList<>();

        for (Vehicle vehicle : vehicleMap.values()) {
            //Checking what user is looking for
            if (vehicle.getManufacturer().contains(searchTerm) || vehicle.getVehicleBrand().contains(searchTerm) || vehicle.getLicencePlate().contains(searchTerm)) {
                foundVehicles.add(vehicle);
            }
        }
        return foundVehicles;
    }

    //New_Entity:fahrerId,fahrzeugId,startKm,endKm,startzeit,endzeit
    //F029,V001,156127,156383,2024-01-01T18:17:53,2024-01-01T20:08:53
    public List<Driver> findDriverByVehicleIDandDate(String vehicleLicencPlate, String date) {
        //even if only on driver can be found
        List<Driver> foundDriver = new ArrayList<>();

        //loopin through trips to find a trip with correct timeframe
        for (Trip trip : tripMap.values()) {
            if (timeWithin(trip.getStartTime(), trip.getEndTime(), date)) {
                //checking if given vehicle is correct tripvehicle
                for (Vehicle vehicletrip : vehicleMap.values()) {
                    if (vehicletrip.getLicencePlate().equals(vehicleLicencPlate)) {
                        //adding driver to list if correct vehicle and timeframe is found
                        foundDriver.add(driverMap.get(trip.getDriverID()));
                    }else logger.logDebug("Trip found but not with correct vehicle");
                }
                return foundDriver;
            } else logger.logDebug("Trip is not within correct timeframe");
        }

        return null;
    }

    private boolean timeWithin(String startTime, String endTime, String date) {
        try {
            //Parsin in Date Object for easy comparision
            LocalDateTime start = LocalDateTime.parse(startTime);
            LocalDateTime end = LocalDateTime.parse(endTime);
            LocalDateTime checkDate = LocalDateTime.parse(date);
            logger.logDebug("Starttime: " + start + " Endtime: " + end + " Date: " + checkDate);

            if ((checkDate.equals(start) || checkDate.equals(end)) || (checkDate.isAfter(start) && checkDate.isBefore(end))) {

                logger.logDebug("Date is equal ore within start and end");
                return true;
            } else {
                return false;
            }
        } catch (DateTimeParseException e) {
            logger.logError("Error parsing date", e);
        }
        return false;
    }


}