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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import tan.jerry.process_future_movement.domain.InputRecord;
import tan.jerry.process_future_movement.domain.OutputRecord;

/**
 * ProcessFutureMovementProcessor processes input records to produce output records.
 * It implements the ItemProcessor interface, enabling transformation of InputRecord
 * objects into OutputRecord objects during the Spring Batch processing flow.
 */
public class ProcessFutureMovementProcessor implements ItemProcessor<InputRecord, OutputRecord>, ProcessFutureMovementService  {

    private static final Logger logger = LoggerFactory.getLogger(ProcessFutureMovementProcessor.class);

    @Value("${debug.flag:false}")
    private boolean debug_flag;

    /**
     * Processes an InputRecord to produce an OutputRecord.
     * The processing involves calculating the total transaction amount based on the quantity and sign,
     * and constructing client and product information strings as per business requirements.
     *
     * @param item The InputRecord to process.
     * @return The resulting OutputRecord containing processed data.
     * @throws Exception if an error occurs during processing.
     */
    @Override
    public OutputRecord process(InputRecord item) throws Exception {
        logger.info("Processing item: {}", item);

        // Calculate total transaction amount based on quantity and sign
        double quantityLong = Double.parseDouble(item.quantityLong()) * ("+".equals(item.quantityLongSign()) ? 1 : -1);
        double quantityShort = Double.parseDouble(item.quantityShort()) * ("+".equals(item.quantityShortSign()) ? 1 : -1);
        double totalTransactionAmount = quantityLong - quantityShort;

        // Construct client and product information strings
        String clientInformation = String.format("%s-%s-%s-%s-%s",
                item.clientType(),
                item.clientNumber(),
                item.accountNumber(),
                item.subAccountNumber(),
                item.oppositePartyCode());

        String productInformation = String.format("%s-%s-%s-%s",
                item.productGroupCode(),
                item.exchangeCode(),
                item.symbol(),
                item.expirationDate());

        // Create and return the OutputRecord
        return new OutputRecord(clientInformation, productInformation, totalTransactionAmount);
    }//process

    /**
     * A stub method for processing input records in a different way, potentially for debugging.
     * If the debug flag is set, this method copies pertinent records from InputRecord to OutputRecord.
     * This method is a placeholder and should be implemented as needed.
     *
     * @param item The InputRecord to process.
     * @return The resulting OutputRecord containing copied data.
     * @throws Exception if an error occurs during processing.
     */
    //@Override
    public OutputRecord process_ex2(InputRecord item) throws Exception {
        logger.trace("Processing item: {}", item);
        if (debug_flag) {
            logger.debug("Debug flag is set. Copying input record to output record.");
            // Directly copy pertinent records from InputRecord to OutputRecord if debug flag is set
            return new OutputRecord(
                    item.clientType() +
                            item.clientNumber() +
                            item.accountNumber() +
                            item.subAccountNumber(),
                    item.exchangeCode() +
                            item.productGroupCode() +
                            item.symbol() +
                            item.expirationDate(),
                    Double.valueOf(item.quantityLong()) -
                            Double.valueOf(item.quantityShort())
            );
        }
        // Further processing can be implemented here if debug_flag is not set
        // For now, returning null if not in debug mode to signify no further processing
        return new OutputRecord(
                item.clientType() +
                        item.clientNumber() +
                        item.accountNumber() +
                        item.subAccountNumber(),
                item.exchangeCode() +
                        item.productGroupCode() +
                        item.symbol() +
                        item.expirationDate(),
                Double.valueOf(item.quantityLong()) -
                        Double.valueOf(item.quantityShort())
        );
    }//process

    /**
     * Another stub method for processing input records, designed as a placeholder for future logic.
     * This method currently does not perform any processing and returns null.
     *
     * @param item The InputRecord to process.
     * @return Null, as the method is not implemented.
     * @throws Exception if an error occurs during processing.
     */
    //@Override
    public OutputRecord process_ex(InputRecord item) throws Exception {
        logger.trace("processing item: " + item.toString());

        // TODO => implement processing logic
        //return new OutputRecord(); // Return processed OutputRecord instance
        return null;
    }//process_ex

}//class
