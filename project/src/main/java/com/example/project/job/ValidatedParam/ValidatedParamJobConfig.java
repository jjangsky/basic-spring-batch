package com.example.project.job.ValidatedParam;

import com.example.project.job.ValidatedParam.Validator.FileParamValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.CompositeJobParametersValidator;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Arrays;

/**
 * 파일 이름 파라미터 전달 및 검증
 */
@Configuration
@RequiredArgsConstructor
public class ValidatedParamJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean
    public Job validateParamJob() {
        return new JobBuilder("validateParamJob", jobRepository)
                // 파라미터 검증에 대해서 Taslket 까지 가지 않아도 Job에서 validate 처리 가능
                .validator(multipleValidator())
                .start(validateParamStep())
                .build();
    }

    // 여러 개의 Validate가 필요한 경우
    private CompositeJobParametersValidator multipleValidator(){
        CompositeJobParametersValidator validator = new CompositeJobParametersValidator();
        validator.setValidators(Arrays.asList(new FileParamValidator()));
        return validator;
    }

    @Bean
    public Step validateParamStep() {
        return new StepBuilder("validateParamStep", jobRepository)
                .tasklet(validateParamTasklet(null), transactionManager)
                .build();
    }

    @Bean
    @StepScope
    public Tasklet validateParamTasklet(@Value("#{jobParameters['fileName']}") String fileName){
        return (contribution, chunkContext) -> {
            System.out.println(fileName);
            System.out.println("\n *********** my first spring batch ***********");
            return RepeatStatus.FINISHED;
        };
    }


}
