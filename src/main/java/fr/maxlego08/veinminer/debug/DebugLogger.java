package fr.maxlego08.veinminer.debug;

import fr.maxlego08.veinminer.VeinMinerPlugin;
import fr.maxlego08.veinminer.api.Config;

import java.util.logging.Logger;

/**
 * Utility class for debug logging and performance measurement.
 * Only logs when debug mode is enabled in the configuration.
 */
public final class DebugLogger {

    private static VeinMinerPlugin plugin;
    private static Logger logger;

    private DebugLogger() {
        // Utility class
    }

    /**
     * Initializes the debug logger with the plugin instance.
     *
     * @param pluginInstance the plugin instance
     */
    public static void init(VeinMinerPlugin pluginInstance) {
        plugin = pluginInstance;
        logger = pluginInstance.getLogger();
    }

    /**
     * Logs a debug message if debug mode is enabled.
     *
     * @param message the message to log
     * @param args    the arguments to format into the message
     */
    public static void debug(String message, Object... args) {
        if (!Config.enableDebug || logger == null) return;

        String formattedMessage = formatMessage(message, args);
        logger.info("[DEBUG] " + formattedMessage);
    }

    /**
     * Logs a warning message if debug mode is enabled.
     *
     * @param message the message to log
     * @param args    the arguments to format into the message
     */
    public static void warn(String message, Object... args) {
        if (!Config.enableDebug || logger == null) return;

        String formattedMessage = formatMessage(message, args);
        logger.warning("[DEBUG] " + formattedMessage);
    }

    /**
     * Logs an error message if debug mode is enabled.
     *
     * @param message the message to log
     * @param args    the arguments to format into the message
     */
    public static void error(String message, Object... args) {
        if (!Config.enableDebug || logger == null) return;

        String formattedMessage = formatMessage(message, args);
        logger.severe("[DEBUG] " + formattedMessage);
    }

    /**
     * Executes an operation and logs the execution time if debug time mode is enabled.
     *
     * @param operation the name of the operation
     * @param action    the action to execute
     */
    public static void debugTime(String operation, Runnable action) {
        if (!Config.enableDebugTime || logger == null) {
            action.run();
            return;
        }

        long startTime = System.nanoTime();
        action.run();
        long endTime = System.nanoTime();

        double durationMs = (endTime - startTime) / 1_000_000.0;
        logger.info(String.format("[DEBUG-TIME] %s took %.3f ms", operation, durationMs));
    }

    /**
     * Executes an operation and logs the execution time if debug time mode is enabled.
     * Returns the result of the operation.
     *
     * @param operation the name of the operation
     * @param action    the action to execute
     * @param <T>       the return type
     * @return the result of the action
     */
    public static <T> T debugTime(String operation, java.util.function.Supplier<T> action) {
        if (!Config.enableDebugTime || logger == null) {
            return action.get();
        }

        long startTime = System.nanoTime();
        T result = action.get();
        long endTime = System.nanoTime();

        double durationMs = (endTime - startTime) / 1_000_000.0;
        logger.info(String.format("[DEBUG-TIME] %s took %.3f ms", operation, durationMs));

        return result;
    }

    /**
     * Starts a timer and returns a Stopwatch for measuring elapsed time.
     *
     * @param operation the name of the operation
     * @return a Stopwatch instance
     */
    public static Stopwatch startTimer(String operation) {
        return new Stopwatch(operation);
    }

    /**
     * Formats a message with the provided arguments.
     *
     * @param message the message template
     * @param args    the arguments
     * @return the formatted message
     */
    private static String formatMessage(String message, Object... args) {
        if (args == null || args.length == 0) {
            return message;
        }

        StringBuilder sb = new StringBuilder(message);
        for (Object arg : args) {
            int index = sb.indexOf("{}");
            if (index != -1) {
                sb.replace(index, index + 2, String.valueOf(arg));
            }
        }
        return sb.toString();
    }

    /**
     * A simple stopwatch for measuring elapsed time.
     */
    public static class Stopwatch {
        private final String operation;
        private final long startTime;

        private Stopwatch(String operation) {
            this.operation = operation;
            this.startTime = System.nanoTime();
        }

        /**
         * Stops the timer and logs the elapsed time.
         */
        public void stop() {
            if (!Config.enableDebugTime || logger == null) return;

            long endTime = System.nanoTime();
            double durationMs = (endTime - startTime) / 1_000_000.0;
            logger.info(String.format("[DEBUG-TIME] %s took %.3f ms", operation, durationMs));
        }

        /**
         * Gets the elapsed time in milliseconds without stopping.
         *
         * @return the elapsed time in milliseconds
         */
        public double elapsedMs() {
            return (System.nanoTime() - startTime) / 1_000_000.0;
        }

        /**
         * Logs an intermediate time without stopping the timer.
         *
         * @param checkpoint the name of the checkpoint
         */
        public void checkpoint(String checkpoint) {
            if (!Config.enableDebugTime || logger == null) return;

            double durationMs = elapsedMs();
            logger.info(String.format("[DEBUG-TIME] %s - %s: %.3f ms", operation, checkpoint, durationMs));
        }
    }
}
