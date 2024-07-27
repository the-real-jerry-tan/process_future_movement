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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import tan.jerry.process_future_movement.domain.InputRecord;
import tan.jerry.process_future_movement.domain.OutputRecord;
import tan.jerry.process_future_movement.listener.JobStatusListener;
import tan.jerry.process_future_movement.log.Ayaka;
import tan.jerry.process_future_movement.reader.ProcessFutureMovementReader;
import tan.jerry.process_future_movement.service.ProcessFutureMovementProcessor;
import tan.jerry.process_future_movement.service.ProcessFutureMovementTaskletProcessor;
import tan.jerry.process_future_movement.writer.ProcessFutureMovementWriter;

/**
 * BatchJobConfig is a configuration class that sets up the Spring Batch job,
 * steps, readers, processors, and writers for processing future movements.
 */
@Configuration
@EnableBatchProcessing
public class BatchJobConfig {

    /**
     * Logger for logging messages.
     */
    private static final Logger logger = LoggerFactory.getLogger(BatchJobConfig.class);

    /**
     * Repository for storing job data.
     */
    private final JobRepository jobRepository;

    /**
     * Manager for handling transactions.
     */
    private final PlatformTransactionManager transactionManager;

    /**
     * Listener for job status updates.
     */
    private final JobStatusListener listener;

    @Value("${input.file.name}")
    private String input_file_name;

    @Value("${output.file.name}")
    private String output_file_name;

    @Value("${ayaka.log.name}")
    private String ayaka_log;

    private static boolean ignoreSignIndicators;

    /**
     * Sets whether to ignore sign indicators in the input data.
     *
     * @param ignoreSignIndicators Boolean flag indicating whether to ignore sign indicators.
     */
    @Value("${ignore.sign.indicators:false}") // Default value is 'false' if not set
    public void setIgnoreSignIndicators(String ignoreSignIndicators) {
        BatchJobConfig.ignoreSignIndicators = Boolean.parseBoolean(ignoreSignIndicators);
    }

    /**
     * Checks if the sign indicators should be ignored.
     *
     * @return True if sign indicators should be ignored, false otherwise.
     */
    public static boolean shouldIgnoreSignIndicators() {
        return ignoreSignIndicators;
    }

    /**
     * Constructs a new BatchJobConfig.
     *
     * @param jobRepository        The job repository.
     * @param transactionManager   The transaction manager.
     * @param listener             The job status listener.
     */
    public BatchJobConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager, JobStatusListener listener) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.listener = listener;
    }

    /**
     * Defines the job for processing future movements.
     *
     * @return The configured Job instance.
     */
    @Bean
    public Job job() {
        return new JobBuilder("pfm_job", jobRepository)
                .start(pfm_step())
                .next(finalize_step())
                .build();
    }

    /**
     * Defines the initialization step for the job.
     *
     * @return The configured Step instance for initialization.
     */
    @Bean
    public Step init_step() {
        return new StepBuilder("init_step", jobRepository)
                .tasklet(init_tasklet(), transactionManager)
                .build();
    }

    /**
     * Defines the main step for processing future movements.
     *
     * @return The configured Step instance for processing.
     */
    @Bean
    public Step pfm_step() {
        Ayaka ayaka = new Ayaka(ayaka_log);
        ayaka.log("Invoking pfm_step");
        return new StepBuilder("pfm_step", jobRepository)
                .<InputRecord, OutputRecord>chunk(10, transactionManager)
                .reader(pfm_reader())
                .processor(pfm_processor())
                .writer(pfm_writer())
                .build();
    }

    /**
     * Defines the ItemReader for the job.
     *
     * @return The configured ItemReader instance.
     */
    @Bean
    public ItemReader<? extends InputRecord> pfm_reader() {
        return new ProcessFutureMovementReader();
    }

    /**
     * Defines the ItemProcessor for the job.
     *
     * @return The configured ItemProcessor instance.
     */
    @Bean
    public ItemProcessor<? super InputRecord, ? extends OutputRecord> pfm_processor() {
        return new ProcessFutureMovementProcessor();
    }

    /**
     * Defines the ItemWriter for the job.
     *
     * @return The configured ItemWriter instance.
     */
    @Bean
    public ItemWriter<? super OutputRecord> pfm_writer() {
        return new ProcessFutureMovementWriter();
    }

    /**
     * Defines the final step for the job.
     *
     * @return The configured Step instance for finalization.
     */
    @Bean
    public Step finalize_step() {
        return new StepBuilder("finalize_step", jobRepository)
                .tasklet(finalize_tasklet(), transactionManager)
                .build();
    }

    /**
     * Defines the Tasklet for the initialization step.
     *
     * @return The configured Tasklet instance for initialization.
     */
    @Bean
    public Tasklet init_tasklet() {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                logger.trace("executing init_tasklet");
                return RepeatStatus.CONTINUABLE;
            }
        };
    }

    /**
     * Defines the Tasklet for processing future movements.
     *
     * @return The configured Tasklet instance for processing.
     */
    @Bean
    public Tasklet process_future_movement_tasklet() {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                logger.trace("executing pfm tasklet");
                return RepeatStatus.CONTINUABLE;
            }
        };
    }

    /**
     * Defines the Tasklet for the finalization step.
     *
     * @return The configured Tasklet instance for finalization.
     */
    @Bean
    public Tasklet finalize_tasklet() {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                logger.trace("executing finalize_tasklet");
                ProcessFutureMovementTaskletProcessor.main(null);
                return RepeatStatus.FINISHED;
            }
        };
    }
}
