package com.haechan.mysettlement.domain.revenue.dto;

import com.haechan.mysettlement.domain.contract.entity.Contract;
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

    private Contract contract;

    // 수익
    private Double fee;

    // 날짜
    private LocalDateTime date;

    @Builder
    public RevenueDto(Contract contract, Double fee, LocalDateTime date) {
        this.contract = contract;
        this.fee = fee;
        this.date = date;
    }
}
