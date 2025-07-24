import Data.Driver;
import Data.Vehicle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Main class of the company car management application.
 * It proccesses command-line arguents to perform various searches on the vehicle database.
 */
public class Main {
    /**
     * The main entry point for the application.
     * Reads command-line arguments, initializes the necessary objects, and
     * controls the main flow of the application based on the provided arguments.
     *
     * @param args The command-line arguments passed to the program.
     */
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
                System.out.println("=====Fahrersuche: Durchsucht alle Fahrer nach gesuchem Name=====");
                outputList(importObj.findDriverByNames(argumentData[1]));
                break;
            case "--fahrzeugsuche":
                logger.logInfo("Case 02 of command Mapping selected: --fahrzeugsuche");
                System.out.println("=====Fahrzeugsuche: Findet Fahrzeuge nach suchbegriff=====");
                outputList(importObj.findVehicleBySearchTerm(argumentData[1]));
                break;
            case "--fahrerZeitpunkt":
                logger.logInfo("Case 03 of command Mapping selected: --fahrerZeitpunkt");
                if (argumentData.length != 3) {
                    logger.logWarning("Wrong number of arguments for --fahrerZeitpunkt");
                } else {
                    System.out.println("=====Blitzer: Herausfinden welcher Fahrer ein Fahrzeug zu einem bestimmten Zeitpunkt verwendet hat =====");
                    outputList(importObj.findDriverByVehicleIDandDate(argumentData[1], argumentData[2]));
                }
                break;
            case "--fahrerDatum":
                logger.logInfo("Case 04 of command Mapping selected: --fahrerDatum");
                if (argumentData.length != 3) {
                    logger.logWarning("Wrong number of arguments for --fahrerDatum");
                } else {
                    System.out.println("=====FahrerDatum: Fahrer mit gleichem Fahrzeugen am selben Tag=====");
                    outputList(importObj.getUsersVehicleByDateAndDriverID(argumentData[1], argumentData[2]));
                }
                break;
            default:
                logger.logWarning("default Case of command Mapping selected: Command could not be processed \n Check spelling or try again");
                break;
        }

    }

    /**
     * Prints a list of objects to the console in a formatted way.
     * Uses the {@code toString()} method of the objects in the list for display.
     * Prints a default message if the list is null or empty.
     *
     * @param listedData The list of objects to be printed. Can contain any object type.
     */
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
     * Processes the raw command-line argument array into a structured string array.
     * It separates the command (e.g., "--fahrersuche") from its values (e.g., "Max;Mustermann").
     *
     * @param argument The argument array received from the main method.
     * @return A String array where the first element is the command and subsequent elements are the values.
     *         Returns an empty array for an invalid format or null for missing arguments.
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