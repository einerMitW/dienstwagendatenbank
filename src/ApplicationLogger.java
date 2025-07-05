import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.*;
import java.util.logging.Formatter;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A basic logger implementation that provides console and file logging capabilities
 * with custom formatting for application logs.
 */
public class ApplicationLogger{
    private static ApplicationLogger instance;
    private final Logger debugLogger;

    private ApplicationLogger() {
        this.debugLogger = Logger.getLogger("DebugLogger");
        setupLogger();
    }

    public static ApplicationLogger getInstance() {
        if (instance == null) {
            instance = new ApplicationLogger();
        }
        return instance;
    }

    public void setupLogger(){
        try {

            // Create ConsoleHandler
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.ALL);
            consoleHandler.setFormatter(new SimpleFormatter());

            // Remove default handlers and add the custom handlers
            debugLogger.setUseParentHandlers(false);
            debugLogger.addHandler(consoleHandler);

            // Log message
            debugLogger.log(Level.INFO, "Initializing logger");
        } catch (Exception e) {
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