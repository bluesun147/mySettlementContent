package com.haechan.finance.domain.settlement.service;

import com.haechan.feign.dto.ContractFeignResponse;
import com.haechan.feign.dto.DistributorFeignResponse;
import com.haechan.feign.dto.OstFeignResponse;
import com.haechan.finance.domain.revenue.dto.RevenueDto;
import com.haechan.finance.domain.revenue.entity.Revenue;
import com.haechan.finance.domain.revenue.repository.RevenueRepository;
import com.haechan.finance.domain.settlement.dto.MemberType;
import com.haechan.finance.domain.settlement.entity.Settlement;
import com.haechan.finance.global.feign.ContractFeignClient;
import com.haechan.finance.global.feign.DistributorFeignClient;
import com.haechan.finance.domain.settlement.repository.SettlementRepository;
import com.haechan.finance.global.feign.OstFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.haechan.finance.domain.settlement.dto.MemberType.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class SettlementService {

    private final SettlementRepository settlementRepository;
    private final RevenueRepository revenueRepository;
    private final ContractFeignClient contractFeignClient;
    private final DistributorFeignClient distributorFeignClient;
    private final OstFeignClient ostFeignClient;

    // 특정달 수익에 대한 정산
    // 유통사 -> 가창자 -> 제작사 -> 본사 순
    // distributor -> singer -> producer
    public void calculate(LocalDate date) {
        // 특정달 수익 객체 전부 가져오기
        List<Revenue> revenueList = revenueRepository.findByDate(date);
        List<RevenueDto> revenueDtoList = revenueList.stream()
                .map(revenue -> new RevenueDto(
                        revenue.getContractId(),
                        revenue.getFee(),
                        revenue.getDate()
                ))
                .collect(Collectors.toList());

        for (RevenueDto dto : revenueDtoList) {

            // Contract contract = dto.getContract();

            // 계약서 id
            Long contractId = dto.getContractId();
            ContractFeignResponse contractFeignResponse = contractFeignClient.findByContractId(contractId);

            // 수익 원금액
            Double fee = dto.getFee();

            // 1. 유통사 정산
            // 유통사 id
            Long distributorId = contractFeignResponse.getDistributorId();
            // 유통사 객체
            DistributorFeignResponse distributorFeignResponse = distributorFeignClient.findDistributorById(distributorId);
            // 유통사 비율
            Double distributorRate = distributorFeignResponse.getPercent() * 0.01;
            Double distributorFee = fee * distributorRate;

            fee -= distributorFee;

            Settlement distributorSettlement = Settlement.builder()
                    .contractId(contractId)
                    .type(DISTRIBUTOR)
                    .memberId(contractFeignResponse.getDistributorId())
                    .settleDate(LocalDateTime.now())
                    .fee(distributorFee)
                    .build();

            settlementRepository.save(distributorSettlement);

            // 2. 가창자 정산
            Double singerRate = contractFeignResponse.getSingerPercent() * 0.01;
            Double singerFee = fee * singerRate;
            fee -= singerFee;

            OstFeignResponse ostFeignResponse = ostFeignClient.findOstById(contractFeignResponse.getOstId());

            Settlement singerSettlement = Settlement.builder()
                    .contractId(contractId)
                    .type(SINGER)
                    // .memberId(contract.getOst().getSinger().getId())
                    .memberId(ostFeignResponse.getSingerId())
                    .settleDate(LocalDateTime.now())
                    .fee(singerFee)
                    .build();

            settlementRepository.save(singerSettlement);

            // 3. 제작사 정산
            Double producerRate = contractFeignResponse.getProducerPercent() * 0.01;
            Double producerFee = fee * producerRate;
            fee -= producerFee;

            Settlement producerSettlement = Settlement.builder()
                    .contractId(contractId)
                    .type(PRODUCER)
                    // .memberId(contract.getOst().getProducer().getId())
                    .memberId(ostFeignResponse.getProducerId())
                    .settleDate(LocalDateTime.now())
                    .fee(producerFee)
                    .build();

            settlementRepository.save(producerSettlement);

            // 4. 본사 정산
            Settlement companySettlement = Settlement.builder()
                    .contractId(contractId)
                    .type(COMPANY)
                    .memberId(0L)
                    .settleDate(LocalDateTime.now())
                    .fee(fee)
                    .build();

            settlementRepository.save(companySettlement);
        }
    }

    // 특정 월 특정 멤버의 정산금
    public Double getMembersSettlement(int year, int month, MemberType type, Long memberId) {
        log.info("year = {}", year);
        log.info("month = {}", month);
        log.info("type = {}", type.getType());
        log.info("memberId = {}", memberId);

        Double fee = settlementRepository.getSumByTypeAndMemberIdAndSettleDate(year, month, type, memberId);
        log.info("fee = {}", fee);
        return fee;
    }

    // 동시성 문제 테스트
    // private String nameStore;
    private ThreadLocal<String> nameStore = new ThreadLocal<>();
    public String saveAndFind(String name) {
        log.info("(save) saved value : nameStore = {}, to be saved : name = {}", nameStore.get(), name);
        nameStore.set(name);
        // 저장 로직에 1초가 걸리고, 그 후 조회
        sleep(1000);
        log.info("(get) saved value = {}", nameStore.get());
        // 해당 스레드가 스레드 로컬 모두 사용 후 저장된 값 제거해야 함.
        nameStore.remove();
        return nameStore.get();
    }

    public void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Page<Settlement> getSettlementList(Pageable pageable) {
        return settlementRepository.findAll(pageable);
    }

    // 특정 월 특정 멤버의 정산 객체
    public Page<Settlement> getSettlementListByDateAndMember(int year, int month, MemberType type, Long memberId, Pageable pageable) {
        return settlementRepository.findByTypeAndMemberIdAndSettleDate(year, month, type, memberId, pageable);
    }
}