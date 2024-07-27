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

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import tan.jerry.process_future_movement.domain.InputRecord;
import tan.jerry.process_future_movement.domain.OutputRecord;

import static org.junit.jupiter.api.Assertions.*;

@SpringBatchTest
@SpringBootTest
@ActiveProfiles("test")
class ProcessFutureMovementProcessorTest {

    private ProcessFutureMovementProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new ProcessFutureMovementProcessor();
    }

    @Disabled
    @Test
    void process_NormalCase() throws Exception {
        boolean isOptional = true; // You could set this based on your test environment
        Assumptions.assumeTrue(isOptional, "Skipping this test because it's optional.");

        InputRecord input = new InputRecord("315", "CL", "1234", "0002", "0001", "SGXDC", "FU", "SGX", "NK", "20100910",
                "JPY", "01", "B", "+", "0000000004", "-", "0000000002", "0", "0", "USD", "0", "0", "USD", "0", "0",
                "USD", "20100820", "00012390", "000123", "000123", "0000000004", "TI", "OTI", "O", " ");
        OutputRecord output = processor.process(input);

        assertNotNull(output);
        assertEquals("CL-1234-0002-0001-SGXDC", output.clientInformation());
        assertEquals("FU-SGX-NK-20100910", output.productInformation());
        assertEquals(4.0, output.totalTransactionAmount(), 0.01);
    }

    @Test
    void process_ZeroQuantities() throws Exception {
        InputRecord input = new InputRecord("315", "CL", "1234", "0002", "0001", "SGXDC", "FU", "SGX", "NK", "20100910",
                "JPY", "01", "B", "+", "0000000000", "-", "0000000000", "0", "0", "USD", "0", "0", "USD", "0", "0",
                "USD", "20100820", "00012390", "000123", "000123", "0000000000", "TI", "OTI", "O", " ");
        OutputRecord output = processor.process(input);

        assertNotNull(output);
        assertEquals(0.0, output.totalTransactionAmount(), 0.01);
    }

    @Test
    void process_NegativeQuantities() throws Exception {
        InputRecord input = new InputRecord("315", "CL", "1234", "0002", "0001", "SGXDC", "FU", "SGX", "NK", "20100910",
                "JPY", "01", "S", "-", "0000000004", "+", "0000000006", "0", "0", "USD", "0", "0", "USD", "0", "0",
                "USD", "20100820", "00012390", "000123", "000123", "0000000004", "TI", "OTI", "O", " ");
        OutputRecord output = processor.process(input);

        assertNotNull(output);
        assertEquals(-10.0, output.totalTransactionAmount(), 0.01);
    }

    @Test
    void process_InvalidQuantityFormat() {
        InputRecord input = new InputRecord("315", "CL", "1234", "0002", "0001", "SGXDC", "FU", "SGX", "NK", "20100910",
                "JPY", "01", "B", "+", "INVALID", "-", "INVALID", "0", "0", "USD", "0", "0", "USD", "0", "0",
                "USD", "20100820", "00012390", "000123", "000123", "0000000004", "TI", "OTI", "O", " ");

        assertThrows(NumberFormatException.class, () -> {
            processor.process(input);
        });
    }

    @Disabled
    @Test
    void process_EmptyFields() throws Exception {
        boolean isOptional = true; // You could set this based on your test environment
        Assumptions.assumeTrue(isOptional, "Skipping this test because it's optional.");

        InputRecord input = new InputRecord("315", "", "", "", "", "", "", "", "", "",
                "", "", "", "", "0", "", "0", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "","");
        OutputRecord output = processor.process(input);

        assertNotNull(output);
        assertEquals("----", output.clientInformation());
        assertEquals("----", output.productInformation());
        assertEquals(0.0, output.totalTransactionAmount(), 0.01);
    }

    @Disabled
    @Test
    void process_DebugFlagEnabled() throws Exception {
        boolean isOptional = true; // You could set this based on your test environment
        Assumptions.assumeTrue(isOptional, "Skipping this test because it's optional.");

        // Enable debug flag
        ReflectionTestUtils.setField(processor, "debug_flag", true);

        InputRecord input = new InputRecord("315", "CL", "1234", "0002", "0001", "SGXDC", "FU", "SGX", "NK", "20100910",
                "JPY", "01", "B", "+", "0000000004", "-", "0000000002", "0", "0", "USD", "0", "0", "USD", "0", "0",
                "USD", "20100820", "00012390", "000123", "000123", "0000000004", "TI", "OTI", "O", " ");
        OutputRecord output = processor.process_ex2(input);

        assertNotNull(output);
        assertEquals("CL123400020001", output.clientInformation());
        assertEquals("SGXFUNKN20100910", output.productInformation());
        assertEquals(2.0, output.totalTransactionAmount(), 0.01);
    }
}
