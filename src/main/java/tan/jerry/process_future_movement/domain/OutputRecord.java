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
package tan.jerry.process_future_movement.domain;

/**
 * Represents an output record for the daily summary report in the process future movement system.
 * This record is derived based on specific business requirements outlined for the summary report.
 *
 * <p>The Daily Summary Report is generated in CSV format (Output.csv) and includes the following headers:
 * - Client_Information
 * - Product_Information
 * - Total_Transaction_Amount
 * </p>
 *
 * <p>Details:
 * <ul>
 * <li><b>Client_Information</b>: A combination of the CLIENT TYPE, CLIENT NUMBER, ACCOUNT NUMBER, and SUBACCOUNT NUMBER fields from the input file.</li>
 * <li><b>Product_Information</b>: A combination of the EXCHANGE CODE, PRODUCT GROUP CODE, SYMBOL, and EXPIRATION DATE fields.</li>
 * <li><b>Total_Transaction_Amount</b>: The net total of the (QUANTITY LONG - QUANTITY SHORT) values for each client per product.</li>
 * </ul>
 * </p>
 *
 * Note: The class is currently implemented as a Java record, which provides a compact syntax for immutable data carriers.
 * The @Data annotation is commented out as a placeholder if the class implementation is changed from record to a traditional Java class in the future.
 */
//@Data - placeholder if we decide later to use Java class rather than record
public record OutputRecord (
        String clientInformation,
        String productInformation,
        double totalTransactionAmount
) {
    /**
     * @param clientInformation     The derived field representing client information. It is a combination of:
     *                              - CLIENT TYPE
     *                              - CLIENT NUMBER
     *                              - ACCOUNT NUMBER
     *                              - SUBACCOUNT NUMBER
     *
     *                              This information is extracted from the input file and combined to uniquely identify the client.
     *
     * @param productInformation    The derived field representing product information. It is a combination of:
     *                              - EXCHANGE CODE
     *                              - PRODUCT GROUP CODE
     *                              - SYMBOL
     *                              - EXPIRATION DATE
     *
     *                              This information is extracted from the input file to specify the product associated with the transactions.
     *
     * @param totalTransactionAmount The net total transaction amount for the client-product combination. It is calculated as:
     *                               (QUANTITY LONG - QUANTITY SHORT)
     *
     *                               This value represents the total volume of transactions for the given client and product, reflecting net positions.
     */
}
