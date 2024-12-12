package com.example.project.job.DbDateReadWrite;

import com.example.project.core.domain.accounts.Accounts;
import com.example.project.core.domain.accounts.AccountsRepository;
import com.example.project.core.domain.orders.Orders;
import com.example.project.core.domain.orders.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Collections;

/**
 * 주문 테이블 -> 정산 테이블 데이터 이관 처리
 * 데이터를 읽고 쓰는 과정에서 객체에 담고 옮기므로 `주문` 객체와 `정산` 객체가 필요
 */
@Configuration
@RequiredArgsConstructor
public class TrMigrationConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final OrdersRepository ordersRepository;
    private final AccountsRepository accountsRepository;

    @Bean
    public Job trMigrationJob(Step trMigrationStep) {
        return new JobBuilder("trMigrationJob", jobRepository)
                .start(trMigrationStep)
                .build();
    }

    /**
     * chunk는 몇 개의 단위로 데이터를 처리할 것인지 명시하는 것이다.
     * 예시에서는 5개로 설정되어 있어 5개를 처리 후 그 이후에 5개를 처리하는 방식
     * chunk 앞의 제네릭 설정은 읽어올 데이터의 타입과 작성할 데이터의 타입을 의미한다.
     */
    @Bean
    public Step trMigrationStep(ItemReader<Orders> trOrdersReader,
                                ItemProcessor<Orders, Accounts> trOrderProcessor,
                                ItemWriter<Accounts> trOrdersWriter) {
        return new StepBuilder("trMigrationStep", jobRepository)
                .<Orders, Accounts>chunk(20, transactionManager)
                .reader(trOrdersReader)
                .processor(trOrderProcessor)
                .writer(trOrdersWriter)
                .build();
    }

    /**
     * 데이터를 읽어오는 과정
     * RepositoryItemReaderBuilder 를 사용하여 Repository에서 데이터를 읽어올 수 있다.
     * pagesize는 chunk 사이즈와 동일하게 설정한다.
     */
    @Bean
    @StepScope
    public RepositoryItemReader<Orders> trOrdersReader() {
        return new RepositoryItemReaderBuilder<Orders>()
                .name("trOrdersReader")
                .repository(ordersRepository)
                .methodName("findAll")
                .pageSize(5)
                .arguments(Collections.emptyList()) // 넘길 인자가 없으므로 빈 배열 전달
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .build();
    }

    /**
     * 데이터를 변환하는 과정
     * 읽어온 데이터에서 작성할 데이터로 변환하는 작업이다.
     * Orders 객체에 데이터를 DB에서 조회가 완료 되었으므로 해당 과정에서 Account로 등록될 수 있도록
     * `new Accounts` 를 사용해서 변환 과정 필요하다.
     */
    @Bean
    @StepScope
    public ItemProcessor<Orders, Accounts> trOrderProcessor() {
        return item -> new Accounts(item); // Lambda expression으로 간소화
    }

    /**
     * 데이터를 작성하는 과정
     * Processor에서 변경된 데이터를 저장하는 단계
     */
    @Bean
    @StepScope
    public ItemWriter<Accounts> trOrdersWriter() {
        return items -> items.forEach(accountsRepository::save); // Lambda expression으로 간소화
    }
}
