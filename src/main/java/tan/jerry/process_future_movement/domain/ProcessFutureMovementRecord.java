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
 * ProcessFutureMovementRecord is an empty marker interface used to abstract testing between
 * input and output records in the process future movement system.
 *
 * <p>This interface serves as a common type for both InputRecord and OutputRecord,
 * enabling the client to work with a generic type without concern for whether the
 * record is input or output at the testing level. This abstraction simplifies the
 * implementation and testing processes, particularly when operations or tests need to
 * be applied uniformly across different types of records.</p>
 *
 * <p>Note: This interface does not define any methods, serving purely as a marker for
 * type hierarchy purposes.</p>
 */
public interface ProcessFutureMovementRecord {
    // No methods or fields defined; serves as a common type for InputRecord and OutputRecord.
}
