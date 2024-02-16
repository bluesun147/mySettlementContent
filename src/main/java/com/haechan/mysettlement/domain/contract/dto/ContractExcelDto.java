package com.haechan.mysettlement.domain.contract.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ContractExcelDto {

    // ost
    private Long ostId;

    // 제작사
    private Long producerId;

    // 유통사
    private Long distributorId;

    // 가창자
    private Long singerId;

    // 제작사 퍼센트
    private Double producerPercent;

    // 가창자 퍼센트
    private Double singerPercent;

    // 시작일
    private LocalDateTime startDate;

    // 종료일
    private LocalDateTime endDate;

    @Builder
    public ContractExcelDto(
            Long ostId, Long producerId, Long distributorId, Long singerId,
            Double producerPercent, Double singerPercent, LocalDateTime startDate, LocalDateTime endDate) {
        this.ostId = ostId;
        this.producerId = producerId;
        this.distributorId = distributorId;
        this.singerId = singerId;
        this.producerPercent = producerPercent;
        this.singerPercent = singerPercent;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
