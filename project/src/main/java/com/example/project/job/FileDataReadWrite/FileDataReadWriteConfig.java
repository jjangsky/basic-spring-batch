package com.example.project.job.FileDataReadWrite;

import com.example.project.core.domain.accounts.Accounts;
import com.example.project.core.domain.orders.Orders;
import com.example.project.job.FileDataReadWrite.dto.Player;
import com.example.project.job.FileDataReadWrite.dto.PlayerYears;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class FileDataReadWriteConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean
    public Job fileReadWriteJob(Step fileReadWriteStep) {
        return new JobBuilder("fileReadWriteJob", jobRepository)
                .start(fileReadWriteStep)
                .build();
    }

    @Bean
    public Step fileReadWriteStep(ItemReader playerItemReader,
                                  ItemProcessor playerItemProcessor,
                                  ItemWriter playerItemWriter) {
        return new StepBuilder("fileReadWriteStep", jobRepository)
                .<Player, Player>chunk(5, transactionManager)
                .reader(playerItemReader)
                .processor(playerItemProcessor)
                .writer(playerItemWriter)
                .build();
    }

    /**
     * 이전에는 Repository에서 데이터를 읽어 왔지만
     * 이번에는 File에서 데이터를 읽어오기 때문에 다른 객체를 사용
     * FlatFileItemReader 사용
     */
    @StepScope
    @Bean
    public FlatFileItemReader<Player> playerItemReader() {
        return new FlatFileItemReaderBuilder<Player>()
                .name("playerItemReader")
                .resource(new FileSystemResource("Player.csv")) // 읽어올 파일 지정
                .lineTokenizer(new DelimitedLineTokenizer()) // 데이터를 나누는 기준 생성
                .fieldSetMapper(new PlayerFieldSetMapper()) // 읽어온 데이터를 객체로 변환하기 위한 Mapper 필요함
                .linesToSkip(1) // 첫 번째 줄은 제목이여서 첫 번째 줄 Skip 처리
                .build();
    }

    // Player -> PlayerYears로 변환과정
    @StepScope
    @Bean
    public ItemProcessor<Player, PlayerYears> playerItemProcessor() {
        return PlayerYears::new;
    }

    @StepScope
    @Bean
    public FlatFileItemWriter<PlayerYears> playerItemWriter() {
        BeanWrapperFieldExtractor<PlayerYears> fieldExtractor = new BeanWrapperFieldExtractor<>();
        // 어떤 필드를 추출할 것인지 배열로 입력 처리
        fieldExtractor.setNames(new String[]{"ID", "lastName", "position", "yearsExperience"});
        fieldExtractor.afterPropertiesSet();

        // 어떤 기준으로 파일을 만드는지 알려주기 위함
        DelimitedLineAggregator<PlayerYears> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter("="); // 다시 csv로 반환해야 해서 구분값 명시
        lineAggregator.setFieldExtractor(fieldExtractor);

        // 반환될 파일 생성
        FileSystemResource outputResource = new FileSystemResource("players_output.txt");
        return new FlatFileItemWriterBuilder<PlayerYears>()
                .name("playerItemWriter")
                .resource(outputResource)
                .lineAggregator(lineAggregator)
                .build();
    }
}
