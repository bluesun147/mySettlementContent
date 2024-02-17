package com.haechan.mysettlement.domain.settlement.service;

import com.haechan.mysettlement.domain.contract.entity.Contract;
import com.haechan.mysettlement.domain.revenue.dto.RevenueDto;
import com.haechan.mysettlement.domain.revenue.entity.Revenue;
import com.haechan.mysettlement.domain.revenue.repository.RevenueRepository;
import com.haechan.mysettlement.domain.settlement.entity.Settlement;
import com.haechan.mysettlement.domain.settlement.repository.SettlementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class SettlementService {

    private final SettlementRepository settlementRepository;
    private final RevenueRepository revenueRepository;

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

            Contract contract = dto.getContract();

            // 1. 유통사 정산
            Double distributorRate = contract.getDistributor().getPercent() * 0.01;
            Double distributorFee = dto.getFee() * distributorRate;

            Settlement settlement = Settlement.builder()
                    .contract(contract)
                    .type(1L)
                    .memberId(contract.getDistributor().getId())
                    .settleDate(LocalDateTime.now())
                    .fee(distributorFee)
                    .build();

            settlementRepository.save(settlement);
        }
    }
}