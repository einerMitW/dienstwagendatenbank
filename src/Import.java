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
                //Check if Trip is allready existing true combined Primary Key

                if (tripMap.get(data[0] +"_"+ data[1] +"_"+ data[4]) == null) {
                    //Putting trip in Hash map. Combinded Key
                    tripMap.put(data[0] +"_"+ data[1] +"_"+ data[4], trip);
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
        return foundVehicles;
    }

    //Feature1
    //New_Entity:fahrerId,fahrzeugId,startKm,endKm,startzeit,endzeit
    //F029,V001,156127,156383,2024-01-01T18:17:53,2024-01-01T20:08:53
    public List<Driver> findDriverByVehicleIDandDate(String vehicleLicencPlate, String date) {
        //even if only on driver can be found
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
                    if(driver != null) {
                        foundDriver.add(driver);
                        return foundDriver;
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
     * Sertches for all vehicles a Driver had one day. An all other who used the same Vehicle the same day.
     * @param searchedDriverID
     * @param searchedDate
     * @return List of Driver and Licencplat of Vehicle as List
     */
    List <String> getUsersVehicleByDateAndDriverID(String searchedDriverID, String searchedDate){
        List<Driver> foundOtherDrivers = new ArrayList<>();
        List<Vehicle> foundUsedVehicles = new ArrayList<>();
        Map <String, String> ergHash = new HashMap<>();
        List<String> ergList = new ArrayList<>();

        //Sertching all Trips form driver and date to find alle used Vehicles
        for(Trip trip : tripMap.values()){
            if(trip.getDriverID().equals(searchedDriverID) && trip.getStartTime().contains(searchedDate)){
                foundUsedVehicles.add(vehicleMap.get(trip.getVehicleID()));
            }
        }

        //Checking all driven vehickels, by serched Driver for other dirvers in same Timeframe
        for (Vehicle foundvehicle :foundUsedVehicles) {
            for(Trip trip : tripMap.values()){
                //The other Drivers has the same vehicle id and Timeframe for ther Trip
                if (foundvehicle.getvID().equals(trip.getVehicleID()) && !trip.getDriverID().equals(searchedDriverID) && trip.getStartTime().contains(searchedDate)){
                    Driver otherDriver = driverMap.get(trip.getDriverID());
                    //Checking if the found driver exists.
                    if (otherDriver != null) {
                        foundOtherDrivers.add(driverMap.get(trip.getDriverID()));
                        //Mapping Driver and Vehicel in ergList ready to return
                        String erg = otherDriver.getFirstName() + " " + otherDriver.getLastName() + "(" + foundvehicle.getLicencePlate() + ")";
                        ergList.add(erg);
                    }else logger.logWarning("Found driver is not in Driver Database");

                }
            }
        }

        logger.logInfo("Found: " + foundOtherDrivers.size() + " other drivers");
        return ergList;

        //Next step
    }


}