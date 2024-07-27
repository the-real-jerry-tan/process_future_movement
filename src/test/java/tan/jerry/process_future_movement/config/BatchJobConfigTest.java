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
package tan.jerry.process_future_movement.config;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import tan.jerry.process_future_movement.domain.InputRecord;
import tan.jerry.process_future_movement.domain.OutputRecord;

import static org.junit.jupiter.api.Assertions.*;

@SpringBatchTest
@SpringBootTest
@ActiveProfiles("test")
class BatchJobConfigTest {

    @Autowired
    private BatchJobConfig batchJobConfig;

    @BeforeEach
    void setUp() {
        // Setup code if needed
    }

    @AfterEach
    void tearDown() {
        // Cleanup code if needed
    }

    @Test
    void job() {
        Job job = batchJobConfig.job();
        assertNotNull(job);
        assertEquals("pfm_job", job.getName());
    }

    @Test
    void init_step() {
        Step step = batchJobConfig.init_step();
        assertNotNull(step);
        assertEquals("init_step", step.getName());
    }

    @Test
    void pfm_step() {
        Step step = batchJobConfig.pfm_step();
        assertNotNull(step);
        assertEquals("pfm_step", step.getName());
    }

    @Test
    void pfm_reader() {
        ItemReader<? extends InputRecord> reader = batchJobConfig.pfm_reader();
        assertNotNull(reader);
    }

    @Test
    void pfm_processor() {
        ItemProcessor<? super InputRecord, ? extends OutputRecord> processor = batchJobConfig.pfm_processor();
        assertNotNull(processor);
    }

    @Test
    void pfm_writer() {
        ItemWriter<? super OutputRecord> writer = batchJobConfig.pfm_writer();
        assertNotNull(writer);
    }

    @Test
    void finalize_step() {
        Step step = batchJobConfig.finalize_step();
        assertNotNull(step);
        assertEquals("finalize_step", step.getName());
    }

    @Test
    void init_tasklet() {
        assertNotNull(batchJobConfig.init_tasklet());
    }

    @Test
    void process_future_movement_tasklet() {
        assertNotNull(batchJobConfig.process_future_movement_tasklet());
    }

    @Test
    void finalize_tasklet() {
        assertNotNull(batchJobConfig.finalize_tasklet());
    }
}
