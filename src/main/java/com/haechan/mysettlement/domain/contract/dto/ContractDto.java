package com.haechan.mysettlement.domain.contract.dto;

import com.haechan.mysettlement.domain.contract.entity.Contract;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ContractDto {

    // 제작사 퍼센트
    private Double producerPercent;

    // 가창자 퍼센트
    private Double singerPercent;

    // 시작일
    private LocalDateTime startDate;

    // 종료일
    private LocalDateTime endDate;

    @Builder
    public ContractDto(
            Double producerPercent, Double singerPercent, LocalDateTime startDate, LocalDateTime endDate) {
        this.producerPercent = producerPercent;
        this.singerPercent = singerPercent;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static ContractDto of(Contract contract) {
        return ContractDto.builder()
                .producerPercent(contract.getProducerPercent())
                .singerPercent(contract.getSingerPercent())
                .startDate(contract.getStartDate())
                .endDate(contract.getEndDate())
                .build();
    }
}
