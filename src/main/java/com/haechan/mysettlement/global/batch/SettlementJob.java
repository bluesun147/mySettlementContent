package com.haechan.mysettlement.global.batch;

import com.haechan.mysettlement.domain.revenue.repository.RevenueRepository;
import com.haechan.mysettlement.domain.settlement.entity.Settlement;
import com.haechan.mysettlement.domain.settlement.repository.SettlementRepository;
import com.haechan.mysettlement.domain.settlement.service.SettlementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;
import java.util.Collections;


// https://sh970901.tistory.com/87

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SettlementJob {

    /*
    spring batch 5부터 deprecated됨.
    jobBuilder 파라미터로 명시적 명시할것.
    https://alwayspr.tistory.com/49
    */
//    private final JobBuilderFactory jobBuilderFactory;
//    private final StepBuilderFactory stepBuilderFactory;
    private static final int chunkSize = 100;

    private final SettlementService settlementService;
    private final SettlementRepository settlementRepository;
    private final RevenueRepository revenueRepository;

    // job
    @Bean
    public Job settlementJob_batchBuild(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("settlementJob_batchBuild", jobRepository)
                .start(settlementJob_batchStep(jobRepository, transactionManager))
                .build();
    }

    // step
    @Bean
    @JobScope
    public Step settlementJob_batchStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("settlementJob_batchStep", jobRepository)
                // I, O
                .<Settlement, Settlement>chunk(chunkSize, transactionManager)
                .reader(settlementReader())
                .processor(getFeeProcessor())
                .writer(settlementWriter())
                .build();
    }

    // reader
    // 읽을 대상 설정.
    @Bean
    public RepositoryItemReader<Settlement> settlementReader() {

        return new RepositoryItemReaderBuilder<Settlement>()
                .name("settlementReader")
                .repository(settlementRepository)
                // 리스트가 아닌 페이지 리턴해야 함
                // repository의 메소드
                .methodName("findAll")
                // 이전에 설정한 chunk와 동일한 사이즈
                .pageSize(chunkSize)
                // 매개변수
                .arguments()
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .build();
    }

    // processor
    // settlement.calculate를 여기에??
    // 테스트 : 200원 이상인 객체는 지금 날짜로 저장 (02.20)
    @StepScope
    @Bean
    public ItemProcessor<Settlement, Settlement> getFeeProcessor() {
        return settlement -> {
            if (settlement.getFee() > 200L) {
                return new Settlement(settlement.getContract(), settlement.getType(), settlement.getMemberId(), LocalDateTime.now(), 999.0);
            } else {
                return null;
            }
        };
    }

    // writer
    // 쓸 대상에 대해 설정
    @StepScope
    @Bean
    public ItemWriter<Settlement> settlementWriter() {
        return settlements -> settlements.forEach(settlementRepository::save);
    }

}
