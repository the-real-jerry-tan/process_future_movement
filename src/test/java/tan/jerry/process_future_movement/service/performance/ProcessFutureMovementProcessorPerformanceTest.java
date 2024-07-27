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
package tan.jerry.process_future_movement.service.performance;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;
import java.util.HashMap;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import tan.jerry.process_future_movement.service.ProcessFutureMovementProcessor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains performance tests for the ProcessFutureMovementProcessor.
 * It measures the processing time for different sizes of input data (original, 10x, 100x, 10Kx).
 * The purpose of these tests is to gauge the performance and scalability of the processor
 * under varying workloads.
 */
@SpringBatchTest
@SpringBootTest
@ActiveProfiles("test")
class ProcessFutureMovementProcessorPerformanceTest {

    private static final Logger logger = LoggerFactory.getLogger(ProcessFutureMovementProcessorPerformanceTest.class);
    private ProcessFutureMovementProcessor processor = new ProcessFutureMovementProcessor();
    private MeterRegistry meterRegistry = new SimpleMeterRegistry();

    @Value("${enable.performance.tests:false}")
    private boolean enablePerformanceTests;

    @Test
    //@Disabled("Disabled commented until we add an explicit configuration key for 'enable.performance.tests' and execution is predicated on its value")
    void performanceTestOriginalSize() {
        if (!enablePerformanceTests) {
            logger.info("Performance tests are disabled.");
            return;
        }

        Timer timer = meterRegistry.timer("process.future.movement.performance.metrics", "class", processor.getClass().getSimpleName(), "size", "original");
        long startTime = System.currentTimeMillis();
        logger.info("Start processing: {}", processor.getClass().getSimpleName());
        Map<String, Double> result = timer.record(() -> processor.process(loadData("Input.txt")));
        long endTime = System.currentTimeMillis();
        logger.info("End processing: {}", processor.getClass().getSimpleName());

        assertNotNull(result);
        long duration = endTime - startTime;
        logger.info("Performance Test - Original Size: {} ms", duration);
        meterRegistry.gauge("process.future.movement.performance.metrics.duration", duration);
    }

    @Test
    void performanceTest10xSize() {
        if (!enablePerformanceTests) {
            logger.info("Performance tests are disabled.");
            return;
        }

        Timer timer = meterRegistry.timer("process.future.movement.performance.metrics", "class", processor.getClass().getSimpleName(), "size", "10x");
        long startTime = System.currentTimeMillis();
        logger.info("Start processing: {}", processor.getClass().getSimpleName());
        Map<String, Double> result = timer.record(() -> processor.process(loadData("Input_10x.txt")));
        long endTime = System.currentTimeMillis();
        logger.info("End processing: {}", processor.getClass().getSimpleName());

        assertNotNull(result);
        long duration = endTime - startTime;
        logger.info("Performance Test - 10x Size: {} ms", duration);
        meterRegistry.gauge("process.future.movement.performance.metrics.duration", duration);
    }

    @Test
    void performanceTest100xSize() {
        if (!enablePerformanceTests) {
            logger.info("Performance tests are disabled.");
            return;
        }

        Timer timer = meterRegistry.timer("process.future.movement.performance.metrics", "class", processor.getClass().getSimpleName(), "size", "100x");
        long startTime = System.currentTimeMillis();
        logger.info("Start processing: {}", processor.getClass().getSimpleName());
        Map<String, Double> result = timer.record(() -> processor.process(loadData("Input_100x.txt")));
        long endTime = System.currentTimeMillis();
        logger.info("End processing: {}", processor.getClass().getSimpleName());

        assertNotNull(result);
        long duration = endTime - startTime;
        logger.info("Performance Test - 100x Size: {} ms", duration);
        meterRegistry.gauge("process.future.movement.performance.metrics.duration", duration);
    }

    @Test
    void performanceTest10KSize() {
        if (!enablePerformanceTests) {
            logger.info("Performance tests are disabled.");
            return;
        }

        Timer timer = meterRegistry.timer("process.future.movement.performance.metrics", "class", processor.getClass().getSimpleName(), "size", "10K");
        long startTime = System.currentTimeMillis();
        logger.info("Start processing: {}", processor.getClass().getSimpleName());
        Map<String, Double> result = timer.record(() -> processor.process(loadData("Input_10K.txt")));
        long endTime = System.currentTimeMillis();
        logger.info("End processing: {}", processor.getClass().getSimpleName());

        assertNotNull(result);
        long duration = endTime - startTime;
        logger.info("Performance Test - 10K Size: {} ms", duration);
        meterRegistry.gauge("process.future.movement.performance.metrics.duration", duration);
    }

    /**
     * This method performs the 10K size performance test multiple times based on the configuration.
     * It checks the `enable.performance.loop` property to decide whether to execute the loop.
     * If enabled, it reads the `performance.loop.size` property to determine the number of iterations.
     * Each iteration runs the `performanceTest10KSize()` method and logs the results.
     * The method also reports metrics to the Spring Actuator for Prometheus using Micrometer.
     */
    @Test
    void performanceTest10KSizeLoop() {
        if (!enablePerformanceTests) {
            logger.info("Performance tests are disabled.");
            return;
        }

        // Load loop configuration
        boolean enablePerformanceLoop = Boolean.parseBoolean(System.getProperty("enable.performance.loop", "false"));
        int loopSize = Integer.parseInt(System.getProperty("performance.loop.size", "1"));

        if (!enablePerformanceLoop) {
            logger.info("Performance loop is disabled.");
            return;
        }

        for (int i = 0; i < loopSize; i++) {
            logger.info("Starting loop iteration {} of {}", i + 1, loopSize);

            Timer timer = meterRegistry.timer("process.future.movement.performance.metrics", "class", processor.getClass().getSimpleName(), "size", "10K", "iteration", String.valueOf(i + 1));
            long startTime = System.currentTimeMillis();
            logger.info("Start processing 10K size iteration: {}", i + 1);
            Map<String, Double> result = timer.record(() -> processor.process(loadData("Input_10K.txt")));
            long endTime = System.currentTimeMillis();
            logger.info("End processing 10K size iteration: {}", i + 1);

            assertNotNull(result);
            long duration = endTime - startTime;
            logger.info("Performance Test - 10K Size Iteration {}: {} ms", i + 1, duration);
            meterRegistry.gauge("process.future.movement.performance.metrics.duration", duration);
        }
    }

    /**
     * Performance test that runs the 10K test a specified number of times in a loop, using recursion.
     * The number of iterations is determined by the configuration property 'performance.recursive.loop.counter'.
     * This method is controlled by the configuration property 'enable.performance.recursive.loop'.
     * If recursion is enabled, it calls itself recursively for the specified number of iterations.
     * Metrics for each iteration are recorded using Micrometer and logged to the console.
     */
    @Test
    void performanceRecursiveLoopTest() {
        if (!enablePerformanceTests || !enablePerformanceRecursiveLoop) {
            logger.info("Recursive performance tests are disabled.");
            return;
        }

        int loopCounter = performanceRecursiveLoopCounter;
        logger.info("Starting recursive performance tests with loop counter: {}", loopCounter);
        performanceRecursiveLoopTestHelper(loopCounter);
    }

    /**
     * Helper method for the recursive performance test. This method handles the actual recursion.
     *
     * @param loopCounter The current value of the loop counter. The method stops recursion when the counter reaches zero.
     */
    private void performanceRecursiveLoopTestHelper(int loopCounter) {
        if (loopCounter <= 0) {
            logger.info("Reached end of recursion.");
            return;
        }

        logger.info("Recursive performance test iteration: {}", performanceRecursiveLoopCounter - loopCounter + 1);
        performanceTest10KSize();  // Calling the 10K test method

        Timer timer = meterRegistry.timer("process.future.movement.performance.metrics", "class", processor.getClass().getSimpleName(), "iteration", String.valueOf(loopCounter));
        long startTime = System.currentTimeMillis();
        logger.info("Start processing (recursive): {}", processor.getClass().getSimpleName());
        Map<String, Double> result = timer.record(() -> processor.process(loadData("Input_10K.txt")));
        long endTime = System.currentTimeMillis();
        logger.info("End processing (recursive): {}", processor.getClass().getSimpleName());

        assertNotNull(result);
        long duration = endTime - startTime;
        logger.info("Recursive Performance Test - Iteration {}: {} ms", loopCounter, duration);
        meterRegistry.gauge("process.future.movement.performance.metrics.duration.recursive", duration);

        // Recur with the decreased counter
        performanceRecursiveLoopTestHelper(loopCounter - 1);
    }

    // Define these fields in the class
    @Value("${enable.performance.recursive.loop:true}")
    private boolean enablePerformanceRecursiveLoop;

    @Value("${performance.recursive.loop.counter:10}")
    private int performanceRecursiveLoopCounter;

    private Map<String, Double> loadData(String filename) {
        // Mocked data loading mechanism. Replace with actual file reading logic.
        Map<String, Double> data = new HashMap<>();
        data.put("CL432100030001,CMEFUN120100910", -79.0);
        data.put("CL123400030001,CMEFUN120100910", 285.0);
        data.put("CL123400030001,CMEFUNK20100910", -215.0);
        data.put("CL123400020001,SGXFUNK20100910", -52.0);
        data.put("CL432100020001,SGXFUNK20100910", 46.0);
        return data;
    }
}
