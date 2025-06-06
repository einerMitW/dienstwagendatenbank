import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import Data.Driver;
import Data.Trip;
import Data.Vehicle;

public class Import {
//    private Driver driver;
//    private Trip trip;
//    private Vehicle vehicle;
    Map<String,Driver> driverMap;
    Map<String,Vehicle> vehicleMap;
    Map<String, Trip> tripMap;

    public Import() {
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
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    private void saveData(String rawLine) {
        String [] data = clearData(rawLine);
        int datalen = data.length;
        //Filtering dataset for length and saving Data
        switch (datalen) {
            case 4:
                //Test for Vihicle or Driver Primary Key
                if (data[0].charAt(0) == 'V') {
                    Vehicle vehicleObj = new Vehicle(data[0], data[1], data[2]);
                    if (vehicleMap.get(data[0]) == null){
                        //put Vehicle in Hash map. Id as Atomary Key
                        vehicleMap.put(data[0], vehicleObj);
                    }

                }
                else {
                    //Writing Data as Driver Obj
                    Driver driverObj = new Driver(data[0], data[1], data[2], data[3]);
                    //When Driver dosent exists writing in Map. Id as Atomary Key
                    if (driverMap.get(data[0]) == null){
                        //put Driver in Hash map
                        driverMap.put(data[0],driverObj);
                    }
                }
                break;

            case 6:
                //Creating Trip OBJ
                Trip trip = new Trip(data[0], data[1], Integer.parseInt(data[2]), Integer.parseInt(data[3]), data[4], data[5]);
                //Check if Trip is allready existing
                if(tripMap.get(data[0]+data[1]) == null) {
                    //Putting trip in Hash map. Combinded Primary Key
                    tripMap.put(data[0]+data[1],trip);
                }
                break;

            default:
                System.out.println("Invalid Data");
                break;
        }
    }

    private String[] clearData(String rawData) {
        //Is ist better to worke whith global Array hear?
        String [] splitData;
        if(!rawData.startsWith("New_Entity")) {
            //replace whitspace with regex \\s to null
            rawData = rawData.replace("\\s", "");
            splitData = rawData.split(",");
            return splitData;
        } else {
            return splitData = new String[0];
        }
    }

}