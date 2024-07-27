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
package tan.jerry.process_future_movement.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tan.jerry.process_future_movement.config.BatchJobConfig;
import tan.jerry.process_future_movement.domain.InputRecord;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * ProcessFutureMovementTaskletProcessor is a service class responsible for processing
 * the input data and generating a daily summary report. The class reads input records,
 * calculates transaction amounts, and produces an output CSV file.
 */
@Service
public class ProcessFutureMovementTaskletProcessor implements ProcessFutureMovementService { // TODO => implements ItemProcessor<InputRecord, OutputRecord>

    @Value("input.file.name")
    String inputFileName;

    @Value("outputex.file.name")
    String outputFilePath;

    static Map<String, Double> report_map;

    /**
     * Main method for executing the tasklet processor.
     * Initiates the process of reading input data and generating the summary report.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        new ProcessFutureMovementTaskletProcessor().process();
    }

    /**
     * Processes the input data by reading records, calculating transaction amounts,
     * and writing the results to an output CSV file. This method also handles
     * checking and replacing periods in product information.
     *
     * @return A map representing the client-product keys and their corresponding total transaction amounts.
     */
    public Map<String, Double> process() {

        Map<String, Double> transactionMap = new HashMap<>();

        inputFileName = null == inputFileName ? "Input.txt" : inputFileName;

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(inputFileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                InputRecord record = parseInputRecord(line);
                String clientInfo = getClientInformation(record);
                String productInfo = getProductInformation(record);
                double transactionAmount = calculateTransactionAmount(record);

                // Remove period character from Product Information if present
                if (productInfo.contains(".")) {
                    System.out.println("Period detected in Product Information: " + productInfo);
                    productInfo = productInfo.replace(".", "");
                }

                String key = clientInfo + "," + productInfo;
                transactionMap.merge(key, transactionAmount, Double::sum);
            }

            writeOutputFile(outputFilePath, transactionMap);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            report_map = transactionMap;
            return report_map;
        }
    }

    /**
     * Parses a line of text into an InputRecord object.
     * Each field in the InputRecord is mapped from a specific position in the line.
     *
     * @param line The line of text representing a record.
     * @return The InputRecord object populated with data from the line.
     */
    private static InputRecord parseInputRecord(String line) {
        return new InputRecord(
                line.substring(0, 3).trim(),  // Record Code
                line.substring(3, 7).trim(),  // Client Type
                line.substring(7, 11).trim(), // Client Number
                line.substring(11, 15).trim(), // Account Number
                line.substring(15, 19).trim(), // Subaccount Number
                line.substring(19, 25).trim(), // Opposite Party Code
                line.substring(25, 27).trim(), // Product Group Code
                line.substring(27, 31).trim(), // Exchange Code
                line.substring(31, 37).trim(), // Symbol
                line.substring(37, 45).trim(), // Expiration Date
                line.substring(45, 48).trim(), // Currency Code
                line.substring(48, 50).trim(), // Movement Code
                line.substring(50, 51).trim(), // Buy/Sell Code
                line.substring(51, 52).trim(), // Quantity Long Sign
                line.substring(52, 62).trim(), // Quantity Long
                line.substring(62, 63).trim(), // Quantity Short Sign
                line.substring(63, 73).trim(), // Quantity Short
                line.substring(73, 86).trim(), // Exchange Broker Fee Decimal
                line.substring(86, 87).trim(), // Exchange Broker Fee DC
                line.substring(87, 90).trim(), // Exchange Broker Fee Currency Code
                line.substring(90, 101).trim(), // Clearing Fee Decimal
                line.substring(101, 102).trim(), // Clearing Fee DC
                line.substring(102, 105).trim(), // Clearing Fee Currency Code
                line.substring(105, 118).trim(), // Commission
                line.substring(118, 119).trim(), // Commission DC
                line.substring(119, 122).trim(), // Commission Currency Code
                line.substring(122, 130).trim(), // Transaction Date
                line.substring(130, 136).trim(), // Future Reference
                line.substring(136, 141).trim(), // Ticket Number
                line.substring(141, 147).trim(), // External Number
                line.substring(147, 162).trim(), // Transaction Price Decimal
                line.substring(162, 168).trim(), // Trader Initials
                line.substring(168, 175).trim(), // Opposite Trader Id
                line.substring(175, 176).trim(), // Open/Close Code
                line.length() >= 303 ? line.substring(176, 303).trim() : line.substring(176).trim() // Filler
        );
    }

    /**
     * Constructs a client information string from an InputRecord.
     *
     * @param record The InputRecord containing client data.
     * @return A string combining client type, client number, account number, and subaccount number.
     */
    private static String getClientInformation(InputRecord record) {
        return record.clientType() + record.clientNumber() + record.accountNumber() + record.subAccountNumber();
    }

    /**
     * Constructs a product information string from an InputRecord.
     *
     * @param record The InputRecord containing product data.
     * @return A string combining exchange code, product group code, symbol, and expiration date.
     */
    private static String getProductInformation(InputRecord record) {
        return record.exchangeCode() + record.productGroupCode() + record.symbol() + record.expirationDate();
    }

    /**
     * Calculates the total transaction amount based on the quantities and their signs from an InputRecord.
     * This method considers the `ignoreSignIndicators` configuration from BatchJobConfig.
     *
     * @param record The InputRecord containing quantity and sign information.
     * @return The calculated total transaction amount.
     */
    private static double calculateTransactionAmount(InputRecord record) { // this is the most important part
        if (BatchJobConfig.shouldIgnoreSignIndicators()) {
            double quantityLong = Double.parseDouble(record.quantityLong());
            double quantityShort = Double.parseDouble(record.quantityShort());
            return quantityLong - quantityShort;
        } else {
            double quantityLong = Double.parseDouble(record.quantityLong()) * ("+".equals(record.quantityLongSign()) ? 1 : -1);
            double quantityShort = Double.parseDouble(record.quantityShort()) * ("+".equals(record.quantityShortSign()) ? 1 : -1);
            return quantityLong - quantityShort;
        }//if else
    }//this is the most important part

    /**
     * Writes the output CSV file containing the client-product information and their total transaction amounts.
     *
     * @param outputFilePath The path to the output CSV file.
     * @param transactionMap A map containing client-product keys and their corresponding total transaction amounts.
     */
    private static void writeOutputFile(String outputFilePath, Map<String, Double> transactionMap) {

        outputFilePath = null == outputFilePath ? "OutputEx.csv" : outputFilePath;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            writer.write("Client_Information,Product_Information,Total_Transaction_Amount");
            writer.newLine();
            for (Map.Entry<String, Double> entry : transactionMap.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Calculates the daily summary report by executing the main process and returning the report map.
     *
     * @return A map representing the client-product keys and their corresponding total transaction amounts.
     */
    public static Map<String, Double> calculateDailySummary() {
        ProcessFutureMovementTaskletProcessor.main(null);
        return report_map;
    }
}
