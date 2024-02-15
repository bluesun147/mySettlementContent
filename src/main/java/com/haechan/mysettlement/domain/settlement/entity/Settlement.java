package com.haechan.mysettlement.domain.settlement.entity;

import com.haechan.mysettlement.domain.contract.entity.Contract;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// 정산

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Settlement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 계약서
    @ManyToOne
    @JoinColumn(name = "contractId")
    private Contract contract;

    // 정산대상 타입
    // 추후에 enum 으로 변경해야 함.
    // 1-제작사 2-유통사 3-가창자
    @Column
    private Long type;

    // 정산 대상 id
    @Column
    private Long memberId;

    // 정산일
    @Column
    private LocalDateTime settleDate;

    // 정산금
    @Column
    private Double fee;

    // 업데이트 날짜
    @Column
    private LocalDateTime updateDate;

    @Builder
    public Settlement(Contract contract, Long type, Long memberId, LocalDateTime settleDate, Double fee) {
        this.contract = contract;
        this.type = type;
        this.memberId = memberId;
        this.settleDate = settleDate;
        this.fee = fee;
    }

}
