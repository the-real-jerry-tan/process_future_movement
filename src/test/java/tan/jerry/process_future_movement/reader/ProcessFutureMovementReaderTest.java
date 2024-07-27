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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import tan.jerry.process_future_movement.domain.InputRecord;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Disabled // until further notice
class ProcessFutureMovementReaderTest {

    @Autowired
    private ResourceLoader resourceLoader;

    private ProcessFutureMovementReader reader;
    private ExecutionContext executionContext;

    @BeforeEach
    void setUp() {
        reader = new ProcessFutureMovementReader();
        executionContext = new ExecutionContext();
    }

    @Test
    void read_NormalCase() {
        try {
            reader.setResource(resourceLoader.getResource("classpath:test_data/Input.txt"));
            reader.open(executionContext);
            InputRecord record = reader.read();
            assertNotNull(record);
            assertEquals("315", record.recordCode());
            assertEquals("CL", record.clientType());
            assertEquals("1234", record.clientNumber());
        } catch (Exception e) {
            fail("Exception during normal case read: " + e.getMessage());
        }
    }

    @Test
    void read_EndOfFile() {
        try {
            reader.setResource(resourceLoader.getResource("classpath:test_data/Input.txt"));
            reader.open(executionContext);
            InputRecord record;
            do {
                record = reader.read();
            } while (record != null);
            assertNull(record);
        } catch (Exception e) {
            fail("Exception during end of file read: " + e.getMessage());
        }
    }

    @Test
    void read_EmptyFile() {
        try {
            reader.setResource(resourceLoader.getResource("classpath:test_data/EmptyInput.txt"));
            reader.open(executionContext);
            InputRecord record = reader.read();
            assertNull(record);
        } catch (Exception e) {
            fail("Exception during empty file read: " + e.getMessage());
        }
    }

    @Test
    void read_InvalidRecordCode() {
        try {
            reader.setResource(resourceLoader.getResource("classpath:test_data/InvalidRecordCode.txt"));
            reader.open(executionContext);
            InputRecord record = reader.read();
            assertNotNull(record);
            assertEquals("000", record.recordCode());
        } catch (Exception e) {
            fail("Exception during invalid record code read: " + e.getMessage());
        }
    }

    @Test
    void read_InvalidNumberFormat() {
        try {
            reader.setResource(resourceLoader.getResource("classpath:test_data/InvalidNumberFormat.txt"));
            reader.open(executionContext);
            assertThrows(NumberFormatException.class, () -> {
                reader.read();
            });
        } catch (Exception e) {
            fail("Exception during invalid number format read: " + e.getMessage());
        }
    }

    @Test
    @Disabled("This test is for experimental purposes and should not stop the build.")
    void read_DisabledTest() throws Exception {
        try {
            reader.setResource(resourceLoader.getResource("classpath:test_data/Input.txt"));
            reader.open(executionContext);
            InputRecord record = reader.read();
            fail("This test is disabled and should not fail the build");
        } catch (Exception e) {
            fail("Exception during disabled test: " + e.getMessage());
        }
    }

    @Test
    void read_LongValues() {
        try {
            reader.setResource(resourceLoader.getResource("classpath:test_data/LongValues.txt"));
            reader.open(executionContext);
            InputRecord record = reader.read();
            assertNotNull(record);
            assertEquals("9999999999", record.quantityLong());
        } catch (Exception e) {
            fail("Exception during long values read: " + e.getMessage());
        }
    }

    @Test
    void read_ShortValues() {
        try {
            reader.setResource(resourceLoader.getResource("classpath:test_data/ShortValues.txt"));
            reader.open(executionContext);
            InputRecord record = reader.read();
            assertNotNull(record);
            assertEquals("0", record.quantityShort());
        } catch (Exception e) {
            fail("Exception during short values read: " + e.getMessage());
        }
    }

    @Test
    void read_MissingFields() {
        try {
            reader.setResource(resourceLoader.getResource("classpath:test_data/MissingFields.txt"));
            reader.open(executionContext);
            InputRecord record = reader.read();
            assertNotNull(record);
            assertEquals("", record.traderInitials());
        } catch (Exception e) {
            fail("Exception during missing fields read: " + e.getMessage());
        }
    }

    @Test
    void read_InvalidDateFormat() {
        try {
            reader.setResource(resourceLoader.getResource("classpath:test_data/InvalidDateFormat.txt"));
            reader.open(executionContext);
            assertThrows(NumberFormatException.class, () -> {
                reader.read();
            });
        } catch (Exception e) {
            fail("Exception during invalid date format read: " + e.getMessage());
        }
    }

    @Test
    void read_EmptyLines() {
        try {
            reader.setResource(resourceLoader.getResource("classpath:test_data/EmptyLines.txt"));
            reader.open(executionContext);
            InputRecord record = reader.read();
            assertNotNull(record);
        } catch (Exception e) {
            fail("Exception during empty lines read: " + e.getMessage());
        }
    }

    @Test
    void read_AllFieldsMaxLength() {
        try {
            reader.setResource(resourceLoader.getResource("classpath:test_data/AllFieldsMaxLength.txt"));
            reader.open(executionContext);
            InputRecord record = reader.read();
            assertNotNull(record);
            assertEquals(303, record.toString().length());
        } catch (Exception e) {
            fail("Exception during all fields max length read: " + e.getMessage());
        }
    }
}
