package app;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;

import Data.Driver;
import Data.Trip;
import Data.Vehicle;

/**
 * Handles the loading, parsing, and searching of data from the vehicle database file.
 * This class is responsible for populating the data models from a flat file
 * and providing public methods to query this data.
 */
public class Import {
    //    private Driver driver;
//    private Trip trip;
//    private Vehicle vehicle;
    public Map<String, Driver> driverMap;
    public Map<String, Vehicle> vehicleMap;
    public Map<String, Trip> tripMap;
    public ApplicationLogger logger;

    /**
     * Constructs an app.Import instance, which triggers the reading and parsing of the
     * vehicle database file. It initializes the data structures and logs any
     * file-related errors that occur during the process.
     */
    public Import() {
        logger = ApplicationLogger.getInstance();
        driverMap = new HashMap<>();
        vehicleMap = new HashMap<>();
        tripMap = new HashMap<>();

        //Extramly importatit that vehicleDB.txt is in CWD of project.
        final String filePath = "vehicleDB.txt";
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

    /**
     * Parses a single line from the database file and creates the corresponding
     * data object (Driver, Vehicle, or Trip). It uses the structure of the line
     * (number of columns, prefixes) to determine the object type and prevent duplicates.
     *
     * @param rawLine A single, unprocessed line of text from the data file.
     */
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

    /**
     * Cleans and tokenizes a raw string read from the data file.
     * This method prepares the string for parsing by removing irrelevant characters
     * and splitting it into distinct fields based on a delimiter.
     *
     * @param rawData The raw string line to be processed.
     * @return A string array containing the cleaned and separated data fields.
     */
    private String[] clearData(String rawData) {
        //Is ist better to worke whith global Array hear?
        String[] splitData;
        if (!rawData.startsWith("New_Entity")) {
            //replace whitspace with regex \s to null
            rawData = rawData.replace("\\s", "");
            splitData = rawData.split(",");
            return splitData;
        } else {
            return splitData = new String[0];
        }
    }

    /**
     * Finds all drivers whose first or last name partially or fully matches a given search term.
     * The search is performed in a case-insensitive manner.
     *
     * @param name The search term to match against driver names.
     * @return A List of {@code Driver} objects matching the criteria. Returns an empty list if no match is found.
     */
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

    /**
     * Finds all vehicles that match a given search term. The search includes the
     * vehicle's manufacturer, brand name, and license plate.
     *
     * @param searchTerm The value to search for in the vehicle attributes.
     * @return A List of {@code Vehicle} objects matching the criteria. Returns an empty list if no match is found.
     */
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

    /**
     * Retrieves a list of drivers who used a specific vehicle, identified by its license plate, on a given date.
     *
     * @param vehicleLicencePlate The license plate of the vehicle in question.
     * @param date The specific date (as a string) to check for vehicle usage.
     * @return A List of {@code Driver} objects who drove the specified vehicle on that day. Returns null if none are found.
     */
    public List<Driver> findDriverByVehicleIDandDate(String vehicleLicencePlate, String date) {
        List<Driver> foundDriver = new ArrayList<>();

        //loopin through trips to find all set parameters.
        for (Trip trip : tripMap.values()) {
            //geting corresponding vehicle obj from Trip
            Vehicle vehicle = vehicleMap.get(trip.getVehicleID());

            //Checking if vehicle found and if licence plate is equal.
            if (vehicle != null && vehicle.getLicencePlate().equals(vehicleLicencePlate)) {

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
     * Determines if a specific point in time falls within a given time interval.
     * The check is inclusive of the start and end times. The method can handle
     * both date-only strings and full date-time strings.
     *
     * @param startTime The starting timestamp of the interval.
     * @param endTime The ending timestamp of the interval.
     * @param date The point in time to check.
     * @return {@code true} if the time is within the interval, otherwise {@code false}.
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


    /**
     * Finds all other drivers who used the same vehicle(s) as a specified driver on a given day.
     *
     * @param searchedDriverID The ID of the driver whose vehicle usage is the basis of the search.
     * @param searchedDate The date on which to check for shared vehicle usage.
     * @return A List of formatted strings, each detailing another driver and the license plate of the shared vehicle.
     */
    public List<String> getUsersVehicleByDateAndDriverID(String searchedDriverID, String searchedDate) {
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