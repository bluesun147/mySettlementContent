package com.haechan.mysettlement.domain.revenue.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class RevenueExcelDto {

    // ost
    private Long ostId;

    // 유통사
    private Long distributorId;


    // 수익
    private Double fee;

    // 날짜
    private LocalDateTime date;

    @Builder
    public RevenueExcelDto(Long ostId, Long distributorId, Double fee, LocalDateTime date) {
        this.ostId = ostId;
        this.distributorId = distributorId;
        this.fee = fee;
        this.date = date;
    }
}
