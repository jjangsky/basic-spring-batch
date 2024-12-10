package com.example.project.job.JobListener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

@Slf4j
public class JobLoggerListener implements JobExecutionListener {

    private static String BEFORE_MESSAGE = "{} Job is Running";

    private static String AFTER_MESSAGE = "{} Job is Done. (Status: {})";

    @Override
    public void beforeJob(JobExecution jobExecution){
        // 현재 실행되는 Job의 이름을 가져올 수 있음
        log.info(BEFORE_MESSAGE, jobExecution.getJobInstance().getJobName());
    }

    @Override
    public void afterJob(JobExecution jobExecution){
        log.info(AFTER_MESSAGE,
                jobExecution.getJobInstance().getJobName(),
                jobExecution.getStatus());

        if (jobExecution.getStatus() == BatchStatus.FAILED){
            // 해당 Job이 실패한 경우
            log.info("Job is Failed");
        }
    }

}
