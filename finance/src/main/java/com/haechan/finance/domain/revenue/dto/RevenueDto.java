package com.haechan.finance.domain.revenue.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class RevenueDto {

//    // ost
//    private Long ostId;
//
//    // 유통사
//    private Long distributorId;

    private Long contractId;

    // 수익
    private Double fee;

    // 날짜
    private LocalDateTime date;

    @Builder
    public RevenueDto(Long contractId, Double fee, LocalDateTime date) {
        this.contractId = contractId;
        this.fee = fee;
        this.date = date;
    }
}
