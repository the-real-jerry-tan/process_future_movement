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
package tan.jerry.process_future_movement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The entry point of the ProcessFutureMovement Spring Boot application.
 * This class is responsible for launching the Spring Boot application
 * and initializing all components and configurations.
 */
@SpringBootApplication
public class ProcessFutureMovementApplication {

    /**
     * The main method that serves as the entry point for the Spring Boot application.
     * It uses SpringApplication.run to launch the application, which will initialize the
     * Spring context and all the Spring Boot features such as auto-configuration, component scanning,
     * and other Spring Boot settings.
     *
     * @param args Command line arguments passed to the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(ProcessFutureMovementApplication.class, args);
    }

}//spring boot app
