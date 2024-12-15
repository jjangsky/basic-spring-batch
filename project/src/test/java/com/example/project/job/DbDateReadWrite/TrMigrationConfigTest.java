package com.example.project.job.DbDateReadWrite;


import com.example.project.core.domain.accounts.AccountsRepository;
import com.example.project.core.domain.orders.Orders;
import com.example.project.core.domain.orders.OrdersRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBatchTest
@SpringBootTest
@ActiveProfiles("local")
class TrMigrationConfigTest {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job trMigrationJob;

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private AccountsRepository accountsRepository;

    @AfterEach
    public void cleanUpEach() {
        ordersRepository.deleteAll();
        accountsRepository.deleteAll();
    }

    @Test
    public void success_noData() throws Exception {
        // when
        JobExecution execution = jobLauncher.run(
                trMigrationJob,
                new JobParametersBuilder()
                        .addLong("time", System.currentTimeMillis())
                        .toJobParameters()
        );

        // then
        assertThat(execution.getExitStatus()).isEqualTo(org.springframework.batch.core.ExitStatus.COMPLETED);
        assertThat(accountsRepository.count()).isZero();
    }

    @Test
    public void success_existData() throws Exception {
        // given
        Orders orders1 = new Orders(null, "kakao gift", 15000, new Date());
        Orders orders2 = new Orders(null, "naver gift", 15000, new Date());
        ordersRepository.save(orders1);
        ordersRepository.save(orders2);

        // when
        JobExecution execution = jobLauncher.run(
                trMigrationJob,
                new JobParametersBuilder()
                        .addLong("time", System.currentTimeMillis())
                        .toJobParameters()
        );

        // then
        assertThat(execution.getExitStatus()).isEqualTo(org.springframework.batch.core.ExitStatus.COMPLETED);
        assertThat(accountsRepository.count()).isEqualTo(2);
    }

}