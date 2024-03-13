package com.haechan.finance.global.batch;

import com.haechan.feign.dto.ContractFeignResponse;
import com.haechan.feign.dto.DistributorFeignResponse;
import com.haechan.feign.dto.OstFeignResponse;
import com.haechan.feign.dto.ProducerFeignResponse;
import com.haechan.finance.domain.revenue.dto.RevenueDto;
import com.haechan.finance.domain.revenue.entity.Revenue;
import com.haechan.finance.domain.revenue.repository.RevenueRepository;
import com.haechan.finance.domain.settlement.entity.Settlement;
import com.haechan.finance.domain.settlement.repository.SettlementRepository;
import com.haechan.finance.global.feign.ContractFeignClient;
import com.haechan.finance.global.feign.DistributorFeignClient;
import com.haechan.finance.global.feign.OstFeignClient;
import com.haechan.finance.global.feign.ProducerFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.haechan.finance.domain.settlement.dto.MemberType.*;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableBatchProcessing
public class SettlementJobConfig {

    private static final int chunkSize = 10;

    private final SettlementRepository settlementRepository;
    private final RevenueRepository revenueRepository;
    private final ContractFeignClient contractFeignClient;
    private final DistributorFeignClient distributorFeignClient;
    private final OstFeignClient ostFeignClient;
    private final ProducerFeignClient producerFeignClient;

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
                .<Revenue, List<Settlement>>chunk(chunkSize, transactionManager)
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
    @Bean
    @StepScope
    // input output
    // Revenue 타입 리더에서 읽고 프로세서에서 프로세스 후 Settlement 타입으로 라이터에게 리턴
    public ItemProcessor<Revenue, List<Settlement>> getFeeProcessor() {

        System.out.println("2. 프로세서 실행");

        return revenue -> {
            RevenueDto dto = RevenueDto.builder()
                    .contractId(revenue.getContractId())
                    .fee(revenue.getFee())
                    .date(revenue.getDate())
                    .build();

            System.out.println("리더에서 받은 revenue : ");
            log.info("(processor!) revenue.getId() = " + revenue.getId());

            // return dto;

            // 계약서
//            Contract contract = dto.getContract();
//            log.info("contract.getId() = {}", contract.getId());


            ContractFeignResponse contractFeignResponse = contractFeignClient.findByContractId(dto.getContractId());



            // 수익 원금액
            Double fee = dto.getFee();
            log.info("fee = " + fee);

            // 4개 settlement 객체 저장할 리스트
            List<Settlement> settlementList = new ArrayList<>();

            // 1. 유통사 정산
//            Double distributorRate = contract.getDistributor().getPercent() * 0.01;
            Long distributorId = contractFeignResponse.getDistributorId();

            DistributorFeignResponse distributorFeignResponse = distributorFeignClient.findDistributorById(distributorId);
            Double distributorRate = distributorFeignResponse.getPercent() * 0.01;

            log.info("distributorRate = {}", distributorRate);

            Double distributorFee = fee * distributorRate;
            log.info("distributorFee = {}", distributorFee);
            fee -= distributorFee;
            log.info("fee2 = {}", fee);

            Settlement distributorSettlement = Settlement.builder()
                    .contractId(contractFeignResponse.getContractId())
                    .type(DISTRIBUTOR)
                    .memberId(distributorId)
                    .settleDate(LocalDateTime.now())
                    .fee(distributorFee)
                    .build();

            log.info("distributorSettlement.getFee() = {}", distributorSettlement.getFee());
            log.info("distributorSettlement.getSettleDate() = {}", distributorSettlement.getSettleDate());

            settlementList.add(distributorSettlement);

            // 2. 가창자 정산
            Double singerRate = contractFeignResponse.getSingerPercent() * 0.01;
            Double singerFee = fee * singerRate;
            fee -= singerFee;

            log.info("contractFeignResponse.getOstId() = {}", contractFeignResponse.getOstId());

            OstFeignResponse ostFeignResponse = ostFeignClient.findOstById(contractFeignResponse.getOstId());
            log.info("ostFeignResponse = {}", ostFeignResponse);

            Settlement singerSettlement = Settlement.builder()
                    .contractId(contractFeignResponse.getContractId())
                    .type(SINGER)
                    .memberId(ostFeignResponse.getSingerId())
                    .settleDate(LocalDateTime.now())
                    .fee(singerFee)
                    .build();

            settlementList.add(singerSettlement);

            // 제작사 정산

            ProducerFeignResponse producerFeignResponse = producerFeignClient.findProducerById(ostFeignResponse.getProducerId());

            Double producerRate = contractFeignResponse.getProducerPercent() * 0.01;
            log.info("producerRate = {}", producerRate);
            Double producerFee = fee * producerRate;
            log.info("producerFee = {}", producerFee);
            fee -= producerFee;

            Settlement producerSettlement = Settlement.builder()
                    .contractId(contractFeignResponse.getContractId())
                    .type(PRODUCER)
                    .memberId(producerFeignResponse.getProducerId())
                    .settleDate(LocalDateTime.now())
                    .fee(producerFee)
                    .build();

            settlementList.add(producerSettlement);

            // 본사 정산
            Settlement companySettlement = Settlement.builder()
                    .contractId(contractFeignResponse.getContractId())
                    .type(COMPANY)
                    .memberId(0L)
                    .settleDate(LocalDateTime.now())
                    .fee(fee)
                    .build();

            settlementList.add(companySettlement);

            // 정산이 완료된 `Settlement` 객체 리스트 반환
            return settlementList;
        };
    }

    // RepositoryItemWriter는 하나의 엔티티 저장
    // but Revenue 하나 당 Settlement 객체 4개 담긴 리스트 저장해야 하므로 커스텀 ItemWriter 사용
    @Bean
    @StepScope
    public SettlementListWriter settlementWriter() {

        System.out.println("3. 라이터");

        return new SettlementListWriter(settlementRepository);
    }
}