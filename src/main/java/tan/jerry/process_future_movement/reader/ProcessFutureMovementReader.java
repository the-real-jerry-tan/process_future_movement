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
package tan.jerry.process_future_movement.reader;

import jakarta.annotation.PostConstruct;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import tan.jerry.process_future_movement.domain.InputRecord;
import tan.jerry.process_future_movement.log.Ayaka;

/**
 * ProcessFutureMovementReader is a custom implementation of FlatFileItemReader for reading
 * fixed-width input records from a file and mapping them to InputRecord objects.
 * It utilizes the Ayaka logging system to provide detailed logs, particularly useful
 * when Spring framework logging configurations override application-level settings.
 */
public class ProcessFutureMovementReader extends FlatFileItemReader<InputRecord> {

    @Value("${input.file.name}")
    private String inputFileName;

    @Value("${ayaka.log.name}")
    private String ayaka_log;

    private Ayaka logger;

    /**
     * Initializes the reader by setting the resource to the specified input file
     * and configuring the line mapper and tokenizer for reading fixed-width records.
     * The Ayaka log is used to capture detailed logging information.
     */
    @PostConstruct
    public void init() {
        setResource(new ClassPathResource(inputFileName));

        setLineMapper(new DefaultLineMapper<InputRecord>() {{
            setLineTokenizer(new FixedLengthTokenizer() {{
                setStrict(false); // Allow lines shorter than specified ranges
                setNames("recordCode", "clientType", "clientNumber", "accountNumber", "subAccountNumber",
                        "oppositePartyCode", "productGroupCode", "exchangeCode", "symbol", "expirationDate",
                        "currencyCode", "movementCode", "buySellCode", "quantityLongSign", "quantityLong",
                        "quantityShortSign", "quantityShort", "exchBrokerFeeDec", "exchBrokerFeeDC", "exchBrokerFeeCurCode",
                        "clearingFeeDec", "clearingFeeDC", "clearingFeeCurCode", "commission", "commissionDC",
                        "commissionCurCode", "transactionDate", "futureReference", "ticketNumber", "externalNumber",
                        "transactionPriceDec", "traderInitials", "oppositeTraderId", "openCloseCode", "filler");
                setColumns(
                        new Range(1, 3),    // recordCode
                        new Range(4, 7),    // clientType
                        new Range(8, 11),   // clientNumber
                        new Range(12, 15),  // accountNumber
                        new Range(16, 19),  // subAccountNumber
                        new Range(20, 25),  // oppositePartyCode
                        new Range(26, 27),  // productGroupCode
                        new Range(28, 31),  // exchangeCode
                        new Range(32, 37),  // symbol
                        new Range(38, 45),  // expirationDate
                        new Range(46, 48),  // currencyCode
                        new Range(49, 50),  // movementCode
                        new Range(51, 51),  // buySellCode
                        new Range(52, 52),  // quantityLongSign
                        new Range(53, 62),  // quantityLong
                        new Range(63, 63),  // quantityShortSign
                        new Range(64, 73),  // quantityShort
                        new Range(74, 85),  // exchBrokerFeeDec
                        new Range(86, 86),  // exchBrokerFeeDC
                        new Range(87, 89),  // exchBrokerFeeCurCode
                        new Range(90, 101), // clearingFeeDec
                        new Range(102, 102), // clearingFeeDC
                        new Range(103, 105), // clearingFeeCurCode
                        new Range(106, 117), // commission
                        new Range(118, 118), // commissionDC
                        new Range(119, 121), // commissionCurCode
                        new Range(122, 129), // transactionDate
                        new Range(130, 135), // futureReference
                        new Range(136, 141), // ticketNumber
                        new Range(142, 147), // externalNumber
                        new Range(148, 162), // transactionPriceDec
                        new Range(163, 168), // traderInitials
                        new Range(169, 175), // oppositeTraderId
                        new Range(176, 176), // openCloseCode
                        new Range(177, 303)  // filler
                );
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<InputRecord>() {{
                setTargetType(InputRecord.class);
            }});
        }});
    }//init

    /**
     * Reads and returns the next record from the input file.
     * Logs detailed information about the record read using Ayaka logger.
     *
     * @return The next InputRecord from the file, or null if no more records are available.
     * @throws Exception if an error occurs during reading.
     */
    @Override
    public InputRecord read() throws Exception {
        logger = new Ayaka("pfm_reader" + ayaka_log);
        logger.log("Invoking read from pfm_reader");
        InputRecord record = super.read();
        if (record != null) {
            logger.log("Read record: " + record.toString(), Ayaka.LogLevel.INFO);
        } else {
            logger.log("End of file reached or no more records to read.", Ayaka.LogLevel.INFO);
        }
        return record;
    }
}//class
