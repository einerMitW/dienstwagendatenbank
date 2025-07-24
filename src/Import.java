import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.logging.Logger;

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
        logger = ApplicationLogger.getInstance();
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
                //Check if Trip is allready existing true combined Primary Key
                if (tripMap.get(data[0] + "_" + data[1] + "_" + data[4]) == null) {
                    //Putting trip in Hash map. Combinded Key
                    tripMap.put(data[0] + "_" + data[1] + "_" + data[4], trip);
                }
                break;

            default:
                logger.logWarning("While Importing Data: Invalid Dataset found");
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

    //Fahrer suche
    public List<Driver> findDriverByNames(String name) {
        List<Driver> foundDrivers = new ArrayList<>();
        String searchTerm = name.toLowerCase(); // Suchbegriff in Kleinbuchstaben umwandeln

        logger.logInfo("Searching for driver with term: " + searchTerm);
        for (Driver driver : driverMap.values()) {
            String firstNameDriver = driver.getFirstName().toLowerCase(); // Vorname des Fahrers in Kleinbuchstaben
            String lastNameDriver = driver.getLastName().toLowerCase();   // Nachname des Fahrers in Kleinbuchstaben

            // Prüfen, ob der Suchbegriff im Vor- oder Nachnamen enthalten ist
            if (firstNameDriver.contains(searchTerm) ||
                    lastNameDriver.contains(searchTerm)) {
                foundDrivers.add(driver);
            }
        }
        logger.logInfo("Found " + foundDrivers.size() + " Drivers for this Name");
        return foundDrivers;
    }

    //Fahrzeug suche
    public List<Vehicle> findVehicleBySearchTerm(String searchTerm) {
        //List of all found Objects whith sertch Propertys
        List<Vehicle> foundVehicles = new ArrayList<>();

        for (Vehicle vehicle : vehicleMap.values()) {
            //Checking what user is looking for
            if (vehicle.getManufacturer().contains(searchTerm) || vehicle.getVehicleBrand().contains(searchTerm) || vehicle.getLicencePlate().contains(searchTerm)) {
                foundVehicles.add(vehicle);
            }
        }
        logger.logInfo("Found " + foundVehicles.size() + " Vehicles for this SearchTerm");
        return foundVehicles;
    }

    //Feature1
    //New_Entity:fahrerId,fahrzeugId,startKm,endKm,startzeit,endzeit
    //F029,V001,156127,156383,2024-01-01T18:17:53,2024-01-01T20:08:53
    public List<Driver> findDriverByVehicleIDandDate(String vehicleLicencPlate, String date) {
        List<Driver> foundDriver = new ArrayList<>();

        //loopin through trips to find all set parameters.
        for (Trip trip : tripMap.values()) {
            //geting corresponding vehicle obj from Trip
            Vehicle vehicle = vehicleMap.get(trip.getVehicleID());

            //Checking if vehicle found and if licence plate is equal.
            if (vehicle != null && vehicle.getLicencePlate().equals(vehicleLicencPlate)) {

                //Checking if timeframe is correct.
                if (timeWithin(trip.getStartTime(), trip.getEndTime(), date)) {
                    Driver driver = driverMap.get(trip.getDriverID());
                    //Checking if driver exists
                    if (driver != null) {
                        foundDriver.add(driver);
                    }
                }
            }

        }
        logger.logInfo("Found " + foundDriver.size() + "Drivers for this Date and LicensePlate");
        //if no dirver is found method returns null
        return foundDriver.size() == 0 ? null : foundDriver;
    }

    /**
     * Compars if a Given Time is eaqual or within a timeframe
     * Sertch Date can be given as Date oder Date and Time.
     *
     * @param startTime
     * @param endTime
     * @param date
     * @return boolean
     */
    private boolean timeWithin(String startTime, String endTime, String date) {
        //Parsing Date in fitting Format
        LocalDateTime startDateTime = LocalDateTime.parse(startTime);
        LocalDateTime endDateTime = LocalDateTime.parse(endTime);

        try {
            if (date.contains("T")) {
                //Parsin in Date Object with date and time
                LocalDateTime checkDateTime = LocalDateTime.parse(date);

                if ((checkDateTime.equals(startDateTime) || checkDateTime.equals(endDateTime)) || (checkDateTime.isAfter(startDateTime) && checkDateTime.isBefore(endDateTime))) {
                    return true;
                } else {
                    return false;
                }
            } else {
                // Parsin in Date Object with date only
                LocalDate startDate = startDateTime.toLocalDate();
                LocalDate endDate = endDateTime.toLocalDate();
                LocalDate checkDate = LocalDate.parse(date);

                if ((checkDate.equals(startDate) || checkDate.equals(endDate)) || (checkDate.isAfter(startDate) && checkDate.isBefore(endDate))) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (DateTimeParseException e) {
            logger.logError("Error parsing date", e);
        }
        return false;
    }


    //Feature2

    /**
     * Searches for all vehicles a driver had on one day, and all other drivers who used the same vehicle on the same day.
     *
     * @param searchedDriverID The ID of the driver to search for.
     * @param searchedDate     The date to search on (format: YYYY-MM-DD).
     * @return A list of strings, where each string contains the name of another driver and the license plate of the shared vehicle.
     */
    List<String> getUsersVehicleByDateAndDriverID(String searchedDriverID, String searchedDate) {
        Set<String> usedVehicleIDs = new HashSet<>();
        List<String> resultList = new ArrayList<>();
        LocalDate searchDate;

        try {
            searchDate = LocalDate.parse(searchedDate);
        } catch (DateTimeParseException e) {
            logger.logError("Invalid date format for search: " + searchedDate, e);
            return resultList; // Return empty list if date is invalid
        }

        // Step 1: Find all unique vehicle IDs used by the searched driver on the given date.
        for (Trip trip : tripMap.values()) {
            LocalDate tripDate = LocalDateTime.parse(trip.getStartTime()).toLocalDate();
            if (trip.getDriverID().equals(searchedDriverID) && tripDate.equals(searchDate)) {
                usedVehicleIDs.add(trip.getVehicleID());
            }
        }

        if (usedVehicleIDs.isEmpty()) {
            logger.logInfo("No vehicles found for driver " + searchedDriverID + " on " + searchedDate);
            return resultList;
        }

        // Step 2: Find all other drivers who used any of those vehicles on the same day.
        for (Trip trip : tripMap.values()) {
            LocalDate tripDate = LocalDateTime.parse(trip.getStartTime()).toLocalDate();
            // Check if the trip is on the same day, involves one of the used vehicles, but is by a different driver.
            if (tripDate.equals(searchDate) && usedVehicleIDs.contains(trip.getVehicleID()) && !trip.getDriverID().equals(searchedDriverID)) {
                Driver otherDriver = driverMap.get(trip.getDriverID());
                Vehicle vehicle = vehicleMap.get(trip.getVehicleID());

                if (otherDriver != null && vehicle != null) {
                    String resultString = otherDriver.getFirstName() + " " + otherDriver.getLastName() + " (" + vehicle.getLicencePlate() + ")";
                    // Avoid adding duplicate entries
                    if (!resultList.contains(resultString)) {
                        resultList.add(resultString);
                    }
                } else {
                    if (otherDriver == null) {
                        logger.logWarning("Found trip with a driver ID that is not in the driver database: " + trip.getDriverID());
                    }
                    if (vehicle == null) {
                        logger.logWarning("Found trip with a vehicle ID that is not in the vehicle database: " + trip.getVehicleID());
                    }
                }
            }
        }

        logger.logInfo("Found: " + resultList.size() + " other drivers using the same vehicles on " + searchedDate);
        return resultList;
    }


}