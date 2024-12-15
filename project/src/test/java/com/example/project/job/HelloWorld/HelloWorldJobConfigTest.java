package com.example.project.job.HelloWorld;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBatchTest
@SpringBootTest
@ActiveProfiles("test")
class HelloWorldJobConfigTest {

    // batch에 대한 테스트 코드 작성할 때 하나의 job은 하나의 testCode를 작성한다.


    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private Job helloWorldJob;

    @Test
    public void success() throws Exception {
        // 테스트 데이터 초기화 (JobInstance/JobExecution 제거)
        JobRepositoryTestUtils jobRepositoryTestUtils = new JobRepositoryTestUtils(jobRepository);
        jobRepositoryTestUtils.removeJobExecutions();

        // when: Job 실행
        JobExecution execution = jobLauncher.run(helloWorldJob, new JobParameters());

        // then: 정상 완료 여부 확인
        Assertions.assertEquals(ExitStatus.COMPLETED, execution.getExitStatus());
    }


}