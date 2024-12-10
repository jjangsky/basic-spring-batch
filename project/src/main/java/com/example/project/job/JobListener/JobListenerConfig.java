package com.example.project.job.JobListener;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class JobListenerConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean
    public Job jobListenerJob() {
        return new JobBuilder("jobListenerJob", jobRepository)
                .listener(new JobLoggerListener())
                .start(jobListenerStep())
                .build();
    }

    @Bean
    public Step jobListenerStep() {
        return new StepBuilder("jobListenerStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("jobListenerStep!");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }


}
