/**
 * © 2024 Jerry Tan. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Jerry Tan
 * ("Confidential Information"). You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms under which this software
 * was distributed or otherwise published, and solely for the prior express purposes
 * explicitly communicated and agreed upon, and only for those specific permissible purposes.
 *
 * This software is provided "AS IS," without a warranty of any kind. All express or implied
 * conditions, representations, and warranties, including any implied warranty of merchantability,
 * fitness for a particular purpose, or non-infringement, are disclaimed, except to the extent
 * that such disclaimers are held to be legally invalid.
 */
package tan.jerry.process_future_movement.log;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Ayaka is a custom logger designed to handle logging independently of
 * the standard logging frameworks like Logback or Log4j, especially in
 * cases where Spring framework configurations override application-level
 * logging settings. This logger writes messages to a specified file, allowing
 * for separate filtering and more controlled logging output.
 */
public class Ayaka {

    private String filename = "ayaka.log";
    private BufferedWriter writer;

    /**
     * Enum representing the various log levels supported by Ayaka.
     */
    public enum LogLevel {
        TRACE, INFO, DEBUG, WARN, FATAL
    }

    /**
     * Constructs an Ayaka logger with the specified log file name.
     * Initializes a BufferedWriter for the specified file, appending to the file if it exists.
     *
     * @param filename The name of the file where logs will be written.
     */
    public Ayaka(String filename) {
        this.filename = filename;
        try {
            this.writer = new BufferedWriter(new FileWriter(filename, true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Logs a message with the default log level TRACE.
     *
     * @param message The message to log.
     */
    public void log(String message) {
        log(message, LogLevel.TRACE);
    }

    /**
     * Logs a message with the specified log level.
     *
     * @param message The message to log.
     * @param level   The log level to use for the message.
     */
    public void log(String message, LogLevel level) {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            writer.write(String.format("%s [%s] - %s", timestamp, level.name(), message));
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the BufferedWriter when logging is complete.
     * Ensures that all buffered messages are written to the file.
     */
    public void close() {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Main method for testing the Ayaka logger.
     * Demonstrates logging messages at various levels and closes the logger.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        Ayaka logger = new Ayaka("ayaka-test.log");
        logger.log("This is a TRACE log message."); // Defaults to TRACE level
        logger.log("This is an INFO message.", LogLevel.INFO);
        logger.log("This is a DEBUG message.", LogLevel.DEBUG);
        logger.log("This is a WARN message.", LogLevel.WARN);
        logger.log("This is a FATAL error message.", LogLevel.FATAL);
        logger.close();
    }
}
