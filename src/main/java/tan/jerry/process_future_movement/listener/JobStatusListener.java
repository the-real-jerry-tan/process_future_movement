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
package tan.jerry.process_future_movement.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * JobStatusListener is a Spring component that listens for the lifecycle events of a batch job.
 * It implements the JobExecutionListener interface to provide custom behavior before and after
 * job execution, such as logging the job status and sending notification emails.
 */
@Component
public class JobStatusListener implements JobExecutionListener {

    private static final Logger logger = LoggerFactory.getLogger(JobStatusListener.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Environment env;

    private final MeterRegistry meterRegistry;

    @Value("${enable.sendmail:false}") // Read enable_sendmail from application properties, default is false
    private boolean enableSendMail;

    @Autowired
    public JobStatusListener(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }//ctor

    /**
     * Called before the job starts. Logs the start of the job and sends an email notification.
     *
     * @param jobExecution The JobExecution object representing the job about to start.
     */
    @Override
    public void beforeJob(JobExecution jobExecution) {
        meterRegistry.counter("before.job.executions", "jobName", jobExecution.getJobInstance().getJobName()).increment();
        logger.trace("Job started: {}", jobExecution.getJobInstance().getJobName());
        if (enableSendMail) {
            sendEmail("Job Started", "The job has started successfully.");
        } else {
            logger.trace("Email sending is disabled.");
        }
    }

    /**
     * Called after the job ends. Logs the completion or failure of the job and sends an email notification.
     *
     * @param jobExecution The JobExecution object representing the job that has finished.
     */
    @Override
    public void afterJob(JobExecution jobExecution) {
        meterRegistry.counter("after.job.executions", "jobName", jobExecution.getJobInstance().getJobName()).increment();
        if (jobExecution.getStatus().isUnsuccessful()) {
            logger.trace("Job failed: {}", jobExecution.getJobInstance().getJobName());
            if (enableSendMail) {
                sendEmail("Job Failed", "The job has failed.");
            } else {
                logger.trace("Email sending is disabled.");
            }
        } else {
            logger.trace("Job completed: {}", jobExecution.getJobInstance().getJobName());
            if (enableSendMail) {
                sendEmail("Job Completed", "The job has completed successfully.");
            } else {
                logger.trace("Email sending is disabled.");
            }
        }
    }

    /**
     * Sends an email notification with the specified subject and body to the distribution list.
     * The distribution list and subject prefix are retrieved from the application environment properties.
     *
     * @param subjectSuffix The suffix to be appended to the email subject.
     * @param body          The body of the email message.
     */
    private void sendEmail(String subjectSuffix, String body) {
        String[] distributionList = env.getProperty("email.distribution.list", "").split(",");
        String subject = env.getProperty("email.subject.prefix", "[Batch Job]") + " " + subjectSuffix;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(distributionList);
        message.setSubject(subject);
        message.setText(body);

        try {
            mailSender.send(message);
            logger.trace("Email sent to: {}", String.join(", ", distributionList));
        } catch (Exception e) {
            logger.trace("Failed to send email", e);
        }
    }
}
