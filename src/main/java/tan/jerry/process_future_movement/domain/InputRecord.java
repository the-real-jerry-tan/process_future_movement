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
 * Represents an input record for processing future movements.
 * This record encapsulates various details about a financial transaction,
 * including client information, product details, and transaction specifics.
 *
 * Note: The class is currently implemented as a Java record, providing
 * a compact syntax for immutable data carriers. The @Data annotation
 * is commented out as a placeholder if the class implementation is
 * changed from record to a traditional Java class in the future.
 */
//@Data - placeholder if we decide later to use Java class rather than record
public record InputRecord(
        String recordCode,
        String clientType,
        String clientNumber,
        String accountNumber,
        String subAccountNumber,
        String oppositePartyCode,
        String productGroupCode,
        String exchangeCode,
        String symbol,
        String expirationDate,
        String currencyCode,
        String movementCode,
        String buySellCode,
        String quantityLongSign,
        String quantityLong,
        String quantityShortSign,
        String quantityShort,
        String exchBrokerFeeDec,
        String exchBrokerFeeDC,
        String exchBrokerFeeCurCode,
        String clearingFeeDec,
        String clearingFeeDC,
        String clearingFeeCurCode,
        String commission,
        String commissionDC,
        String commissionCurCode,
        String transactionDate,
        String futureReference,
        String ticketNumber,
        String externalNumber,
        String transactionPriceDec,
        String traderInitials,
        String oppositeTraderId,
        String openCloseCode,
        String filler
) {
    /**
     * @param recordCode          The record code indicating the type of record.
     * @param clientType          The type of client, such as individual or institutional.
     * @param clientNumber        The client's unique identifier number.
     * @param accountNumber       The client's account number.
     * @param subAccountNumber    The sub-account number under the main account.
     * @param oppositePartyCode   The code identifying the opposite party in the transaction.
     * @param productGroupCode    The code representing the product group.
     * @param exchangeCode        The code for the exchange where the transaction occurred.
     * @param symbol              The symbol representing the traded instrument.
     * @param expirationDate      The expiration date of the instrument.
     * @param currencyCode        The currency code used in the transaction.
     * @param movementCode        The movement code indicating the type of transaction.
     * @param buySellCode         The code indicating a buy or sell action.
     * @param quantityLongSign    The sign of the long quantity (positive/negative).
     * @param quantityLong        The quantity of the long position.
     * @param quantityShortSign   The sign of the short quantity (positive/negative).
     * @param quantityShort       The quantity of the short position.
     * @param exchBrokerFeeDec    The decimal value of the exchange broker fee.
     * @param exchBrokerFeeDC     The debit/credit indicator for the exchange broker fee.
     * @param exchBrokerFeeCurCode The currency code for the exchange broker fee.
     * @param clearingFeeDec      The decimal value of the clearing fee.
     * @param clearingFeeDC       The debit/credit indicator for the clearing fee.
     * @param clearingFeeCurCode  The currency code for the clearing fee.
     * @param commission          The commission amount.
     * @param commissionDC        The debit/credit indicator for the commission.
     * @param commissionCurCode   The currency code for the commission.
     * @param transactionDate     The date of the transaction.
     * @param futureReference     A reference identifier for the future transaction.
     * @param ticketNumber        The ticket number associated with the transaction.
     * @param externalNumber      An external number related to the transaction.
     * @param transactionPriceDec The decimal value of the transaction price.
     * @param traderInitials      The initials of the trader who executed the transaction.
     * @param oppositeTraderId    The ID of the opposite trader involved in the transaction.
     * @param openCloseCode       The code indicating whether the position is opened or closed.
     * @param filler              A filler field for future use or additional information.
     */
}
