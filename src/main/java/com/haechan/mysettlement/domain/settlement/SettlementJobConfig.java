package com.haechan.mysettlement.domain.settlement;

import com.haechan.mysettlement.domain.contract.entity.Contract;
import com.haechan.mysettlement.domain.revenue.dto.RevenueDto;
import com.haechan.mysettlement.domain.revenue.entity.Revenue;
import com.haechan.mysettlement.domain.revenue.repository.RevenueRepository;
import com.haechan.mysettlement.domain.settlement.entity.Settlement;
import com.haechan.mysettlement.domain.settlement.repository.SettlementRepository;
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
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SettlementJobConfig {

    private static final int chunkSize = 10;

    private final SettlementRepository settlementRepository;
    private final RevenueRepository revenueRepository;

    // job
    @Bean
    public Job settlement_batchBuild(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        System.out.println("잡");
        return new JobBuilder("settlement_batchBuild", jobRepository)
                .start(settlement_batchStep(jobRepository, transactionManager))
                .build();
    }

    // step
    @Bean
    @JobScope
    public Step settlement_batchStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        System.out.println("스텝");
        return new StepBuilder("settlement_batchStep", jobRepository)
                // I, O
                .<Revenue, Settlement>chunk(chunkSize, transactionManager)
                .reader(settlementReader())
                .processor(getFeeProcessor())
                .writer(settlementWriter())
                .build();
    }

    // reader
    // RepositoryItemReader, Writer
    // https://velog.io/@alstn_dev/게시판-프로젝트-스프링-배치-적용1
    @Bean
    @StepScope
    public RepositoryItemReader<Revenue> settlementReader() {

        System.out.println("1. 리더 실행");
        return new RepositoryItemReaderBuilder<Revenue>()
                .name("settleReader")
                .repository(revenueRepository)
                // 리스트가 아닌 페이지 리턴해야 함
                // repository의 메소드
                .methodName("findByThisMonth")
                // 이전에 설정한 chunk와 동일한 사이즈
                .pageSize(chunkSize)
                // 매개변수
                .arguments(LocalDate.now())
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .build();
    }

    // processor
    // settlement.calculate를 여기에??
    @Bean
    @StepScope
    // input output
    // Revenue 타입 리더에서 읽고 프로세서에서 프로세스 후 Settlement 타입으로 라이터에게 리턴
    public ItemProcessor<Revenue, Settlement> getFeeProcessor() {

        System.out.println("2. 프로세서 실행");

        return revenue -> {
            RevenueDto dto = RevenueDto.builder()
                    .contract(revenue.getContract())
                    .fee(revenue.getFee())
                    .date(revenue.getDate())
                    .build();

            System.out.println("리더에서 받은 revenue : ");
            log.info("(processor!) revenue.getId() = " + revenue.getId());

            // return dto;

            // 계약서
            Contract contract = dto.getContract();
            log.info("contract.getId() = {}", contract.getId());

            // 수익 원금액
            Double fee = dto.getFee();
            log.info("fee = " + fee);

            // 1. 유통사 정산
            Double distributorRate = contract.getDistributor().getPercent() * 0.01;
            log.info("distributorRate = {}", distributorRate);

            Double distributorFee = fee * distributorRate;
            log.info("distributorFee = {}", distributorFee);
            fee -= distributorFee;
            log.info("fee2 = {}", fee);

            Settlement distributorSettlement = Settlement.builder()
                    .contract(contract)
                    .type(1L)
                    .memberId(contract.getDistributor().getId())
                    .settleDate(LocalDateTime.now())
                    .fee(distributorFee)
                    .build();

            log.info("distributorSettlement.getFee() = {}", distributorSettlement.getFee());
            log.info("distributorSettlement.getSettleDate() = {}", distributorSettlement.getSettleDate());

            return distributorSettlement;
        };
    }

    @Bean
    @StepScope
    public RepositoryItemWriter<Settlement> settlementWriter() {

        System.out.println("3. 라이터");

        // 메소드명 없을 시 saveAll
        return new RepositoryItemWriterBuilder<Settlement>()
                .repository(settlementRepository)
                .build();
    }
}
