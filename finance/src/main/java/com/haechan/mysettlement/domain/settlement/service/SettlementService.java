package com.haechan.mysettlement.domain.settlement.service;

import com.haechan.mysettlement.domain.contract.entity.Contract;
import com.haechan.mysettlement.domain.revenue.dto.RevenueDto;
import com.haechan.mysettlement.domain.revenue.entity.Revenue;
import com.haechan.mysettlement.domain.revenue.repository.RevenueRepository;
import com.haechan.mysettlement.domain.settlement.dto.MemberType;
import com.haechan.mysettlement.domain.settlement.entity.Settlement;
import com.haechan.mysettlement.domain.settlement.repository.SettlementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.haechan.mysettlement.domain.settlement.dto.MemberType.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class SettlementService {

    private final SettlementRepository settlementRepository;
    private final RevenueRepository revenueRepository;

    // 특정달 수익에 대한 정산
    // 유통사 -> 가창자 -> 제작사 -> 본사 순
    // distributor -> singer -> producer
    public void calculate(LocalDate date) {
        // 특정달 수익 객체 전부 가져오기
        List<Revenue> revenueList = revenueRepository.findByDate(date);
        List<RevenueDto> revenueDtoList = revenueList.stream()
                .map(revenue -> new RevenueDto(
                        revenue.getContract(),
                        revenue.getFee(),
                        revenue.getDate()
                ))
                .collect(Collectors.toList());

        for (RevenueDto dto : revenueDtoList) {

            // 계약서
            Contract contract = dto.getContract();
            // 수익 원금액
            Double fee = dto.getFee();

            // 1. 유통사 정산
            Double distributorRate = contract.getDistributor().getPercent() * 0.01;
            Double distributorFee = fee * distributorRate;
            fee -= distributorFee;

            Settlement distributorSettlement = Settlement.builder()
                    .contract(contract)
                    .type(DISTRIBUTOR)
                    .memberId(contract.getDistributor().getId())
                    .settleDate(LocalDateTime.now())
                    .fee(distributorFee)
                    .build();

            settlementRepository.save(distributorSettlement);

            // 2. 가창자 정산
            Double singerRate = contract.getSingerPercent() * 0.01;
            Double singerFee = fee * singerRate;
            fee -= singerFee;

            Settlement singerSettlement = Settlement.builder()
                    .contract(contract)
                    .type(SINGER)
                    .memberId(contract.getOst().getSinger().getId())
                    .settleDate(LocalDateTime.now())
                    .fee(singerFee)
                    .build();

            settlementRepository.save(singerSettlement);

            // 3. 제작사 정산
            Double producerRate = contract.getProducerPercent() * 0.01;
            Double producerFee = fee * producerRate;
            fee -= producerFee;

            Settlement producerSettlement = Settlement.builder()
                    .contract(contract)
                    .type(PRODUCER)
                    .memberId(contract.getOst().getProducer().getId())
                    .settleDate(LocalDateTime.now())
                    .fee(producerFee)
                    .build();

            settlementRepository.save(producerSettlement);

            // 4. 본사 정산
            Settlement companySettlement = Settlement.builder()
                    .contract(contract)
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