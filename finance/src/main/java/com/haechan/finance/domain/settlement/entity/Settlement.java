package com.haechan.finance.domain.settlement.entity;

import com.haechan.finance.domain.settlement.dto.MemberType;
import com.haechan.finance.global.BaseTime;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

// 정산

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Settlement extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 계약서
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "contractId")
//    private Contract contract;
    // 간접 참조 (msa)
    private Long contractId;

    // 정산대상 타입
    // 추후에 enum 으로 변경해야 함.
    // 1-제작사 2-유통사 3-가창자
    @Column
    private MemberType type;

    // 정산 대상 id
    @Column
    private Long memberId;

    // 정산일
    @Column
    private LocalDateTime settleDate;

    // 정산금
    @Column
    private Double fee;

    @Builder
    public Settlement(Long contractId, MemberType type, Long memberId, LocalDateTime settleDate, Double fee) {
        this.contractId = contractId;
        this.type = type;
        this.memberId = memberId;
        this.settleDate = settleDate;
        this.fee = fee;
    }

}
