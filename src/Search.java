import Data.Trip;
import Data.Vehicle;
import Data.Driver;


import java.util.HashMap;

public class Search {
    private Driver driver;
    private Import importObj;

    public Search() {
        importObj = new Import();
    }

    public String findDriverByName(String seartchedFirstName, String searchedLastName){
        for(Driver driver : importObj.driverMap.values()) {
            if(driver.getFirstName().equals(seartchedFirstName) || driver.getLastName().equals(searchedLastName)) {
                return driver.getDriverID();
            }
        }
        return "No Driver found";
    }
}
