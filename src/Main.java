public class Main {
    public static void main(String[] args) {
        Import importObj = new Import();
        Search searchObj = new Search();

        //Checks for arugments java Prgramm is called when executed.
        if (args.length > 0 && args[0].length() > 0) {
        //right command mapping
        switch (args[0]) {
            case " --fahrersuche=":
                break;
            default:
                break;
        }
        }
        System.out.println(importObj.driverMap + "\n"+ "Driver Map");
        System.out.println(importObj.vehicleMap + "\n"+ "Vehicle Map");
        System.out.println(importObj.tripMap + "\n"+ "trip Map");
        System.out.println("Import finished");

        String id = searchObj.findDriverByName("Max", "Mustermann");
        System.out.println(id);
    }
}