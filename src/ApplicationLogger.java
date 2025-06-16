import java.io.IOException;
import java.util.logging.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A basic logger implementation that provides console and file logging capabilities
 * with custom formatting for application logs.
 */
public class ApplicationLogger{
    // Constants for configuration
    private static final String LOG_FILE = "application.log";
    //private static final DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Logger instances for different log levels
    private final Logger debugLogger;

    //Initializes a new logger with console and file handlers
    public ApplicationLogger() {
        this.debugLogger = Logger.getLogger("DebugLogger");
        setupLogger();
    }

    public void setupLogger(){
        try {
            // Set log level
            debugLogger.setLevel(Level.FINE);

            // Create ConsoleHandler
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.INFO);
            consoleHandler.setFormatter(new SimpleFormatter());

            // Create FileHandler
            FileHandler fileHandler = new FileHandler(LOG_FILE, true);
            fileHandler.setLevel(Level.FINE);
            fileHandler.setFormatter(new SimpleFormatter());

            // Remove default handlers and add the custom handlers
            debugLogger.setUseParentHandlers(false);
            debugLogger.addHandler(fileHandler);
            debugLogger.addHandler(consoleHandler);

            // Log message
            debugLogger.log(Level.INFO, "Initializing logger");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public String format(LogRecord record) {
//        LocalDateTime date = LocalDateTime.now();
//        return String.format("[%s] [%s] [%s] %s%n",
//                dtFormatter.format(date),
//                record.getLoggerName(),
//                record.getLevel(),
//                record.getMessage());
//    }

    // Public logging methods for logging in other classes
    public void logDebug(String message) {
        debugLogger.fine(message);
    }

    public void logInfo(String message) {
        debugLogger.info(message);
    }

    public void logWarning(String message) {
        debugLogger.warning(message);
    }

    public void logSevere(String message) {
        debugLogger.severe(message);
    }

    //Error logging method with aditional catching of thrown exceptions
    public void logError(String message, Throwable thrown) {
        debugLogger.log(Level.SEVERE, message, thrown);
    }
}