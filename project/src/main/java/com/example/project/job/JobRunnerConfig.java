package com.example.project.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobRunnerConfig {

    private final JobLauncher jobLauncher;
    private final Job helloWorldJob;

    public JobRunnerConfig(JobLauncher jobLauncher, Job helloWorldJob) {
        this.jobLauncher = jobLauncher;
        this.helloWorldJob = helloWorldJob;
    }

    @Bean
    public CommandLineRunner runJob() {
        return args -> {
            jobLauncher.run(
                    helloWorldJob,
                    new JobParametersBuilder()
                            .addLong("time", System.currentTimeMillis())
                            .toJobParameters()
            );
        };
    }
}
