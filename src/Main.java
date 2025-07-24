import Data.Driver;
import Data.Vehicle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ApplicationLogger logger = ApplicationLogger.getInstance();
        Import importObj = new Import();
        logger.logInfo("Import of Data in Projek done \n ------------");

        String argumentData[] = getArgsData(args);
        logger.logDebug("Argument Data: " + Arrays.toString(argumentData));
        if (argumentData == null || argumentData.length == 0) {
            logger.logWarning("No arguments found");
            return;
        }

        switch (argumentData[0]) {
            case "--fahrersuche":
                logger.logInfo("Case 01 of command Mapping selected: --fahrersuche");
                outputList(importObj.findDriverByNames(argumentData[1]));
                break;
            case "--fahrzeugsuche":
                logger.logInfo("Case 02 of command Mapping selected: --fahrzeugsuche");
                outputList(importObj.findVehicleBySearchTerm(argumentData[1]));
                break;
            case "--fahrerZeitpunkt":
                logger.logInfo("Case 03 of command Mapping selected: --fahrerZeitpunkt");
                if (argumentData.length != 3) {
                    logger.logWarning("Wrong number of arguments for --fahrerZeitpunkt");
                }else {
                outputList(importObj.findDriverByVehicleIDandDate(argumentData[1], argumentData[2]));
                }
                break;
            case "--fahrerDatum":
                logger.logInfo("Case 04 of command Mapping selected: --fahrerDatum");
                if (argumentData.length != 3) {
                    logger.logWarning("Wrong number of arguments for --fahrerDatum");
                }else {
                    outputList(importObj.getUsersVehicleByDateAndDriverID(argumentData[1], argumentData[2]));
                }
                break;
            default:
                logger.logWarning("default Case of command Mapping selected: Command could not be processed \n Check spelling or try again");
                break;
        }

    }

    public static void outputList(List<?> listedData) {
        //Testing for null / no List objekt
        if (listedData != null && !listedData.isEmpty()) {
            for (Object data : listedData) {
                System.out.println(data);
                System.out.println("-------------------");
            }
        } else {
            System.out.println("Keine Daten gefunden");
        }
    }

    /**
     * Splitting the arguments to get the command and the rest.
     * @param argument
     * @return String[] with the command and the given Values.
     */
    public static String[] getArgsData(String[] argument) {
        if (argument == null || argument.length == 0 || argument[0] == null) {
            return null; // Sicherstellen, dass kein Fehler entsteht
        }

        List<String> resultList = new ArrayList<>();

        // Teile in "command" und "rest"
        String[] commandData = argument[0].split("=", 2);
        if (commandData.length != 2) {
            return new String[0]; // Ungültiges Format
        }

        // Füge den Befehl hinzu
        resultList.add(commandData[0]);

        // Teile den Rest nach ";"
        String[] values = commandData[1].split(";");
        resultList.addAll(Arrays.asList(values));

        // Rückgabe als Array
        return resultList.toArray(new String[0]);
    }
}