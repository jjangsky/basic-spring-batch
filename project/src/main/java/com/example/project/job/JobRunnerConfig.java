package com.example.project.job;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class JobRunnerConfig {

    private final JobLauncher jobLauncher;
    private final Job helloWorldJob;
    private final Job validateParamJob;
    private final Job jobListenerJob;

    @Bean
    public CommandLineRunner runJob() {
        return args -> {
            // Job 실행 시 필요한 Param 설정
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .addString("fileName", "data.csv") // 유효한 파일 이름 전달
                    .toJobParameters();

            try {
                jobLauncher.run(jobListenerJob, jobParameters);
            } catch (Exception e) {
                // Validate 예외 처리 발생
                System.err.println("Job execution failed: " + e.getMessage());
            }
        };
    }
}
