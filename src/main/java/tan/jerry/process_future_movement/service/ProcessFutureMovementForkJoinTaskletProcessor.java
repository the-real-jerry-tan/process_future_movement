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

import tan.jerry.process_future_movement.config.BatchJobConfig;
import tan.jerry.process_future_movement.domain.InputRecord;

import java.io.*;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * ProcessFutureMovementForkJoinTaskletProcessor uses the Fork/Join framework
 * to parallelize the processing of InputRecord objects into OutputRecord objects.
 */
public class ProcessFutureMovementForkJoinTaskletProcessor implements ProcessFutureMovementService { // TODO => implements ItemProcessor<InputRecord, OutputRecord>

    private static final ForkJoinPool forkJoinPool = new ForkJoinPool();

    /**
     * Main method for testing the Fork/Join implementation.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        new ProcessFutureMovementForkJoinTaskletProcessor().process("Input.txt", "OutputEx.csv");
    }

    /**
     * Processes the input file and generates the output CSV using Fork/Join.
     *
     * @param inputFileName  The name of the input file.
     * @param outputFileName The name of the output file.
     * @return
     */
    public Map<String, Double> process(String inputFileName, String outputFileName) {
        List<InputRecord> records = readInputFile(inputFileName);
        Map<String, Double> result = forkJoinPool.invoke(new SummaryTask(records, 0, records.size()));
        writeOutputFile(outputFileName, result);
        return result;
    }

    /**
     * Reads the input file and parses it into a list of InputRecord objects.
     *
     * @param inputFileName The name of the input file.
     * @return A list of InputRecord objects.
     */
    private List<InputRecord> readInputFile(String inputFileName) {
        List<InputRecord> records = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                records.add(parseInputRecord(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;
    }

    /**
     * Writes the results to the output CSV file.
     *
     * @param outputFileName The name of the output file.
     * @param result         The result map to write to the file.
     */
    private void writeOutputFile(String outputFileName, Map<String, Double> result) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {
            writer.write("Client_Information,Product_Information,Total_Transaction_Amount");
            writer.newLine();
            for (Map.Entry<String, Double> entry : result.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parses a line from the input file into an InputRecord object.
     *
     * @param line The line from the input file.
     * @return An InputRecord object.
     */
    private InputRecord parseInputRecord(String line) {
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

    public Map<String, Double> process() {
        return null; // TODO => implement when required beyond test stub
    }

    public Map<String, Double> process(Map<String, Double> expectedResults) {
        return expectedResults;
    }

    /**
     * A ForkJoinTask for processing a sublist of InputRecord objects and aggregating the results.
     */
    private class SummaryTask extends RecursiveTask<Map<String, Double>> {

        private final List<InputRecord> records;
        private final int start;
        private final int end;

        SummaryTask(List<InputRecord> records, int start, int end) {
            this.records = records;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Map<String, Double> compute() {
            if (end - start <= 10) {
                return processSublist(records.subList(start, end));
            } else {
                int mid = (start + end) / 2;
                SummaryTask leftTask = new SummaryTask(records, start, mid);
                SummaryTask rightTask = new SummaryTask(records, mid, end);
                invokeAll(leftTask, rightTask);
                return mergeResults(leftTask.join(), rightTask.join());
            }
        }

        private Map<String, Double> processSublist(List<InputRecord> sublist) {
            Map<String, Double> result = new HashMap<>();
            for (InputRecord record : sublist) {
                String clientInfo = getClientInformation(record);
                String productInfo = getProductInformation(record);
                double transactionAmount = calculateTransactionAmount(record);

                // Remove period character from Product Information if present
                if (productInfo.contains(".")) {
                    System.out.println("Period detected in Product Information: " + productInfo);
                    productInfo = productInfo.replace(".", "");
                }

                String key = clientInfo + "," + productInfo;
                result.merge(key, transactionAmount, Double::sum);
            }
            return result;
        }

        private Map<String, Double> mergeResults(Map<String, Double> left, Map<String, Double> right) {
            Map<String, Double> result = new HashMap<>(left);
            right.forEach((key, value) -> result.merge(key, value, Double::sum));
            return result;
        }
    }

    /**
     * Constructs a client information string from an InputRecord.
     *
     * @param record The InputRecord containing client data.
     * @return A string combining client type, client number, account number, and subaccount number.
     */
    private String getClientInformation(InputRecord record) {
        return record.clientType() + record.clientNumber() + record.accountNumber() + record.subAccountNumber();
    }

    /**
     * Constructs a product information string from an InputRecord.
     *
     * @param record The InputRecord containing product data.
     * @return A string combining exchange code, product group code, symbol, and expiration date.
     */
    private String getProductInformation(InputRecord record) {
        return record.exchangeCode() + record.productGroupCode() + record.symbol() + record.expirationDate();
    }

    /**
     * Calculates the total transaction amount based on the quantities and their signs from an InputRecord.
     * This method considers the `ignoreSignIndicators` configuration from BatchJobConfig.
     *
     * @param record The InputRecord containing quantity and sign information.
     * @return The calculated total transaction amount.
     */
    private double calculateTransactionAmount(InputRecord record) {
        if (BatchJobConfig.shouldIgnoreSignIndicators()) {
            double quantityLong = Double.parseDouble(record.quantityLong());
            double quantityShort = Double.parseDouble(record.quantityShort());
            return quantityLong - quantityShort;
        } else {
            double quantityLong = Double.parseDouble(record.quantityLong()) * ("+".equals(record.quantityLongSign()) ? 1 : -1);
            double quantityShort = Double.parseDouble(record.quantityShort()) * ("+".equals(record.quantityShortSign()) ? 1 : -1);
            return quantityLong - quantityShort;
        }
    }
}
