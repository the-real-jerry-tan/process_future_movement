/**
 * © 2024 Jerry Tan. All Rights Reserved.
 * <p>
 * This software is the confidential and proprietary information of Jerry Tan
 * ("Confidential Information"). You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms under which this software
 * was distributed or otherwise published, and solely for the prior express purposes
 * explicitly communicated and agreed upon, and only for those specific permissible purposes.
 * <p>
 * This software is provided "AS IS," without a warranty of any kind. All express or implied
 * conditions, representations, and warranties, including any implied warranty of merchantability,
 * fitness for a particular purpose, or non-infringement, are disclaimed, except to the extent
 * that such disclaimers are held to be legally invalid.
 */
package tan.jerry.process_future_movement.service;

import java.util.Map;

/**
 * The {@code ProcessFutureMovementService} interface defines a contract for processing
 * future movement transactions. This interface is designed to be implemented by various
 * classes, each using a different method or algorithm for processing data. The choice of
 * implementation can be based on the specific requirements of the environment or workload,
 * such as performance needs, complexity, or volume of data.
 *
 * <p>This design allows for flexible selection of the processing method:
 * <ul>
 *   <li><b>Sequential Processing</b>: Suitable for low-volume environments or during
 *   development, where the overhead of parallel processing is unnecessary.</li>
 *   <li><b>Parallel Streams</b>: Used when the workload is high and can benefit from parallel
 *   processing, but when the work can be done in parallel chunks without needing specific order.</li>
 *   <li><b>MapReduce</b>: Appropriate for situations where tasks can be divided into separate
 *   map and reduce phases, providing good performance in a scalable manner, especially
 *   when dealing with large data sets.</li>
 * </ul>
 *
 * <p>The interface allows for easy swapping of the processing logic by selecting the appropriate
 * implementation class based on the configuration or runtime conditions.
 */
public interface ProcessFutureMovementService {

    /**
     * Processes the input map containing client-product transaction data and performs the necessary
     * calculations or transformations. The default implementation simply returns the input map
     * unchanged, but implementing classes will override this method with specific logic tailored
     * to their processing approach.
     *
     * @param map A map containing client-product keys and corresponding transaction amounts.
     * @return A map with processed transaction data, which may involve aggregation, filtering, or
     * other transformations depending on the implementation.
     */
    public default Map<String, Double> process(Map<String, Double> map) {
        return map;
    }
}
