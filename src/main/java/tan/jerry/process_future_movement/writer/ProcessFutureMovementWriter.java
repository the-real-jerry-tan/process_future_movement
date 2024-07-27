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
package tan.jerry.process_future_movement.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import tan.jerry.process_future_movement.domain.OutputRecord;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class ProcessFutureMovementWriter implements ItemWriter<OutputRecord> {

    private static final Logger logger = LoggerFactory.getLogger(ProcessFutureMovementWriter.class);
    private BufferedWriter writer;

    public ProcessFutureMovementWriter() {
        // Load properties file
        Properties properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("application.properties"));
            String outputFileName = properties.getProperty("output.file.name"); // TODO => Configure output to /data or /out directory; for testing locally it currently writes to default output location
            if (outputFileName == null) {
                throw new RuntimeException("Output file name not found in properties file");
            }
            writer = new BufferedWriter(new FileWriter(outputFileName));
        } catch (IOException e) {
            logger.trace("Error initializing writer", e);
            throw new RuntimeException("Unable to initialize writer", e);
        }
    }

    @Override
    public void write(Chunk<? extends OutputRecord> chunk) throws Exception {
        System.out.println("SKY TREE BLUE MOON BREEZE => Writing chunk: " + chunk.toString());
        List<? extends OutputRecord> records = chunk.getItems();
        if (records == null || records.isEmpty()) {
            logger.trace("No records to write.");
            return;
        }

        for (OutputRecord record : records) {
            logger.trace("Writing records: " + records.toString());
            try {
                writer.write(record.toString());
                writer.newLine();
            } catch (IOException e) {
                logger.trace("Error writing record: " + record, e);
                throw new RuntimeException("Unable to write record: " + record, e);
            }
        }
        writer.flush();
    }

    public void close() {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                logger.trace("Error closing writer", e);
            }
        }
    }
}
