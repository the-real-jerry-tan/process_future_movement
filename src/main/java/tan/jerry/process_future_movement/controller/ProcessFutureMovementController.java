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
package tan.jerry.process_future_movement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tan.jerry.process_future_movement.service.ProcessFutureMovementTaskletProcessor;
import com.opencsv.CSVWriter;

import java.io.StringWriter;
import java.util.Map;

/**
 * REST controller for handling requests related to processing future movements.
 * Provides endpoints for accessing the daily summary report.
 */
@RestController
@RequestMapping("/api")
public class ProcessFutureMovementController {

    private final ProcessFutureMovementTaskletProcessor processor;

    /**
     * Constructs a new ProcessFutureMovementController.
     *
     * @param processor The ProcessFutureMovementTaskletProcessor used to process future movements.
     */
    @Autowired
    public ProcessFutureMovementController(ProcessFutureMovementTaskletProcessor processor) {
        this.processor = processor;
    }

    /**
     * Handles GET requests to the /api/get_daily_summary_report endpoint.
     * This endpoint returns a daily summary report as a map of client-product combinations and their total transaction amounts.
     * It can return the data either in JSON format or as a downloadable CSV file, depending on the client's request.
     *
     * Clients can specify the desired format through the "Accept" header in the HTTP request:
     * - To receive JSON: "Accept: application/json"
     * - To download as CSV: "Accept: text/csv"
     *
     * @param acceptHeader The Accept header from the HTTP request, specifying the desired response format.
     * @return A ResponseEntity containing the report data in the requested format.
     */
    @GetMapping("/get_daily_summary_report")
    public ResponseEntity<?> getDailySummaryReport(@RequestHeader(value = "Accept", defaultValue = "application/json") String acceptHeader) {
        // Process the data using the processor
        ProcessFutureMovementTaskletProcessor.main(null);
        Map<String, Double> summaryReport = processor.calculateDailySummary();

        // Check the Accept header to determine the response format
        if (acceptHeader.contains("text/csv")) {
            // Convert the summary report to CSV format
            StringWriter csvOutput = new StringWriter();
            try (CSVWriter csvWriter = new CSVWriter(csvOutput)) {
                // Write headers
                csvWriter.writeNext(new String[]{"Client-Product", "Total Transaction Amount"});

                // Write data rows
                summaryReport.forEach((clientProduct, amount) ->
                        csvWriter.writeNext(new String[]{clientProduct, String.valueOf(amount)})
                );
            } catch (Exception e) {
                // Return error response if CSV generation fails
                return new ResponseEntity<>("Error generating CSV", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            // Set headers for CSV file download
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, "text/csv");
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=daily_summary_report.csv");
            return new ResponseEntity<>(csvOutput.toString(), headers, HttpStatus.OK);
        } else {
            // Default response is JSON
            return ResponseEntity.ok(summaryReport);
        }
    }
}
